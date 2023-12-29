package puzzles.tipover.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.tipover.model.TipOverConfig;
import puzzles.tipover.model.TipOverModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import java.io.File;

/**
 * Class that creates the GUi for the game, Tip Over; Acts as the
 * view and controller for the MVC pattern
 *
 * @author Ricky Leung
 */
public class TipOverGUI extends Application implements Observer<TipOverModel, String> {
    /** Model of the game */
    private TipOverModel model;
    /** Message that is displayed to the user */
    private Label message;
    /** Gridpane used for storing all the labels to represent crate heights */
    private GridPane crates;
    /** BorderPane used as the scene for the stage */
    private BorderPane fullGUI;
    /** Stored reference to the stage */
    private Stage stage = new Stage();
    /** File name of the current board that is loaded */
    private String originalLoad;
    /** Universal font name */
    private static String fontName = "Ariel";
    /** Universal size */
    private static int SIZE = 30;

    /**
     * Creates the model and adds the GUI as an observer
     */
    public void init() {
        originalLoad = getParameters().getRaw().get(0);
        this.model = new TipOverModel();
        this.model.loadBoardFromFile(originalLoad);
        model.addObserver(this);
    }

    /**
     * Initial initialization of the GUI:
     *      - Borderpane as the scene:
     *              - Label for displayed message at top
     *              - Gridpane for crates/towers at center
     *              - VBox at right:
     *                  - Gridpane for move buttons
     *                  - Buttons for Load, Reset, and Hint
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.message = new Label("");
        this.message.setFont(new Font(fontName, this.SIZE / 2));
        this.message.setAlignment(Pos.CENTER_RIGHT);

        this.crates = new GridPane();
        this.crates.setStyle("-fx-border-color: black");
        this.crates.setHgap(10);
        this.crates.setVgap(10);
        for (int row = 0; row < model.getConfig().getRows(); row++) {
            for (int col = 0; col < model.getConfig().getCols(); col++) {
                Label val = new Label();
                val.setFont(new Font(fontName, this.SIZE));
                crates.add(val, col, row);
            }
        }

        VBox rightPanel = new VBox();
        rightPanel.setAlignment(Pos.TOP_LEFT);
        GridPane arrows = new GridPane();
        Button up = new Button("↑");
        up.setOnAction(event -> {
            model.move("m N");
        });
        arrows.add(up, 1, 0);
        Button right = new Button("→");
        right.setOnAction(event -> {
            model.move("m E");
        });
        arrows.add(right, 2, 1);
        Button down = new Button("↓");
        down.setOnAction(event -> {
            model.move("m S");
        });
        arrows.add(down, 1, 2);
        Button left = new Button("←");
        left.setOnAction(event -> {
            model.move("m W");
        });
        arrows.add(left, 0, 1);
        arrows.setAlignment(Pos.CENTER);

        Button load = new Button("Load");
        load.setMinSize(SIZE * 3, SIZE);
        load.setOnAction(event -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Load a game board.");
                fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/data/tipover"));
                fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Text Files", "*.txt"));
                File selectedFile = fileChooser.showOpenDialog(stage);
                model.loadBoardFromFile(selectedFile.getName());
            } catch (NullPointerException e) {

            }
        });
        Button reset = new Button("Reset");
        reset.setMinSize(SIZE * 3, SIZE);
        reset.setOnAction(event -> {
            model.resetBoard();
        });
        Button hint = new Button("Hint");
        hint.setMinSize(SIZE * 3, SIZE);
        hint.setOnAction(event -> {
            model.getHint();
        });
        rightPanel.getChildren().addAll(arrows, load, reset, hint);


        fullGUI = new BorderPane();
        fullGUI.setTop(this.message);
        fullGUI.setCenter(this.crates);
        fullGUI.setRight(rightPanel);
        stage.setScene(new Scene(fullGUI));
        stage.setTitle("Tip Over");
        this.model.loadBoardFromFile(originalLoad);
        this.stage = stage;
        stage.show();
    }

    /**
     * Method used for updating the gridpane of crates/towers based on the board
     * once a button is pressed
     */
    public void displayBoard() {
        TipOverConfig currentConfig = this.model.getConfig();
        int[][] currentBoard = currentConfig.getBoard();
        Coordinates goal = currentConfig.getGOAL();
        Coordinates currentLocation = currentConfig.getCurrentLocation();
        for (int row = 0; row < currentConfig.getRows(); row++) {
            for (int col = 0; col < currentConfig.getCols(); col++) {
                Label val = (Label) crates.getChildren().get(row * currentConfig.getCols() + col);
                if (goal.row() == row && goal.col() == col) {
                    val.setStyle("-fx-background-color: red");
                } else if (currentLocation.row() == row && currentLocation.col() == col) {
                    val.setStyle("-fx-background-color: #ea9797");
                } else {
                    val.setStyle("-fx-background-color: white");
                }
                val.setText(Integer.toString(currentBoard[row][col]));

            }
        }
    }

    /**
     * Updates the message label and crates gridpane based on the button
     * that was pressed by the user
     *
     * @param tipOverModel the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     */
    @Override
    public void update(TipOverModel tipOverModel, String message) {
        if (message.startsWith("Loaded")) {
            this.crates = new GridPane();
            this.crates.setStyle("-fx-border-color: black");
            this.crates.setHgap(10);
            this.crates.setVgap(10);
            for (int row = 0; row < model.getConfig().getRows(); row++) {
                for (int col = 0; col < model.getConfig().getCols(); col++) {
                    Label val = new Label();
                    val.setFont(new Font(fontName, this.SIZE));
                    crates.add(val, col, row);
                }
            }
            fullGUI.setCenter(this.crates);
            stage.setWidth(model.getConfig().getCols() * 28 + 100);

            stage.setHeight(Math.max(225, model.getConfig().getRows() * 55));
        }
        this.message.setText(message);
        displayBoard();
    }

    /**
     * Main function for launching the GUI
     *
     * @param args filename
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOverGUI filename");
            System.exit(0);
        } else {
            Application.launch(args);
        }
    }
}
