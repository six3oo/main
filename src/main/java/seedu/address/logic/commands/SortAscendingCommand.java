package seedu.address.logic.commands;

public class SortAscendingCommand extends Command{

    public final static String COMMAND_WORD = "sortAscend";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "List all persons by name "
            + "in ascending alphabetical order.\n ";

    @Override
    public CommandResult execute() {
        model.sortNameAscend();
        return new CommandResult("List is now arranged in ascending order.\n");
    }
}
