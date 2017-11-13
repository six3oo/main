# moomeowroar
###### \java\seedu\address\logic\commands\ChangePwdCommand.java
``` java
/**
 * Command to change password
 */

public class ChangePwdCommand extends Command {

    public static final String COMMAND_WORD = "changepwd";
    public static final String COMMAND_ALIAS = "cpwd";
    public static final String COMMAND_HELP = "changepwd NEW_PASSWORD OLD_PASSWORD";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes password. \n"
            + COMMAND_WORD + " <new_password> <old_password>";


    public static final String MESSAGE_CHANGE_FAILURE = "Wrong password.";
    public static final String MESSAGE_CHANGE_SUCCESS = "Password changed successfully.";

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
        } else {
            throw new CommandException(MESSAGE_CHANGE_FAILURE);
        }
    }


}
```
###### \java\seedu\address\logic\commands\ChangeThemeCommand.java
``` java
/**
 * Change theme
 */

public class ChangeThemeCommand extends Command {

    public static final String COMMAND_WORD = "theme";
    public static final String COMMAND_ALIAS = "t";
    public static final String COMMAND_HELP = "theme THEME_NAME";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes theme. \n"
            + COMMAND_WORD + " <dark/light/youtube>";


    public static final String MESSAGE_NO_THEME = "Invalid theme.";
    public static final String MESSAGE_CHANGE_SUCCESS = "Theme changed successfully.";

    private final String themeName;

    /**
     * @param String Name of theme to change
     */
    public ChangeThemeCommand(String themeName) {
        this.themeName = themeName;
    }

    @Override
    public CommandResult execute() throws CommandException {
        EventsCenter.getInstance().post(new ChangeThemeEvent(themeName));
        return new CommandResult(MESSAGE_CHANGE_SUCCESS);
    }

}
```
###### \java\seedu\address\logic\LogicManager.java
``` java
    /**
     * Retrieves all available commands according to user input
     * @param commandText
     * @return output of available commands as a String
     */

    public String liveHelp(String commandText) throws IllegalValueException {
        String finalString = "";
        ArrayList<String> result = addressBookParser.filterCommand(commandText, false);
        for (String string: result) {
            finalString += string + "\n";
        }
        return finalString;
    }

    public String getCommandWord(String commandText) throws IllegalValueException {
        ArrayList<String> result = addressBookParser.filterCommand(commandText, true);
        if (result.isEmpty() || result.size() > 1) {
            return "nil";
        } else {
            return result.get(0);
        }
    }

    /**
     * Check if password is correct and set lock
     * @param commandText
     * @return
     */
    public boolean isPassword(String password) {
        if (model.getUserPrefs().checkPassword(password)) {
            isLock = false;
            return true;
        } else {
            isLock = true;
            return false;
        }
    }

    public boolean isAddressBookLock() {
        return isLock;
    }
```
###### \java\seedu\address\logic\parser\AddressBookParser.java
``` java
    /**
     * Searches for all commands that contains the input text
     * @param text
     * @return ArrayList of command words
     */

    public ArrayList<String> filterCommand(String text, boolean isExact) throws IllegalValueException {
        String[] array = ParserUtil.parseByDelimiter(text, " ");
        ArrayList<String> result = new ArrayList<String>();
        HashMap<String, String> commandList = new HashMap<String, String>();
        commandList.put(AddCommand.COMMAND_WORD, AddCommand.COMMAND_HELP);
        commandList.put(ChangePwdCommand.COMMAND_WORD, ChangePwdCommand.COMMAND_HELP);
        commandList.put(EditCommand.COMMAND_WORD, EditCommand.COMMAND_HELP);
        commandList.put(DeleteCommand.COMMAND_WORD, DeleteCommand.COMMAND_HELP);
        commandList.put(FavCommand.COMMAND_WORD, FavCommand.COMMAND_HELP);
        commandList.put(FindFavCommand.COMMAND_WORD, FindFavCommand.COMMAND_HELP);
        commandList.put(FindFavCommand.COMMAND_ALIAS, FindFavCommand.COMMAND_HELP);
        commandList.put(ClearCommand.COMMAND_WORD, ClearCommand.COMMAND_HELP);
        commandList.put(FindCommand.COMMAND_WORD, FindCommand.COMMAND_HELP);
        commandList.put(FindTagCommand.COMMAND_WORD, FindTagCommand.COMMAND_HELP);
        commandList.put(FindEmailCommand.COMMAND_WORD, FindEmailCommand.COMMAND_HELP);
        commandList.put(ListCommand.COMMAND_WORD, ListCommand.COMMAND_HELP);
        commandList.put(HistoryCommand.COMMAND_WORD, HistoryCommand.COMMAND_HELP);
        commandList.put(HelpCommand.COMMAND_WORD, HelpCommand.COMMAND_HELP);
        commandList.put(UndoCommand.COMMAND_WORD, UndoCommand.COMMAND_HELP);
        commandList.put(ExitCommand.COMMAND_WORD, ExitCommand.COMMAND_HELP);
        commandList.put(SendCommand.COMMAND_WORD, SendCommand.COMMAND_HELP);
        commandList.put(RedoCommand.COMMAND_WORD, RedoCommand.COMMAND_HELP);
        commandList.put(SelectCommand.COMMAND_WORD, SelectCommand.COMMAND_HELP);
        commandList.put(ChangeThemeCommand.COMMAND_WORD, ChangeThemeCommand.COMMAND_HELP);
        for (HashMap.Entry<String, String> entry : commandList.entrySet()) {
            if (isExact) {
                if (entry.getKey().equals(array[0])) {
                    result.add(entry.getKey());
                }
            } else {
                if (entry.getKey().contains(array[0])) {
                    result.add(entry.getValue());
                }
            }
        }
        return result;
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

    public static final int NUM_OF_PARAM = 2; //Inclusive of command word

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an ChangePwdCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ChangeThemeCommand parse(String args) throws ParseException {
        try {
            String[] array = ParserUtil.parseByDelimiter(args, " ");
            if (array.length == NUM_OF_PARAM) {
                switch (array[1]) {
                case "light":
                    return new ChangeThemeCommand("LightTheme");
                case "dark":
                    return new ChangeThemeCommand("DarkTheme");
                case "youtube":
                    return new ChangeThemeCommand("YoutubeTheme");
                default:
                    break;
                }
            }
            throw new ParseException(
                    ChangeThemeCommand.MESSAGE_NO_THEME + "\n" + ChangeThemeCommand.MESSAGE_USAGE);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    ChangeThemeCommand.MESSAGE_NO_THEME + "\n" + ChangeThemeCommand.MESSAGE_USAGE);
        }
    }

}
```
###### \java\seedu\address\ui\CommandBox.java
``` java
    /**
     * Handles the key press event, {@code keyEvent}.
     */
    @FXML
    private void handleKeyReleased(KeyEvent keyEvent) throws IllegalValueException {
        switch (keyEvent.getCode()) {
        case UP:
            // As up and down buttons will alter the position of the caret,
            // consuming it causes the caret's position to remain unchanged
            keyEvent.consume();
            navigateToPreviousInput();
            break;
        case DOWN:
            keyEvent.consume();
            navigateToNextInput();
            break;
        default:
            if (keyEvent.getCode() == KeyCode.ENTER) {
                commandWord.setVisible(false);
            } else {
                if (commandText.equals("find") || commandText.equals("findemail") || commandText.equals("findtag")) {
                    handleCommandInputChanged();
                    raise(new NewResultAvailableEvent("", false));
                } else {
                    raise(new NewResultAvailableEvent(logic.liveHelp(commandTextField.getText()), false));
                }
                if (keyEvent.getCode() == KeyCode.SPACE || keyEvent.getCode() == KeyCode.BACK_SPACE) {
                    commandText = logic.getCommandWord(commandTextField.getText());
                    if (commandText.equals("nil")) {
                        commandWord.setVisible(false);
                    } else {
                        commandWord.setVisible(true);
                        commandWord.setText(commandText);
                    }
                    if (commandTextField.getText().equals("")) {
                        raise(new NewResultAvailableEvent("", false));
                    }
                }
            }
            break;
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
###### \resources\view\CommandBox.fxml
``` fxml
  <Button fx:id="commandWord" mnemonicParsing="false" text="Find" textAlignment="right" StackPane.alignment="CENTER_LEFT" mouseTransparent="true" visible="false" minHeight="50" maxHeight="50"/>
