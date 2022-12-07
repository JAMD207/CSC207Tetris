package views;

import model.TetrisModel;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.TetrisPiece;
import model.TetrisPoint;

import java.util.Arrays;


/**
 * Tetris View
 *
 * Based on the Tetris assignment in the Nifty Assignments Database, authored by Nick Parlante
 */
public class TetrisView {

    TetrisModel model; //reference to model
    Stage stage;
    boolean slow;
    Button startButton, stopButton, loadButton, saveButton, newButton, slowButton; //buttons for functions
    Label scoreLabel = new Label("");
    Label gameModeLabel = new Label("");

    BorderPane borderPane;
    Canvas canvas;
    GraphicsContext gc; //the graphics context will be linked to the canvas

    Boolean paused;
    Timeline timeline;

    Color main; //main colour
    Color pieces; //tetris pieces colour
    Color ghost; //ghost piece colour
    String text; // text colour
    String buttons; //button colour
    String sliderr; //slider colour
    Button ProtanopiaButton, DeuteranopiaButton, TritanopiaButton, Normalbutton;

    int pieceWidth = 20; //width of block on display
    private double width; //height and width of canvas
    private double height;

    /**
     * Constructor
     *
     * @param model reference to tetris model
     * @param stage application stage
     */

    public TetrisView(TetrisModel model, Stage stage) {
        this.model = model;
        this.stage = stage;
        Colour();
        initUI();
    }


    /**
     * Constructor
     * for colour
     */
    public void Colour() {
        // normal palette
        //initializer for colour
        this.main = Color.GREEN;
        this.pieces = Color.RED;
        this.ghost = Color.PURPLE;
        this.text = "-fx-text-fill: #e8e6e3";
        this.buttons = "-fx-background-color: #17871b; -fx-text-fill: white;";
        this.sliderr = "-fx-control-inner-background: palegreen;";
    }

