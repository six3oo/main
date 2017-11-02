# jhchia7
###### \java\seedu\address\model\person\ChannelId.java
``` java

/**
 * Represents a Person's Channel ID in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidChannelId(String)}
 */
public class ChannelId {

    public static final String MESSAGE_CHANNEL_ID_CONSTRAINTS =
            "Person's channel ID can take any values, and it should not be blank";

    /*
     * The first character of the channel ID must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String CHANNEL_ID_VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Validates given channel ID.
     *
     * @throws IllegalValueException if given channel ID string is invalid.
     */
    public ChannelId(String channelId) throws IllegalValueException {
        requireNonNull(channelId);
        if (!isValidChannelId(channelId)) {
            throw new IllegalValueException(MESSAGE_CHANNEL_ID_CONSTRAINTS);
        }
        this.value = channelId;
    }

    /**
     * Returns true if a given string is a valid person channel ID.
     */
    public static boolean isValidChannelId (String test) {
        return test.matches(CHANNEL_ID_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ChannelId // instanceof handles nulls
                && this.value.equals(((ChannelId) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