```
###### \resources\view\DarkTheme.css
``` css
#commandWord {
    -fx-background-color: #383838;
    -fx-padding: 0 0 0 22px;
    -fx-background-insets: 2px 0 2px 0;
    -fx-background-radius: 3px;
    -fx-border-insets: 0 0px 0 0px;
    -fx-border-width: 0 0 0 0;
    -fx-border-color: #d0d0d0;
    -fx-font-size: 14pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: #699cff;
}

#personListView {
    -fx-background-color: #383838;
}

#resultDisplayPlaceholder {
    -fx-effect: dropshadow(three-pass-box, #282a38, 5, 0, 0, 2);
}

#bottomPlaceholder {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}
```
###### \resources\view\LightTheme.css
``` css
.background {
    -fx-background-color: #eeede6;
    background-color: #eeede6; /* Used in the default.html file */
}


.label {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: #555555;
    -fx-opacity: 0.9;
}

.label-bright {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.label-header {
    -fx-font-size: 32pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.text-field {
    -fx-font-size: 12pt;
    -fx-font-family: "Segoe UI Semibold";
}

.tab-pane {
    -fx-padding: 0 0 0 1;
}

.tab-pane .tab-header-area {
    -fx-padding: 0 0 0 0;
    -fx-min-height: 0;
    -fx-max-height: 0;
}

.table-view {
    -fx-base: #eeede6;
    -fx-control-inner-background: #eeede6;
    -fx-background-color: #eeede6;
    -fx-table-cell-border-color: transparent;
    -fx-table-header-border-color: transparent;
    -fx-padding: 5;
}

.table-view .column-header-background {
    -fx-background-color: transparent;
}

.table-view .column-header, .table-view .filler {
    -fx-size: 35;
    -fx-border-width: 0 0 1 0;
    -fx-background-color: transparent;
    -fx-border-color:
        transparent
        transparent
        derive(-fx-base, 80%)
        transparent;
    -fx-border-insets: 0 10 1 0;
}

.table-view .column-header .label {
    -fx-font-size: 20pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: #666666;
    -fx-alignment: center-left;
    -fx-opacity: 1;
}

.table-view:focused .table-row-cell:filled:focused:selected {
    -fx-background-color: -fx-focus-color;
}

.split-pane:horizontal .split-pane-divider {
    -fx-background-color: #eeede6;
    -fx-border-color: transparent transparent transparent #ebebeb;
}

.split-pane {
    -fx-border-radius: 1;
    -fx-border-width: 1;
    -fx-background-color: #eeede6;
}

.list-view {
    -fx-background-insets: 0;
    -fx-padding: 0;
}

.cell_big_label {
    -fx-font-family: "Segoe UI Semibold";
    -fx-font-size: 20px;
    -fx-text-fill: #2e2e2e;
}

.cell_small_label {
    -fx-font-family: "Segoe UI";
    -fx-font-size: 14px;
    -fx-text-fill: #2e2e2e;
}

.list-cell {
    -fx-label-padding: 0 0 0 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 0 0;
    -fx-border-color: #c3c3c3 transparent transparent transparent;
    -fx-border-top-width: 1px;
}

.list-cell .label {
    -fx-text-fill: #2e2e2e;
}

.list-cell:filled:even {
    -fx-background-color: #eeede6;
}

.list-cell:filled:odd {
    -fx-background-color: #eeede6;
}

.list-cell:filled:selected {
    -fx-background-color: #b8c975;
}

.list-cell:filled:selected #cardPane {
    -fx-border-color: #3e7b91;
    -fx-border-width: 0;
}

.list-cell:filled:selected .cell_big_label {
    -fx-text-fill: #FFFFFF;
}

.list-cell:empty {
    -fx-border-width: 0px;
}

.anchor-pane {
     -fx-background-color: #eeede6;
}

.pane-with-border {
     -fx-background-color: #eeede6;
     -fx-border-color: #eeede6;
     -fx-border-top-width: 1px;
}

.pane-transparent {
     -fx-background-color: transparent;
}

.pwd-field {
    -fx-background-color: #eeede6;
    -fx-padding: 20px;
}

.status-bar {
    -fx-background-color: #deded8;
    -fx-text-fill: black;
}

.result-display {
    -fx-background-color: #eeede6;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: #c3c3c3;
    -fx-border-color: #eeede6;
    -fx-border-width: 2px;
    -fx-border-insets: 0px 10px 10px 10px;
    -fx-background-radius: 0px;
}

.result-display .label {
    -fx-text-fill: black !important;
}

.status-bar .label {
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: #61594a;
}

.status-bar-with-border {
    -fx-background-color: #deded8;
    -fx-border-color: #deded8;
    -fx-border-width: 0px;
}

.status-bar-with-border .label {
    -fx-text-fill: #61594a;
}

.grid-pane {
    -fx-background-color: #deded8;
    -fx-border-color: #deded8;
    -fx-border-width: 0px;
}

.grid-pane .anchor-pane {
    -fx-background-color: #deded8;
}

.context-menu {
    -fx-background-color: #eeede6;
}

.context-menu .label {
    -fx-text-fill: #2e2e2e;
}

.menu-bar {
    -fx-background-color: #eeede6;
}

.menu-bar .label {
    -fx-font-size: 14pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: #2e2e2e;
    -fx-opacity: 0.9;
}

.menu .left-container {
    -fx-background-color: black;
}

/*
 * Metro style Push Button
 * Author: Pedro Duque Vieira
 * http://pixelduke.wordpress.com/2012/10/23/jmetro-windows-8-controls-on-java/
 */
.button {
    -fx-padding: 5 22 5 22;
    -fx-border-color: #e2e2e2;
    -fx-border-width: 2;
    -fx-background-radius: 0;
    -fx-background-color: #1d1d1d;
    -fx-font-family: "Segoe UI", Helvetica, Arial, sans-serif;
    -fx-font-size: 11pt;
    -fx-text-fill: #d8d8d8;
    -fx-background-insets: 0 0 0 0, 0, 1, 2;
}

.button:hover {
    -fx-background-color: #b8c975;
}

.button:pressed, .button:default:hover:pressed {
  -fx-background-color: white;
  -fx-text-fill: #1d1d1d;
}

.button:focused {
    -fx-border-color: white, white;
    -fx-border-width: 1, 1;
    -fx-border-style: solid, segments(1, 1);
    -fx-border-radius: 0, 0;
    -fx-border-insets: 1 1 1 1, 0;
}

.button:disabled, .button:default:disabled {
    -fx-opacity: 0.4;
    -fx-background-color: #1d1d1d;
    -fx-text-fill: white;
}

.button:default {
    -fx-background-color: -fx-focus-color;
    -fx-text-fill: #ffffff;
}

.button:default:hover {
    -fx-background-color: derive(-fx-focus-color, 30%);
}

.dialog-pane {
    -fx-background-color: #1d1d1d;
}

.dialog-pane > *.button-bar > *.container {
    -fx-background-color: #1d1d1d;
}

.dialog-pane > *.label.content {
    -fx-font-size: 14px;
    -fx-font-weight: bold;
    -fx-text-fill: #1d1d1d;;
}

.dialog-pane:header *.header-panel {
    -fx-background-color: derive(#1d1d1d, 25%);
}

.dialog-pane:header *.header-panel *.label {
    -fx-font-size: 18px;
    -fx-font-style: italic;
    -fx-fill: #1d1d1d;;
    -fx-text-fill: #1d1d1d;;
}

.scroll-bar {
    -fx-background-color: #eeede6;
}

.scroll-bar .thumb {
    -fx-background-color: derive(#1d1d1d, 50%);
    -fx-background-insets: 3;
}

.scroll-bar .increment-button, .scroll-bar .decrement-button {
    -fx-background-color: transparent;
    -fx-padding: 0 0 0 0;
}

.scroll-bar .increment-arrow, .scroll-bar .decrement-arrow {
    -fx-shape: " ";
}

.scroll-bar:vertical .increment-arrow, .scroll-bar:vertical .decrement-arrow {
    -fx-padding: 1 8 1 8;
}

.scroll-bar:horizontal .increment-arrow, .scroll-bar:horizontal .decrement-arrow {
    -fx-padding: 8 1  8 1;
}

#cardPane {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}

#commandTypeLabel {
    -fx-font-size: 11px;
    -fx-text-fill: #FFFFFF;
}

#commandTextField {
    -fx-background-color: #61594a;
    -fx-background-insets: 0;
    -fx-border-color: #383838 #383838 #ffffff #383838;
    -fx-border-insets: 0 10px 0 10px ;
    -fx-border-width: 0;
    -fx-font-family: "Segoe UI Semibold";
    -fx-font-size: 14pt;
    -fx-text-fill: white;
}

#filterField, #personListPanel, #personWebpage {
    -fx-effect: innershadow(gaussian, black, 10, 0, 0, 0);
}

