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
    private String[] argArray;

    /**
     * Parses the given {@code String} of arguments in the context of the FavCommand
     * and returns an FavCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FavCommand parse(String args) throws ParseException {
        try {
            argArray = args.split("\\s+");
            if (argArray[1] == "true") {
                status = true;
            } else if (argArray[1] == "false") {
                status = false;
            }
            Index index = ParserUtil.parseIndex(argArray[0]);
            return new FavCommand(index, status);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FavCommand.MESSAGE_USAGE));
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FavCommand.MESSAGE_USAGE));
        }
    }
}
