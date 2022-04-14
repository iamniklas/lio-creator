package de.niklasenglmeier.liocreator

import de.niklasenglmeier.liocreator.models.Track
import javafx.application.Application
import javafx.collections.FXCollections
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.ListView
import javafx.stage.Stage

class MainApplication : Application() {
    lateinit var controller: MainController

    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(MainApplication::class.java.getResource("main-view.fxml"))

        val scene = Scene(fxmlLoader.load(), 1280.0, 720.0)
        controller = fxmlLoader.getController()
        stage.title = "LIO Creator"
        stage.scene = scene
        stage.isResizable = false

        controller.stage = stage
        val list: ListView<String> = ListView<String>()
        val items = FXCollections.observableArrayList(
            Track(0, "Vocals"),
            Track(1, "Guitar"),
            Track(2, "Synth"),
            Track(3, "Piano"),
            Track(4, "Bass"),
            Track(5, "Drums"),
            Track(6, "Violin"),
        )
        controller.listViewTest.setCellFactory { TrackListViewCell() }
        controller.listViewTest.items = items

        stage.show()
    }
}

fun main() {
    Application.launch(MainApplication::class.java)
}