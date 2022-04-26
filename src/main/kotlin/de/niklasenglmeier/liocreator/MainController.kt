package de.niklasenglmeier.liocreator

import de.niklasenglmeier.liocreator.models.Track
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Stage
import java.io.File


class MainController {
    lateinit var stage: Stage

    @FXML
    lateinit var listViewTest: ListView<Track>

    @FXML
    private val masterBox: VBox? = null

    @FXML
    lateinit var testLabel: Label

    @FXML
    lateinit var testButton : Button

    @FXML
    fun onToolbarExportButtonClicked(event: ActionEvent) {
        /*val secondStage = Stage()
        val fxmlLoader = FXMLLoader(javaClass.getResource("export-view.fxml"))

        secondStage.scene = Scene(fxmlLoader.load(), 1280.0, 720.0)
        secondStage.show()*/

        val secondLabel = Label("I'm a Label on new Window")

        val secondaryLayout = StackPane()
        secondaryLayout.children.add(secondLabel)

        val secondScene = Scene(secondaryLayout, 230.0, 100.0)

        // New window (Stage)

        // New window (Stage)
        val newWindow = Stage()
        newWindow.title = "Second Stage"
        newWindow.scene = secondScene

        newWindow.isResizable = false
        newWindow.initModality(Modality.APPLICATION_MODAL)

        // Set position of second window, related to primary window.

        newWindow.show()
    }

    @FXML
    fun onButtonClicked(event: ActionEvent) {
        testLabel.text = "You clicked the button! Congratulations"
    }

    @FXML
    fun onToolbarOpenProjectClicked(event: ActionEvent) {
        getFileFromFileChooserDialog("Open Project File")
    }

    @FXML
    fun onToolbarOpenMIDIClicked(event: ActionEvent) {
        getFileFromFileChooserDialog("Open MIDI File")
    }

    private fun getFileFromFileChooserDialog(title: String): File? {
        val fileChooser = FileChooser()
        fileChooser.title = title
        return fileChooser.showOpenDialog(stage)
    }

    fun onStageLoaded() {
        val items = FXCollections.observableArrayList(
            Track(0, "Vocals"),
            Track(1, "Guitar"),
            Track(2, "Synth"),
            Track(3, "Piano"),
            Track(4, "Bass"),
            Track(5, "Drums"),
            Track(6, "Violin"),
        )
        listViewTest.setCellFactory { TrackListViewCell() }
        listViewTest.items = items
    }
}