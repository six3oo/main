package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.FavCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new FavCommand object
 */
public class FavCommandParser implements Parser<FavCommand> {
    private boolean status;

    /**
     * Parses the given {@code String} of arguments in the context of the FavCommand
     * and returns an FavCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FavCommand parse(String args) throws ParseException {
        args = args.trim();
        try {
            String[] argArray = args.split("\\s+");
            Index index = ParserUtil.parseIndex(argArray[0]);

            if (argArray[1].equals("true")) {
                status = true;
            }
            else if (argArray[1].equals("false")) {
                status = false;
            }
            else {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FavCommand.MESSAGE_USAGE));
            }
            return new FavCommand(index, status);
        } catch (IllegalValueException | ArrayIndexOutOfBoundsException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FavCommand.MESSAGE_USAGE));
        }
    }

}
