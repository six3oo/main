package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

/**
 * Finds and lists all persons in address book whose tag contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindFavCommand extends Command {

    public static final String COMMAND_WORD = "findfav";
    public static final String COMMAND_ALIAS = "ffav";
    public static final String COMMAND_HELP = "findfav";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "Find all persons who are in the favourites list.\n"
            + "Parameters: N/A\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed all persons";

    @Override
    public CommandResult execute() {
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