#resultDisplay .content {
    -fx-background-color: #61594a;
    -fx-background-radius: 3px;
}

#tags {
    -fx-hgap: 7;
    -fx-vgap: 3;
}

#tags .label {
    -fx-text-fill: white;
    -fx-background-color: #3e7b91;
    -fx-padding: 3 5 3 5;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 11;
}

#profile {
    -fx-background: #61594a;
}

```
###### \resources\view\LightTheme.css
``` css
#commandWord {
    -fx-background-color: #61594a;
    -fx-padding: 0 0 0 21px;
    -fx-background-radius: 3px;
    -fx-border-insets: 0 0px 0 0px;
    -fx-border-width: 0 0 0 0;
    -fx-border-color: #d0d0d0;
    -fx-font-size: 14pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: #b8c975;
}

#personListView {
    -fx-background-color: #eeede6;
}

#resultDisplayPlaceholder {
    -fx-effect: dropshadow(three-pass-box, #b2b1ac, 5, 0, 0, 2);
}

#bottomPlaceholder {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}
```
###### \resources\view\LockScreen.fxml
``` fxml
<?import java.net.URL?>
<?import javafx.geometry.Insets?>
<?import javafx.scene.layout.StackPane?>
<?import javafx.scene.layout.VBox?>

<VBox xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1" styleClass="background" fx:id="vBox">
  <stylesheets>
    <URL value="@DarkTheme.css" />
    <URL value="@Extensions.css" />
  </stylesheets>

  <StackPane VBox.vgrow="NEVER" fx:id="pwdBoxPlaceholder" styleClass="pwd-field">
    <padding>
      <Insets top="5" right="10" bottom="5" left="10" />
    </padding>
  </StackPane>

  <StackPane fx:id="statusbarPlaceholder" VBox.vgrow="NEVER" />
