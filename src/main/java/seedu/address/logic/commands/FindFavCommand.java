package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import static seedu.address.model.Model.PREDICATE_FAVOURITE;

/**
 * Finds and lists all persons who are favourites.
 */
public class FindFavCommand extends Command {

    public static final String COMMAND_WORD = "findfav";
    public static final String COMMAND_ALIAS = "ffav";
    public static final String COMMAND_HELP = "findfav";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "Find all persons who are in the favourites list.\n"
            + "Parameters: N/A\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed favourite persons";

    @Override
    public CommandResult execute() throws CommandException {
        model.updateFilteredPersonList(PREDICATE_FAVOURITE);
        return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredPersonList().size()));
    }
}
