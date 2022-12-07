import javafx.application.Application;
import javafx.stage.Stage;
import model.TetrisModel;
import views.TetrisView;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * A Tetris Application, in JavaFX
 * 
 * Based on the Tetris assignment in the Nifty Assignments Database, authored by Nick Parlante
 */
public class TetrisApp extends Application {
    TetrisModel model;
    TetrisView view;


    /** 
     * Main method
     * 
     * @param args agument, if any
     */
    public static void main(String[] args){
        launch(args);

    }
    public static void playMusic() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        InputStream music;
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("Tetris.wav").getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    /** 
     * Start method.  Control of application flow is dictated by JavaFX framework
     * 
     * @param primaryStage stage upon which to load GUI elements
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        playMusic();
        this.model = new TetrisModel(); // create a model
        this.view = new TetrisView(model, primaryStage); //tie the model to the view
        this.model.startGame(); //begin

    }

}

