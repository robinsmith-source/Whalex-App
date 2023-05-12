package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controller.SceneManager;

public class WhalexApp extends Application {

	private static final Logger log = LogManager.getLogger(WhalexApp.class);

    /**
     * The main stage of the application.
     */
    private static Stage applicationStage;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Method to get the main stage of the application into other scene controllers.
     * @return the main stage of the application
     */
    public static Stage getApplicationStage() {
        return applicationStage;
    }

    /**
     * Method to start the application.
     * @param stage the main stage of the application
     * @throws Exception if the application can't be started
     */
    public void start(Stage stage) throws Exception {
        applicationStage = stage;


        //Start Application with Login Scene
        SceneManager.LOGIN.changeScene();

        log.info("Starting Whalex Application");

        stage.show();
        log.debug("Showing {}.", stage.getTitle());
    }
}
