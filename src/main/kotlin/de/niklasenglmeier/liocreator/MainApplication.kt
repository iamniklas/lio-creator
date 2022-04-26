package de.niklasenglmeier.liocreator

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class MainApplication : Application() {
    private lateinit var controller: MainController

    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(MainApplication::class.java.getResource("main-view.fxml"))

        val scene = Scene(fxmlLoader.load(), 1280.0, 720.0)
        controller = fxmlLoader.getController()
        stage.title = "LIO Creator"
        stage.scene = scene
        stage.isResizable = false

        controller.stage = stage

        stage.show()
        controller.onStageLoaded()


    }
}

fun main() {
    Application.launch(MainApplication::class.java)
}