    /**
     * Initialize interface
     */
    private void initUI() {
        this.paused = false;
        this.stage.setTitle("CSC207 Tetris");
        this.width = this.model.getWidth()*pieceWidth + 2;
        this.height = this.model.getHeight()*pieceWidth + 2;

        borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #121212;");

        //add canvas
        canvas = new Canvas(this.width, this.height);
        canvas.setId("Canvas");
        gc = canvas.getGraphicsContext2D();

        //labels
        gameModeLabel.setId("GameModeLabel");
        scoreLabel.setId("ScoreLabel");

        gameModeLabel.setText("Player is: Human");
        gameModeLabel.setMinWidth(250);
        gameModeLabel.setFont(new Font(20));
        gameModeLabel.setStyle(text);

        final ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton pilotButtonHuman = new RadioButton("Human");
        pilotButtonHuman.setToggleGroup(toggleGroup);
        pilotButtonHuman.setSelected(true);
        pilotButtonHuman.setUserData(Color.SALMON);
        pilotButtonHuman.setFont(new Font(16));
        pilotButtonHuman.setStyle(text);

        RadioButton pilotButtonComputer = new RadioButton("Computer (Default)");
        pilotButtonComputer.setToggleGroup(toggleGroup);
        pilotButtonComputer.setUserData(Color.SALMON);
        pilotButtonComputer.setFont(new Font(16));
        pilotButtonComputer.setStyle(text);

        scoreLabel.setText("Score is: 0");
        scoreLabel.setFont(new Font(20));
        scoreLabel.setStyle(text);

        //add buttons
        Normalbutton = new Button("Normal");
        Normalbutton.setId("Normal");
        Normalbutton.setPrefSize(150, 50);
        Normalbutton.setFont(new Font(12));
        Normalbutton.setStyle("-fx-background-color: white; -fx-text-fill: black;");

        TritanopiaButton = new Button("Tritanopia");
        TritanopiaButton.setId("Tritanopia");
        TritanopiaButton.setPrefSize(150, 50);
        TritanopiaButton.setFont(new Font(12));
        TritanopiaButton.setStyle(buttons);

        DeuteranopiaButton = new Button("Deuteranopia");
        DeuteranopiaButton.setId("Deuteranopia");
        DeuteranopiaButton.setPrefSize(150, 50);
        DeuteranopiaButton.setFont(new Font(12));
        DeuteranopiaButton.setStyle(buttons);

        ProtanopiaButton = new Button("Protanopia");
        ProtanopiaButton.setId("Protanopia");
        ProtanopiaButton.setPrefSize(150, 50);
        ProtanopiaButton.setFont(new Font(12));
        ProtanopiaButton.setStyle(buttons);

        startButton = new Button("Start");
        startButton.setId("Start");
        startButton.setPrefSize(150, 50);
        startButton.setFont(new Font(12));
        startButton.setStyle(buttons);

        stopButton = new Button("Stop");
        stopButton.setId("Start");
        stopButton.setPrefSize(150, 50);
        stopButton.setFont(new Font(12));
        stopButton.setStyle(buttons);

        saveButton = new Button("Save");
        saveButton.setId("Save");
        saveButton.setPrefSize(150, 50);
        saveButton.setFont(new Font(12));
        saveButton.setStyle(buttons);

        loadButton = new Button("Load");
        loadButton.setId("Load");
        loadButton.setPrefSize(150, 50);
        loadButton.setFont(new Font(12));
        loadButton.setStyle(buttons);

        newButton = new Button("New Game");
        newButton.setId("New");
        newButton.setPrefSize(150, 50);
        newButton.setFont(new Font(12));
        newButton.setStyle(buttons);

        slowButton = new Button("Slow");
        slowButton.setId("Slow");
        slowButton.setPrefSize(150, 50);
        slowButton.setFont(new Font(12));
        slowButton.setStyle(buttons);

        HBox controls = new HBox(20, saveButton, loadButton, newButton, startButton, stopButton, slowButton);
        controls.setPadding(new Insets(20, 20, 20, 20));
        controls.setAlignment(Pos.CENTER);

        Slider slider = new Slider(0, 100, 50);
        slider.setShowTickLabels(true);
        slider.setStyle(sliderr);

        VBox vBox = new VBox(20, slider);
        vBox.setPadding(new Insets(20, 20, 20, 20));
        vBox.setAlignment(Pos.TOP_CENTER);

        VBox scoreBox = new VBox(20, scoreLabel, gameModeLabel, pilotButtonHuman, pilotButtonComputer,
                                        ProtanopiaButton,DeuteranopiaButton,TritanopiaButton, Normalbutton);
        scoreBox.setPadding(new Insets(20, 20, 20, 20));
        vBox.setAlignment(Pos.TOP_CENTER);

        toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> swapPilot(newVal));

