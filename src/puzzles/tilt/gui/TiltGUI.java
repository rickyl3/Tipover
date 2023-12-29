package puzzles.tilt.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltConfig;
import puzzles.tilt.model.TiltModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.File;

/**
 * The GUI of the Tilt puzzle.
 *
 * @author Boya Li
 */
public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    /** The model.*/
    private TiltModel model;
    /** The label at the top that would inform the user of the messages.*/
    private Label message;
    /** The BorderPane of the message label, side buttons, and inner BorderPane.*/
    private BorderPane outerBorder;
    /** The BorderPane of the grid and direction arrows.*/
    private BorderPane innerBorder;
    /** The GridPane of images of the board.*/
    private GridPane grid;
    /** The stage.*/
    private Stage stage = new Stage();
    /** The current file.*/
    private String file;

    /**
     * When the GUI is initiated, it will acquire the file,
     * set a new TiltModel, load the board from the file,
     * and add this as an observer.
     */
    public void init() {
        file = getParameters().getRaw().get(0);
        this.model = new TiltModel();
        this.model.loadBoardFromFile(file);
        model.addObserver(this);
    }

    /**
     * This would start the GUI, which shows the stage.
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception Checks whether there is an exception showing the stage.
     */
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(System.getProperty("user.dir"));
        this.message = new Label("");
        this.message.setAlignment(Pos.CENTER);

        this.outerBorder = new BorderPane();
        //Top
        outerBorder.setTop(message);

        //Right
        Button load = new Button("Load");
        load.setOnAction(event -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Load a game.");
                fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/data/tilt"));
                fileChooser.getExtensionFilters().
                        addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
                File newFile = fileChooser.showOpenDialog(stage);
                model.loadBoardFromFile(newFile.getName());
            }
            catch (NullPointerException e) { }
        });
        Button reset = new Button("Reset");
        reset.setOnAction(event -> {
            model.resetBoard();
        });
        Button hint = new Button("Hint");
        hint.setOnAction(event -> {
            model.getHint();
        });

        VBox buttons = new VBox();
        buttons.getChildren().addAll(load, reset, hint);
        buttons.setAlignment(Pos.CENTER);
        outerBorder.setRight(buttons);

        //Center
        this.innerBorder = new BorderPane();

        this.grid = new GridPane();
        innerBorder.setCenter(grid);

        Button north = new Button("^");
        north.setOnAction(event -> {
            model.tilt("N");
        });
        north.setMinHeight(30);
        north.setMinWidth(560);
        innerBorder.setTop(north);
        Button east = new Button(">");
        east.setOnAction(event -> {
            model.tilt("E");
        });
        east.setMinHeight(500);
        east.setMinWidth(30);
        innerBorder.setRight(east);
        Button south = new Button("v");
        south.setOnAction(event -> {
            model.tilt("S");
        });
        south.setMinHeight(30);
        south.setMinWidth(560);
        innerBorder.setBottom(south);
        Button west = new Button("<");
        west.setOnAction(event -> {
            model.tilt("W");
        });
        west.setMinHeight(500);
        west.setMinWidth(30);
        innerBorder.setLeft(west);
        outerBorder.setCenter(innerBorder);

        stage.setScene(new Scene(outerBorder));
        stage.setTitle("Tilt");
        this.model.loadBoardFromFile(file);
        this.stage = stage;
        stage.show();
    }

    /**
     * Checks whether a new file has been loaded. Either way it would
     * update the board and would update the message label at the top
     * with the respective message.
     *
     * @param tiltModel the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(TiltModel tiltModel, String message) {
        if(message.length() > 6) {
            if(message.startsWith("Loaded")) {
                this.grid = new GridPane();
                this.grid.setMinHeight(500);
                this.grid.setMaxHeight(500);
                this.grid.setMinHeight(500);
                this.grid.setMaxWidth(500);

                for(int row = 0; row < model.getConfig().getSize(); row++) {
                    for(int col = 0; col < model.getConfig().getSize(); col++) {
                        Image image = new Image("puzzles/tilt/gui/resources/block.png");
                        ColorAdjust white = new ColorAdjust();
                        white.setBrightness(100000);
                        ImageView blank = new ImageView(image);
                        blank.setFitHeight((double) 500 / model.getConfig().getSize());
                        blank.setFitWidth((double) 500 / model.getConfig().getSize());
                        blank.setEffect(white);
                        grid.add(blank, col, row);
                    }
                }
                innerBorder.setCenter(this.grid);
            }
        }
        this.message.setText(message);
        displayBoard();
    }

    /**
     * Checks every slot in the board and updates the GridPane
     * accordingly.
     */
    public void displayBoard() {
        TiltConfig curr = this.model.getConfig();
        String[][] board = curr.getBoardGUI();
        for(int row = 0; row < curr.getSize(); row++) {
            for(int col = 0; col < curr.getSize(); col++) {
                if(board[row][col].equals("*")) {
                    Image pic = new Image("puzzles/tilt/gui/resources/block.png");
                    ImageView image = new ImageView(pic);
                    image.setFitHeight((double) 500 / model.getConfig().getSize());
                    image.setFitWidth((double) 500 / model.getConfig().getSize());
                    grid.add(image, col, row);
                }
                else if(board[row][col].equals("O")) {
                    Image pic = new Image("puzzles/tilt/gui/resources/hole.png");
                    ImageView image = new ImageView(pic);
                    image.setFitHeight((double) 500 / model.getConfig().getSize());
                    image.setFitWidth((double) 500 / model.getConfig().getSize());
                    grid.add(image, col, row);
                }
                else if(board[row][col].equals("B")) {
                    Image pic = new Image("puzzles/tilt/gui/resources/blue.png");
                    ImageView image = new ImageView(pic);
                    image.setFitHeight((double) 500 / model.getConfig().getSize());
                    image.setFitWidth((double) 500 / model.getConfig().getSize());
                    grid.add(image, col, row);
                }
                else if (board[row][col].equals("G")) {
                    Image pic = new Image("puzzles/tilt/gui/resources/green.png");
                    ImageView image = new ImageView(pic);
                    image.setFitHeight((double) 500 / model.getConfig().getSize());
                    image.setFitWidth((double) 500 / model.getConfig().getSize());
                    grid.add(image, col, row);
                }
                else {
                    Image image = new Image("puzzles/tilt/gui/resources/block.png");
                    ColorAdjust white = new ColorAdjust();
                    white.setBrightness(100000);
                    ImageView blank = new ImageView(image);
                    blank.setFitHeight((double) 500 / model.getConfig().getSize());
                    blank.setFitWidth((double) 500 / model.getConfig().getSize());
                    blank.setEffect(white);
                    grid.add(blank, col, row);
                }
            }
        }
    }

    /**
     * This is the main method. It would launch the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage: java TiltGUI filename");
            System.exit(0);
        }
        else {
            Application.launch(args);
        }
    }
}