</VBox>
```
###### \resources\view\YoutubeTheme.css
``` css
.background {
    -fx-background-color: #FFFFFF;
    background-color: #FFFFFF; /* Used in the default.html file */
}


.label {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: #555555;
    -fx-opacity: 0.9;
}

.label-bright {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.label-header {
    -fx-font-size: 32pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.text-field {
    -fx-font-size: 12pt;
    -fx-font-family: "Segoe UI Semibold";
}

.tab-pane {
    -fx-padding: 0 0 0 1;
}

.tab-pane .tab-header-area {
    -fx-padding: 0 0 0 0;
    -fx-min-height: 0;
    -fx-max-height: 0;
}

.table-view {
    -fx-base: #FFFFFF;
    -fx-control-inner-background: #FFFFFF;
    -fx-background-color: #FFFFFF;
    -fx-table-cell-border-color: transparent;
    -fx-table-header-border-color: transparent;
    -fx-padding: 5;
}

.table-view .column-header-background {
    -fx-background-color: transparent;
}

.table-view .column-header, .table-view .filler {
    -fx-size: 35;
    -fx-border-width: 0 0 1 0;
    -fx-background-color: transparent;
    -fx-border-color:
        transparent
        transparent
        derive(-fx-base, 80%)
        transparent;
    -fx-border-insets: 0 10 1 0;
}

.table-view .column-header .label {
    -fx-font-size: 20pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: #666666;
    -fx-alignment: center-left;
    -fx-opacity: 1;
}

.table-view:focused .table-row-cell:filled:focused:selected {
    -fx-background-color: -fx-focus-color;
}

.split-pane:horizontal .split-pane-divider {
    -fx-background-color: #FFFFFF;
}

.split-pane {
    -fx-border-radius: 1;
    -fx-border-width: 0;
    -fx-background-color: #FFFFFF;
}

.list-view {
    -fx-background-color: #FFFFFF;
    -fx-background-insets: 0;
    -fx-padding: 0;
}

.cell_big_label {
    -fx-font-family: "Segoe UI Semibold";
    -fx-font-size: 20px;
    -fx-text-fill: #656f77;
}

.cell_small_label {
    -fx-font-family: "Segoe UI";
    -fx-font-size: 14px;
    -fx-text-fill: #656f77;
}

.list-cell {
    -fx-label-padding: 0 0 0 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 0 0;
    -fx-border-color: #c3c3c3 transparent transparent transparent;
    -fx-border-top-width: 1px;
    -fx-background-color: #FFFFFF;
}

.list-cell .label {
    -fx-text-fill: #595a5b;
}

.list-cell:filled:even {
    -fx-background-color: #FFFFFF;
}

.list-cell:filled:odd {
    -fx-background-color: #FFFFFF;
}

.list-cell:filled:selected {
    -fx-background-color: #b8c975;
}

.list-cell:filled:selected #cardPane {
    -fx-border-color: #3e7b91;
    -fx-border-width: 0;
}

.list-cell:filled:selected .cell_big_label {
    -fx-text-fill: #FFFFFF;
}

.list-cell:empty {
    -fx-border-width: 0px;
}

.anchor-pane {
     -fx-background-color: #FFFFFF;
}

.pane-with-border {
     -fx-background-color: #FFFFFF;
     -fx-border-color: #FFFFFF;
     -fx-border-top-width: 1px;
}

.pane-transparent {
     -fx-background-color: transparent;
}

.pwd-field {
    -fx-background-color: #FFFFFF;
    -fx-padding: 20px;
}

.status-bar {
    -fx-background-color: #FFFFFF;
    -fx-text-fill: black;
}

.result-display {
    -fx-background-color: #FFFFFF;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 12pt;
    -fx-text-fill: #595a5b;
    -fx-border-color: #FFFFFF;
    -fx-border-width: 0 0 1px 0;
    -fx-border-color: #dadada;
    -fx-padding: 0 10 0 10;
    -fx-background-radius: 0px;
}

.result-display .label {
    -fx-text-fill: black !important;
}

.status-bar .label {
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: #2e2e2e;
}

.status-bar-with-border {
    -fx-background-color: #FFFFFF;
    -fx-border-color: #deded8;
    -fx-border-width: 0px;
}

.status-bar-with-border .label {
    -fx-text-fill: #61594a;
}

.grid-pane {
    -fx-background-color: #FFFFFF;
    -fx-border-color: #FFFFFF;
    -fx-border-width: 0px;
}

.grid-pane .anchor-pane {
    -fx-background-color: #FFFFFF;
}

.context-menu {
    -fx-background-color: #6c6454;
}

.context-menu .label {
    -fx-text-fill: #2e2e2e;
}

.menu-bar {
    -fx-background-color: #FFFFFF;
}

.menu-bar .label {
    -fx-font-size: 14pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: #333333;
    -fx-opacity: 0.9;
}

.menu .left-container {
    -fx-background-color: black;
}

/*
 * Metro style Push Button
 * Author: Pedro Duque Vieira
 * http://pixelduke.wordpress.com/2012/10/23/jmetro-windows-8-controls-on-java/
 */
.button {
    -fx-padding: 5 22 5 22;
    -fx-border-color: #e2e2e2;
    -fx-border-width: 2;
    -fx-background-radius: 0;
    -fx-background-color: #1d1d1d;
    -fx-font-family: "Segoe UI", Helvetica, Arial, sans-serif;
    -fx-font-size: 11pt;
    -fx-text-fill: #d8d8d8;
    -fx-background-insets: 0 0 0 0, 0, 1, 2;
}

.button:hover {
    -fx-background-color: #b8c975;
}

.button:pressed, .button:default:hover:pressed {
  -fx-background-color: white;
  -fx-text-fill: #1d1d1d;
}

.button:focused {
    -fx-border-color: white, white;
    -fx-border-width: 1, 1;
    -fx-border-style: solid, segments(1, 1);
    -fx-border-radius: 0, 0;
    -fx-border-insets: 1 1 1 1, 0;
}

.button:disabled, .button:default:disabled {
    -fx-opacity: 0.4;
    -fx-background-color: #1d1d1d;
    -fx-text-fill: white;
}

.button:default {
    -fx-background-color: -fx-focus-color;
    -fx-text-fill: #ffffff;
}

.button:default:hover {
    -fx-background-color: derive(-fx-focus-color, 30%);
}

.dialog-pane {
    -fx-background-color: #FFFFFF;
}

.dialog-pane > *.button-bar > *.container {
    -fx-background-color: #FFFFFF;
}

.dialog-pane > *.label.content {
    -fx-font-size: 14px;
    -fx-font-weight: bold;
    -fx-text-fill: #FFFFFF;;
}

.dialog-pane:header *.header-panel {
    -fx-background-color: #FFFFFF;
}

.dialog-pane:header *.header-panel *.label {
    -fx-font-size: 18px;
    -fx-font-style: italic;
    -fx-fill: #FFFFFF;;
    -fx-text-fill: #FFFFFF;;
}

.scroll-bar {
    -fx-background-color: #FFFFFF;
}

.scroll-bar .thumb {
    -fx-background-color: #FFFFFF;
    -fx-background-insets: 3;
}

.scroll-bar .increment-button, .scroll-bar .decrement-button {
    -fx-background-color: transparent;
    -fx-padding: 0 0 0 0;
}

.scroll-bar .increment-arrow, .scroll-bar .decrement-arrow {
    -fx-shape: " ";
}

.scroll-bar:vertical .increment-arrow, .scroll-bar:vertical .decrement-arrow {
    -fx-padding: 1 8 1 8;
}

.scroll-bar:horizontal .increment-arrow, .scroll-bar:horizontal .decrement-arrow {
    -fx-padding: 8 1  8 1;
}

#cardPane {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}

#commandTypeLabel {
    -fx-font-size: 11px;
    -fx-text-fill: #FFFFFF;
}

