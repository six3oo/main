package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Adds a person identified using it's last displayed index from the address book to the favourites list.
 */

public class FavCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "fave";
    public static final String COMMAND_ALIAS = "fv";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds or removes the person identified by the index number"
            + " used in the last person listing to a favourites list.\n"
            + "Parameters: INDEX (must be a positive integer),"
            + " STATUS (must be a boolean variable, e.g. 'true' or 'false')\n"
            + "Example: " + COMMAND_WORD + " 1 true";

    public static final String MESSAGE_FAVE_PERSON_SUCCESS = "Added Person to Favourites: %1$s";
    public static final String MESSAGE_UNFAVE_PERSON_SUCCESS = "Removed Person from Favourites: %1$s";

    private final Index targetIndex;
    private boolean status;

    public FavCommand(Index targetIndex, boolean status) {
        this.targetIndex = targetIndex;
        this.status = status;
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToFave = lastShownList.get(targetIndex.getZeroBased());

        try {
            model.favPerson(personToFave, status);
        } catch (DuplicatePersonException dpe) {
            assert false : "The target person cannot be already in the favourites list";
        } catch (PersonNotFoundException pnfe) {
            assert false : "The target person cannot be found";
        }

        if (status == true) {
            return new CommandResult(String.format(MESSAGE_FAVE_PERSON_SUCCESS, personToFave));
        } else {
            return new CommandResult(String.format(MESSAGE_UNFAVE_PERSON_SUCCESS, personToFave));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FavCommand // instanceof handles nulls
                && this.targetIndex.equals(((FavCommand) other).targetIndex)); // state check
    }
}
