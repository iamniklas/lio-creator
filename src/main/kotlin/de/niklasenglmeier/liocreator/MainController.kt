package de.niklasenglmeier.liocreator

import de.niklasenglmeier.liocreator.models.Track
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
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
}