# jhchia7
###### \java\seedu\address\logic\commands\SendCommand.java
``` java

/**
 * Sends a message to the contact through email
 */

public class SendCommand extends Command {

    public static final String COMMAND_WORD = "send";
    public static final String COMMAND_ALIAS = "snd";
    public static final String COMMAND_HELP = "send INDEX";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":Opens up third-party communication application with"
                                                            + " the information of the person identified"
                                                            + " by the index number used in the last person listing. ";


    public static final String MESSAGE_OPEN_MAIL_SUCCESS = "Opened Mail App...";
    public static final String MESSAGE_NO_MAIL = "Contact does not have an email address.";

    private final Index index;

    /**
     * @param index of the person in the filtered person list to send message to
     */
    public SendCommand(Index index) {
        this.index = index;
    }


    @Override
    public CommandResult execute() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToCommunicate = lastShownList.get(index.getZeroBased());
        String emailOfPerson = personToCommunicate.getEmail().toString();

        //Open Windows 10 Mail app
        try {
            Process p = Runtime.getRuntime().exec("cmd /c start mailto:" + emailOfPerson);
            p.waitFor();
            System.out.println("Mail launched!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        EventsCenter.getInstance().post(new SendMessageEvent());
        return new CommandResult(String.format(MESSAGE_OPEN_MAIL_SUCCESS));
    }


}
```
###### \java\seedu\address\logic\parser\SendCommandParser.java
``` java

/**
 * Parses input arguments and creates a new SendCommand object
 */
public class SendCommandParser implements Parser<SendCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SendCommand
     * and returns an SendCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SendCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new SendCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SendCommand.MESSAGE_USAGE));
        }
    }
}
```
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
###### \java\seedu\address\ui\BrowserPanel.java
``` java

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    private Channel channel;

    @FXML
    private TextFlow channelTitle;
    @FXML
    private ImageView channelThumbnail;
    @FXML
    private TextFlow channelDescription;
    @FXML
    private TextFlow subscriberCount;
    @FXML
    private TextFlow viewCount;
    @FXML
    private TextFlow createDate;


    public BrowserPanel() {
        super(FXML);

        registerAsAnEventHandler(this);
    }

    /**
     * Calls helper methods to get Channel title, description, subscriber count, view count
     * and create date
     * @param person target person to be selected
     * @throws IOException
     */

    private void loadPersonPage(ReadOnlyPerson person) throws IOException {

        channel = YouTubeAuthorize.getYouTubeChannel(person.getChannelId().toString());

        Text title = new Text(getChannelTitle());
        title.setFont(Font.font("Calibri", 40));
        title.setFill(Color.WHITE);
        channelTitle.getChildren().clear();
        channelTitle.getChildren().add(title);

        Text description = new Text("Description:\n" + getChannelDescription());
        description.setFont(Font.font("Calibri", 20));
        description.setFill(Color.WHITE);
        channelDescription.getChildren().clear();
        channelDescription.getChildren().add(description);

        Text subNumber = new Text("Subscribers: " + getSubCount());
        subNumber.setFont(Font.font("Calibri", 25));
        subNumber.setFill(Color.WHITE);
        subscriberCount.getChildren().clear();
        subscriberCount.getChildren().add(subNumber);

        Text viewNumber = new Text("Views: " + getViewCount());
        viewNumber.setFont(Font.font("Calibri", 25));
        viewNumber.setFill(Color.WHITE);
        viewCount.getChildren().clear();
        viewCount.getChildren().add(viewNumber);

        Text date = new Text("Created: " + getCreateDate());
        date.setFont(Font.font("Calibri", 25));
        date.setFill(Color.WHITE);
        createDate.getChildren().clear();
        createDate.getChildren().add(date);

        Image thumbnail = getChannelThumbnail();
        channelThumbnail.setImage(thumbnail);


    }


    /**
     * Frees resources allocated to the browser.
     */

    public void freeResources() {

        channelTitle = null;
        channelThumbnail = null;
        channelDescription = null;
        subscriberCount = null;
        viewCount = null;
        createDate = null;

    }


    private String getChannelTitle() throws IOException {

        return channel.getSnippet().getTitle();

    }

    private Image getChannelThumbnail() {

        Image thumbnail = new Image(channel.getSnippet().getThumbnails().getHigh().getUrl());

        return thumbnail;

    }

    private String getChannelDescription() {

        return channel.getSnippet().getDescription();
    }

    private String getSubCount() {

        return formatNumber(channel.getStatistics().getSubscriberCount().longValue());
    }

    private String getViewCount() {

        return formatNumber(channel.getStatistics().getViewCount().longValue());
    }

    private String getCreateDate() {
        return channel.getSnippet().getPublishedAt().toStringRfc3339();

    }

    /**
     * Formats number with thousand, million and billion suffix
     * @param number to be formatted with suffix
     * @return
     */
    private String formatNumber(long number) {
        final long thousand = 1000L;
        final long million = 1000000L;
        final long billion = 1000000000L;

        if (number >= billion) {
            return String.format("%.1f%c", (double) number / billion, 'b');
        } else if (number >= million) {
            return String.format("%.1f%c", (double) number / million, 'm');
        } else if (number >= thousand) {
            return String.format("%.1f%c", (double) number / thousand, 'k');
        } else {
            return number + "";
        }
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) throws IOException {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection().person);
    }
}
```
