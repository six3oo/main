package systemtests;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.FavCommand.MESSAGE_FAVE_PERSON_SUCCESS;
import static seedu.address.logic.commands.FavCommand.MESSAGE_UNFAVE_PERSON_SUCCESS;
import static seedu.address.testutil.TestUtil.getLastIndex;
import static seedu.address.testutil.TestUtil.getMidIndex;
import static seedu.address.testutil.TestUtil.getPerson;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.KEYWORD_MATCHING_MEIER;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FavCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

public class FavCommandSystemTest extends AddressBookSystemTest {

    private static final String MESSAGE_INVALID_FAVE_COMMAND_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, FavCommand.MESSAGE_USAGE);

    @Test
    public void fave() {
        /* ----------------- Performing fave operation while an unfiltered list is being shown -------------------- */

        /* Case: add the first person in the list, command with leading spaces and trailing spaces to favourites list ->
         faved */
        Model expectedModel = getModel();
        String command = "     " + FavCommand.COMMAND_WORD + "      " + INDEX_FIRST_PERSON.getOneBased()
        + "       " + "true";
        ReadOnlyPerson favedPerson = favPerson(expectedModel, INDEX_FIRST_PERSON, true);
        String expectedResultMessage = String.format(MESSAGE_FAVE_PERSON_SUCCESS, favedPerson);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);

        /* Case: Adding the last person in the list to favourites -> faved */
        Model modelBeforeFavingLast = getModel();
        Index lastPersonIndex = getLastIndex(modelBeforeFavingLast);
        assertAddCommandSuccess(lastPersonIndex);

        /* Case: Undo faving the last person in the list -> last person restored */
        command = UndoCommand.COMMAND_WORD;
        expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeFavingLast, expectedResultMessage);

        /* Case: redo faving the last person in the list -> last person added to favourites again */
        command = RedoCommand.COMMAND_WORD;
        favPerson(modelBeforeFavingLast, lastPersonIndex, true);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeFavingLast, expectedResultMessage);

        /* Case: Add the middle person in the list to favourites list -> faved */
        Index middlePersonIndex = getMidIndex(getModel());
        assertAddCommandSuccess(middlePersonIndex);

        /* Case: Remove the middle person in the list from the favourites list -> unfaved */
        Index middlePersonIndex1 = getMidIndex(getModel());
        assertRemoveCommandSuccess(middlePersonIndex1);

        /* ------------------ Performing fave operation while a filtered list is being shown ---------------------- */

        /* Case: filtered person list, fave index within bounds of address book and person list -> faved */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        Index index = INDEX_FIRST_PERSON;
        assertTrue(index.getZeroBased() < getModel().getFilteredPersonList().size());
        assertAddCommandSuccess(index);

        /* Case: filtered person list, fave index within bounds of address book but out of bounds of person list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getAddressBook().getPersonList().size();
        command = FavCommand.COMMAND_WORD + " " + invalidIndex;
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* --------------------- Performing fave operation while a person card is selected ------------------------ */

        /* Case: delete the selected person -> person list panel selects the person before the deleted person */
        showAllPersons();
        expectedModel = getModel();
        Index selectedIndex = getLastIndex(expectedModel);
        Index expectedIndex = Index.fromZeroBased(selectedIndex.getZeroBased() - 1);
        selectPerson(selectedIndex);
        command = FavCommand.COMMAND_WORD + " " + selectedIndex.getOneBased();
        favedPerson = favPerson(expectedModel, selectedIndex, true);
        expectedResultMessage = String.format(MESSAGE_FAVE_PERSON_SUCCESS, favedPerson);
        assertCommandSuccess(command, expectedModel, expectedResultMessage, expectedIndex);

        /* --------------------------------- Performing invalid fave operation ------------------------------------ */

        /* Case: invalid index (0) -> rejected */
        command = FavCommand.COMMAND_WORD + " 0";
        assertCommandFailure(command, MESSAGE_INVALID_FAVE_COMMAND_FORMAT);

        /* Case: invalid index (-1) -> rejected */
        command = FavCommand.COMMAND_WORD + " -1";
        assertCommandFailure(command, MESSAGE_INVALID_FAVE_COMMAND_FORMAT);

        /* Case: invalid index (size + 1) -> rejected */
        Index outOfBoundsIndex = Index.fromOneBased(
                getModel().getAddressBook().getPersonList().size() + 1);
        command = FavCommand.COMMAND_WORD + " " + outOfBoundsIndex.getOneBased();
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets) -> rejected */
        assertCommandFailure(FavCommand.COMMAND_WORD + " abc", MESSAGE_INVALID_FAVE_COMMAND_FORMAT);

        /* Case: invalid arguments (extra argument) -> rejected */
        assertCommandFailure(FavCommand.COMMAND_WORD + " 1 abc", MESSAGE_INVALID_FAVE_COMMAND_FORMAT);

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("fAVe 1", MESSAGE_UNKNOWN_COMMAND);
    }
    /*
    /**
     * Removes the {@code ReadOnlyPerson} at the specified {@code index} in {@code model}'s address book.
     * @return the removed person
     *
    private ReadOnlyPerson removePerson(Model model, Index index) {
        ReadOnlyPerson targetPerson = getPerson(model, index);
        try {
            model.favPerson(targetPerson);
        } catch (DuplicatePersonException dpe) {
            throw new AssertionError("targetPerson is retrieved from model.");
        }
        return targetPerson;
    }
    */

    /**
     * Adds the {@code ReadOnlyPerson} at the specified {@code index} in {@code model}'s address book to the
     * address book's favourites list.
     * @return the added person
     */
    private ReadOnlyPerson favPerson(Model model, Index index, boolean status) {
        ReadOnlyPerson targetPerson = getPerson(model, index);
        try {
            model.favPerson(targetPerson, status);
        } catch (DuplicatePersonException dpe) {
            throw new AssertionError("targetPerson is already in the favourites list.");
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("targetPerson does not exist.");
        }
        return targetPerson;
    }

    /**
     * Faves the person at {@code toFave} by creating a default {@code FavCommand} using {@code toFave} and
     * performs the same verification as {@code assertCommandSuccess(String, Model, String)}.
     * @see FavCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertAddCommandSuccess(Index toFave) {
        Model expectedModel = getModel();
        ReadOnlyPerson favedPerson = favPerson(expectedModel, toFave, true);
        String expectedResultMessage = String.format(MESSAGE_FAVE_PERSON_SUCCESS, favedPerson);

        assertCommandSuccess(
                FavCommand.COMMAND_WORD + " " + toFave.getOneBased() + " " + "true",
            expectedModel, expectedResultMessage);
    }

    /**
     * Unfaves the person at {@code toUnFave} by creating a default {@code FavCommand} using {@code toUnFave} and
     * performs the same verification as {@code assertCommandSuccess(String, Model, String)}.
     * @see FavCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertRemoveCommandSuccess(Index toUnFave) {
        Model expectedModel = getModel();
        ReadOnlyPerson unFavedPerson = favPerson(expectedModel, toUnFave, false);
        String expectedResultMessage = String.format(MESSAGE_UNFAVE_PERSON_SUCCESS, unFavedPerson);

        assertCommandSuccess(
                FavCommand.COMMAND_WORD + " " + toUnFave.getOneBased() + " " + "false",
            expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card remains unchanged.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String)} except that the browser url
     * and selected card are expected to update accordingly depending on the card at {@code expectedSelectedCardIndex}.
     * @see DeleteCommandSystemTest#assertCommandSuccess(String, Model, String)
     * @see AddressBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
                                      Index expectedSelectedCardIndex) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);

        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }

        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