        //timeline structures the animation, and speed between application "ticks"
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.25), e -> updateBoard()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        slow = false;
        slowButton.setOnAction(e -> {
            if (!(slow)) {
                timeline.setRate(0.2);
                slow = true;
            } else {
                timeline.setRate(1);
                slow = false;
            }
            borderPane.requestFocus();
                });

        //colour options for the game
        Normalbutton.setOnAction(e -> {
            this.main = Color.GREEN;
            this.pieces = Color.RED;
            this.ghost = Color.PURPLE;
            this.text = "-fx-text-fill: #e8e6e3";
            this.buttons = "-fx-background-color: #17871b; -fx-text-fill: white;";
            TritanopiaButton.setStyle(buttons);
            DeuteranopiaButton.setStyle(buttons);
            ProtanopiaButton.setStyle(buttons);
            Normalbutton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            startButton.setStyle(buttons);
            stopButton.setStyle(buttons);
            loadButton.setStyle(buttons);
            saveButton.setStyle(buttons);
            newButton.setStyle(buttons);
            slowButton.setStyle(buttons);
            slider.setStyle("-fx-control-inner-background: palegreen;");
            borderPane.requestFocus();
        });

        TritanopiaButton.setOnAction(e -> {
            this.main = Color.TEAL;
            this.pieces = Color.RED;
            this.ghost = Color.SLATEGREY;
            this.text = "-fx-text-fill: #e8e6e3";
            this.buttons = "-fx-background-color: #a593b6; -fx-text-fill: white;";
            TritanopiaButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            DeuteranopiaButton.setStyle(buttons);
            ProtanopiaButton.setStyle(buttons);
            Normalbutton.setStyle(buttons);
            startButton.setStyle(buttons);
            stopButton.setStyle(buttons);
            loadButton.setStyle(buttons);
            saveButton.setStyle(buttons);
            newButton.setStyle(buttons);
            slowButton.setStyle(buttons);
            slider.setStyle("-fx-control-inner-background: THISTLE;");
            borderPane.requestFocus();
        });

        DeuteranopiaButton.setOnAction(e -> {
            this.main = Color.CORNFLOWERBLUE;
            this.pieces = Color.GOLD;
            this.ghost = Color.GOLDENROD;
            this.text = "-fx-text-fill: #e8e6e3";
            this.buttons = "-fx-background-color: #ffe132; -fx-text-fill: white;";
            this.sliderr = "-fx-control-inner-background: PAPAYAWHIP;";
            TritanopiaButton.setStyle(buttons);
            DeuteranopiaButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            ProtanopiaButton.setStyle(buttons);
            Normalbutton.setStyle(buttons);
            startButton.setStyle(buttons);
            stopButton.setStyle(buttons);
            loadButton.setStyle(buttons);
            saveButton.setStyle(buttons);
            newButton.setStyle(buttons);
            slowButton.setStyle(buttons);
            slider.setStyle(sliderr);
            borderPane.requestFocus();
        });

        ProtanopiaButton.setOnAction(e -> {
            this.main = Color.CYAN;
            this.pieces = Color.HOTPINK;
            this.ghost = Color.SALMON;
            this.text = "-fx-text-fill: #e8e6e3";
            this.buttons = "-fx-background-color: #24f25b; -fx-text-fill: white;";
            this.sliderr = "-fx-control-inner-background: LIGHTSTEELBLUE;";
            TritanopiaButton.setStyle(buttons);
            DeuteranopiaButton.setStyle(buttons);
            ProtanopiaButton.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            Normalbutton.setStyle(buttons);
            startButton.setStyle(buttons);
            stopButton.setStyle(buttons);
            loadButton.setStyle(buttons);
            saveButton.setStyle(buttons);
            newButton.setStyle(buttons);
            slowButton.setStyle(buttons);
            slider.setStyle(sliderr);
            borderPane.requestFocus();
        });


        //configure this such that you start a new game when the user hits the newButton
        //Make sure to return the focus to the borderPane once you're done!
        newButton.setOnAction(e -> {
            this.model.newGame();
            borderPane.requestFocus();
        });

        //configure this such that you restart the game when the user hits the startButton
        //Make sure to return the focus to the borderPane once you're done!
        startButton.setOnAction(e -> {
            this.paused = false;
            borderPane.requestFocus();
        });

        //configure this such that you pause the game when the user hits the stopButton
        //Make sure to return the focus to the borderPane once you're done!
        stopButton.setOnAction(e -> {
            this.paused = true;
            borderPane.requestFocus();
        });

        //configure this such that the save view pops up when the saveButton is pressed.
        //Make sure to return the focus to the borderPane once you're done!
        saveButton.setOnAction(e -> {
            this.createSaveView();
            borderPane.requestFocus();
        });

        //configure this such that the load view pops up when the loadButton is pressed.
        //Make sure to return the focus to the borderPane once you're done!
        loadButton.setOnAction(e -> {
            this.createLoadView();
            borderPane.requestFocus();
        });

        //configure this such that you adjust the speed of the timeline to a value that
        //ranges between 0 and 3 times the default rate per model tick.  Make sure to return the
        //focus to the borderPane once you're done!
        slider.setOnMouseReleased(e -> {
            double val = slider.getValue();
            double sp = (val/100)*3;
            timeline.setRate(sp);
            borderPane.requestFocus();
        });

        //configure this such that you can use controls to rotate and place pieces as you like!!
        //You'll want to respond to tie key presses to these moves:
        // TetrisModel.MoveType.DROP, TetrisModel.MoveType.ROTATE, TetrisModel.MoveType.LEFT
        //and TetrisModel.MoveType.RIGHT
        //make sure that you don't let the human control the board
        //if the autopilot is on, however.
        borderPane.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                if (k.getCode() == KeyCode.W) {
                    model.modelTick(TetrisModel.MoveType.ROTATE);
                }
                if (k.getCode() == KeyCode.D) {
                    model.modelTick((TetrisModel.MoveType.LEFT));
                }
                if (k.getCode() == KeyCode.A) {
                    model.modelTick((TetrisModel.MoveType.RIGHT));
                }
                if (k.getCode() == KeyCode.S) {
                    model.modelTick((TetrisModel.MoveType.DROP));
                }
            }
        });

        borderPane.setTop(controls);
        borderPane.setRight(scoreBox);
        borderPane.setCenter(canvas);
        borderPane.setBottom(vBox);

        var scene = new Scene(borderPane, 800, 800);
        this.stage.setScene(scene);
        this.stage.show();
    }

    /**
     * Get user selection of "autopilot" or human player
     *
     * @param value toggle selector on UI
     */
    private void swapPilot(Toggle value) {
        RadioButton chk = (RadioButton)value.getToggleGroup().getSelectedToggle();
        String strVal = chk.getText();
        if (strVal.equals("Computer (Default)")){
            this.model.setAutoPilotMode();
            gameModeLabel.setText("Player is: Computer (Default)");
        } else if (strVal.equals("Human")) {
            this.model.setHumanPilotMode();
            gameModeLabel.setText("Player is: Human");
        }
        borderPane.requestFocus(); //give the focus back to the pane with the blocks.
    }

    /**
     * Update board (paint pieces and score info)
     */
    private void updateBoard() {
        if (this.paused != true) {
            paintBoard();
            this.model.modelTick(TetrisModel.MoveType.DOWN);
            updateScore();
        }
    }

    /**
     * Update score on UI
     */
    private void updateScore() {
        if (this.paused != true) {
            scoreLabel.setText("Score is: " + model.getScore() + "\nPieces placed:" + model.getCount());
        }
    }

    /**
     * Methods to calibrate sizes of pixels relative to board size
     */
    private final int yPixel(int y) {
        return (int) Math.round(this.height -1 - (y+1)*dY());
    }
    private final int xPixel(int x) {
        return (int) Math.round(this.width -1 - (x+1)*dX());
    }
    private final float dX() {
        return( ((float)(this.width-2)) / this.model.getBoard().getWidth() );
    }
    private final float dY() {
        return( ((float)(this.height-2)) / this.model.getBoard().getHeight() );
    }

    /**
     * find the maximum height in the column below the tetris piece
     */


    /**
     * Draw the board
     */
    public void paintBoard() {

        // Draw a rectangle around the whole screen
        gc.setStroke(main);
        gc.setFill(main);
        gc.fillRect(0, 0, this.width-1, this.height-1);

        // Draw the line separating the top area on the screen
        gc.setStroke(Color.BLACK);
        int spacerY = yPixel(this.model.getBoard().getHeight() - this.model.BUFFERZONE - 1);
        gc.strokeLine(0, spacerY, this.width-1, spacerY);

        // Factor a few things out to help the optimizer
        final int dx = Math.round(dX()-2);
        final int dy = Math.round(dY()-2);
        final int bWidth = this.model.getBoard().getWidth();
        int x, y;



        // Loop through and draw all the blocks; sizes of blocks are calibrated relative to screen size
        for (x=0; x<bWidth; x++) {
            int left = xPixel(x);	// the left pixel
            // draw from 0 up to the col height
            final int yHeight = this.model.getBoard().getHeight();
            for (y=0; y<yHeight; y++) {
                if (this.model.getBoard().getGrid(x, y)) {
                    gc.setFill(pieces);
                    gc.fillRect(left+1, yPixel(y)+1, dx, dy);
                    gc.setFill(main);
                }
            }
        }

    }

    /**
     * Create the view to save a board to a file
     */
    private void createSaveView(){
        SaveView saveView = new SaveView(this);
    }

    /**
     * Create the view to select a board to load
     */
    private void createLoadView(){
        LoadView loadView = new LoadView(this);
    }


}