#commandBoxPlaceholder {
    -fx-border-width: 0 0 1 0;
    -fx-border-color: #e8e5ea;
}

#commandTextField {
    -fx-background-color: #FFFFFF;
    -fx-background-insets: 0;
    -fx-border-color: #d0d0d0;
    -fx-border-radius: 3px;
    -fx-border-insets: 0 10px 0 10px ;
    -fx-border-width: 0 0 1px 0;
    -fx-font-family: "Segoe UI Semibold";
    -fx-font-size: 14pt;
    -fx-text-fill: #2e2e2e;
}

#filterField, #personListPanel, #personWebpage {
    -fx-effect: innershadow(gaussian, black, 10, 0, 0, 0);
}

#resultDisplay .content {
    -fx-background-color: #FFFFFF;
    -fx-background-radius: 3px;
}

#tags {
    -fx-hgap: 7;
    -fx-vgap: 3;
}

#tags .label {
    -fx-text-fill: white;
    -fx-background-color: #3e7b91;
    -fx-padding: 3 5 3 5;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 11;
}

```
###### \resources\view\YoutubeTheme.css
``` css
#commandWord {
    -fx-background-color: #FFFFFF;
    -fx-padding: 0 0 2px 21px;
    -fx-background-radius: 3px;
    -fx-background-insets: 2px 0px 2px 0px;
    -fx-border-width: 0 0 0 0;
    -fx-border-color: #d0d0d0;
    -fx-font-size: 14pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: #4f7df6;
}

#resultDisplayPlaceholder {
    -fx-effect: dropshadow(three-pass-box, #dadada, 5, 0, 0, 2);
}

#personListView {
    -fx-background-color: #FFFFFF;
}

#bottomPlaceholder {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}
```
