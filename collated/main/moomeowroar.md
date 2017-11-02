# moomeowroar
###### \java\seedu\address\logic\commands\ChangePwdCommand.java
``` java
/**
 * Sends a message to the contact through email
 */

public class ChangePwdCommand extends Command {

    public static final String COMMAND_WORD = "changepwd";
    public static final String COMMAND_ALIAS = "cpwd";
    public static final String COMMAND_HELP = "changepwd NEW_PASSWORD OLD_PASSWORD";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes password. \n"
            + COMMAND_WORD + " <new_password> <old_password>";


    public static final String MESSAGE_NO_SETTINGS = "No available settings.";
    public static final String MESSAGE_CHANGE_SUCCESS = "Settings changed successfully.";

    private final String oldPwd;
    private final String newPwd;

    /**
     * @param String Name of setting to change
     */
    public ChangePwdCommand(String oldPwd, String newPwd) {
        this.oldPwd = oldPwd;
        this.newPwd = newPwd;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (model.getUserPrefs().changePwd(oldPwd, newPwd)) {
            return new CommandResult(MESSAGE_CHANGE_SUCCESS);
        }
        return new CommandResult(MESSAGE_NO_SETTINGS);
    }


}
```
###### \java\seedu\address\logic\parser\ChangePwdCommandParser.java
``` java
/**
 * Parses input arguments and creates a new ChangePwdCommand object
 */
public class ChangePwdCommandParser implements Parser<ChangePwdCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an ChangePwdCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ChangePwdCommand parse(String args) throws ParseException {
        try {
            String[] array = ParserUtil.parseByDelimiter(args, " ");
            if (array.length == 2) {
                return new ChangePwdCommand("", array[1]);
            } else if (array.length == 3) {
                return new ChangePwdCommand(array[2], array[1]);
            } else {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangePwdCommand.MESSAGE_USAGE));
            }
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangePwdCommand.MESSAGE_USAGE));
        }
    }

}
```
###### \java\seedu\address\logic\parser\ChangeThemeCommandParser.java
``` java
/**
 * Parses input arguments and creates a new ChangePwdCommand object
 */
public class ChangeThemeCommandParser implements Parser<ChangeThemeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an ChangePwdCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ChangeThemeCommand parse(String args) throws ParseException {
        try {
            String[] array = ParserUtil.parseByDelimiter(args, " ");
            if (array.length == 2) {
                if (array[1].equals("light")) {
                    return new ChangeThemeCommand("LightTheme");
                } else if (array[1].equals("dark")) {
                    return new ChangeThemeCommand("DarkTheme");
                }
            }
            throw new ParseException(
                    String.format(ChangeThemeCommand.MESSAGE_NO_THEME, ChangeThemeCommand.MESSAGE_USAGE));
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangeThemeCommand.MESSAGE_USAGE));
        }
    }

}
```
###### \java\seedu\address\ui\LockScreen.java
``` java
/**
 * The Lock Screen Window.
 * Provides a password field to enter the password.
 */
public class LockScreen extends UiPart<Region> {

    private static final String ICON = "/images/address_book_32.png";
    private static final String FXML = "LockScreen.fxml";
    private static final int MIN_HEIGHT = 100;
    private static final int MIN_WIDTH = 300;
    private int x = 0;
    private int y = 0;
    private String theme = "";

    private Logic logic;
    private Config config;
    private UserPrefs prefs;
    private Stage primaryStage;
    private MainWindow mainWindow;

    @FXML
    private StackPane pwdBoxPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private VBox vBox;

    public LockScreen(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        super(FXML);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.config = config;
        this.prefs = prefs;

        // Configure the UI
        setTitle("Login");
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize();
        Scene scene = new Scene(getRoot());
        primaryStage.setScene(scene);

        initThemeFromSettings();
    }

    /**
     * Loads the passwordField into the stage.
     */
    void fillInnerParts() {
        PasswordField pwBox = new PasswordField();
        pwBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (logic.isPassword(pwBox.getText())) {
                    loadMainWindow();
                } else {
                    setTitle("Wrong Password!");
                    pwBox.setStyle("-fx-text-box-border: #f44b42; -fx-focus-color: #f44b42;");
                    shakeStage();
                }
                pwBox.clear();
            }
        });
        pwdBoxPlaceholder.getChildren().add(pwBox);
    }

    private void initThemeFromSettings() {
        theme = prefs.getGuiSettings().getTheme();
        vBox.getStylesheets().remove(0);
        vBox.getStylesheets().add("/view/" + theme + ".css");
    }

    void loadMainWindow() {
        mainWindow = new MainWindow(primaryStage, config, prefs, logic);
        mainWindow.show(); //This should be called before creating other UI parts
        mainWindow.fillInnerParts();
    }

    /**
     * Show the stage.
     */
    void show() {
        primaryStage.show();
    }

    /**
     * Hide the stage.
     */
    void hide() {
        if (mainWindow != null) {
            prefs.updateLastUsedGuiSetting(mainWindow.getCurrentGuiSetting());
            mainWindow.releaseResources();
        }
        primaryStage.hide();
    }

    public Stage getMainWindowPrimaryStage() {
        return mainWindow.getPrimaryStage();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the given image as the icon of the main window.
     * @param iconSource e.g. {@code "/images/help_icon.png"}
     */
    private void setIcon(String iconSource) {
        FxViewUtil.setStageIcon(primaryStage, iconSource);
    }

    /**
     * Sets the default size based on user preferences.
     */
    private void setWindowDefaultSize() {
        primaryStage.setHeight(MIN_HEIGHT);
        primaryStage.setWidth(MIN_WIDTH);
    }

    /**
     * Sets the window minimum size.
     */
    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Function to animate the stage for wrong password.
     */
    private void shakeStage() {
        Timeline timelineX = new Timeline(new KeyFrame(Duration.seconds(0.05), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (x == 0) {
                    primaryStage.setX(primaryStage.getX() + 10);
                    x = 1;
                } else {
                    primaryStage.setX(primaryStage.getX() - 10);
                    x = 0;
                }
            }
        }));

        timelineX.setCycleCount(4);
        timelineX.setAutoReverse(false);
        timelineX.play();

        Timeline timelineY = new Timeline(new KeyFrame(Duration.seconds(0.05), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (y == 0) {
                    primaryStage.setY(primaryStage.getY() + 10);
                    y = 1;
                } else {
                    primaryStage.setY(primaryStage.getY() - 10);
                    y = 0;
                }
            }
        }));

        timelineY.setCycleCount(4);
        timelineY.setAutoReverse(false);
        timelineY.play();
    }
}
```
