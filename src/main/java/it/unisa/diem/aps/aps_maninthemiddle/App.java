package it.unisa.diem.aps.aps_maninthemiddle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import static it.unisa.diem.aps.aps_maninthemiddle.EnumerationState.State.ENABLED;

/**
 * JavaFX App
 */
public class App extends Application implements Runnable{
    //ciao

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        //launch();
        Elettore e1Object= new Elettore(01,ENABLED,0);
        Elettore e2Object= new Elettore(02,ENABLED,0);
        Scheda s1 = new Scheda();
        
        Thread e1 = new Thread(() -> {
            
        
        
        });
        //Thread e2 = new Thread(e2Object);
        
        e1.start();
        //e2.start();
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}