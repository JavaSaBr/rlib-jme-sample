package com.ss.client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * The started of the client.
 *
 * @author JavaSaBr
 */
public class Starter extends Application {

    public static void main(@NotNull final String[] args) throws IOException {
        SampleGame.start(args);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
    }
}
