package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showFirstPersonOnly;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code FavCommand}.
 */
public class FavCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        ReadOnlyPerson personToFave = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        FavCommand favCommand = prepareCommand(INDEX_FIRST_PERSON, true);


        String expectedMessage = String.format(FavCommand.MESSAGE_FAVE_PERSON_SUCCESS, personToFave);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.favPerson(personToFave, true);

        assertCommandSuccess(favCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_remove_validIndexUnfilteredList_success() throws Exception {
        ReadOnlyPerson personToFave = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        FavCommand favCommand = prepareCommand(INDEX_FIRST_PERSON, false);


        String expectedMessage = String.format(FavCommand.MESSAGE_UNFAVE_PERSON_SUCCESS, personToFave);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.favPerson(personToFave, false);

        assertCommandSuccess(favCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        FavCommand favCommand = prepareCommand(outOfBoundIndex, true);

        assertCommandFailure(favCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showFirstPersonOnly(model);

        ReadOnlyPerson personToFave = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        FavCommand favCommand = prepareCommand(INDEX_FIRST_PERSON, true);

        String expectedMessage = String.format(FavCommand.MESSAGE_FAVE_PERSON_SUCCESS, personToFave);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.favPerson(personToFave, true);

        assertCommandSuccess(favCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showFirstPersonOnly(model);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        FavCommand favCommand = prepareCommand(outOfBoundIndex, true);

        assertCommandFailure(favCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        FavCommand favFirstCommand = new FavCommand(INDEX_FIRST_PERSON, true);
        FavCommand favSecondCommand = new FavCommand(INDEX_SECOND_PERSON, true);

        // same object -> returns true
        assertTrue(favFirstCommand.equals(favFirstCommand));

        // same values -> returns true
        FavCommand favFirstCommandCopy = new FavCommand(INDEX_FIRST_PERSON, true);
        assertTrue(favFirstCommand.equals(favFirstCommandCopy));

        // different types -> returns false
        assertFalse(favFirstCommand.equals(1));

        // null -> returns false
        assertFalse(favFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(favFirstCommand.equals(favSecondCommand));
    }

    /**
     * Returns a {@code FavCommand} with the parameter {@code index}.
     */
    private FavCommand prepareCommand(Index index, boolean status) {
        FavCommand favCommand = new FavCommand(index, status);
        favCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return favCommand;
    }
}
