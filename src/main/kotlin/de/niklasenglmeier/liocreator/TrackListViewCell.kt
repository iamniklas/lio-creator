package de.niklasenglmeier.liocreator

import de.niklasenglmeier.liocreator.models.Track
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.layout.GridPane
import java.io.IOException

class TrackListViewCell : ListCell<Track>() {
    @FXML
    private lateinit var labelId: Label

    @FXML
    private lateinit var labelName: Label

    @FXML
    private lateinit var gridPane: GridPane

    private var mLLoader: FXMLLoader? = null

    init {
        prefWidth = 0.0
    }

    override fun updateItem(student: Track?, empty: Boolean) {
        super.updateItem(student, empty)

        if (empty || student == null) {
            text = null
            graphic = null
        } else {
            if (mLLoader == null) {
                mLLoader = FXMLLoader(TrackListViewCell::class.java.getResource("track-editor-listview-cell.fxml"))
                mLLoader?.setController(this)
                try {
                    mLLoader!!.load()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            labelId.text = "Track ${(student.id + 1)}"
            labelName.text = student.name

            text = null
            graphic = gridPane
        }
    }
}