import de.niklasenglmeier.liocreator.models.MIDIFile
import de.niklasenglmeier.liocreator.models.MIDIHeader
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.math.BigInteger
import java.nio.charset.StandardCharsets

class Tests {
    lateinit var midiFile: MIDIFile
    lateinit var midiHeader: MIDIHeader

    @Test
    fun loadMidiFile() {
        //region #Field Initializations
        val resourceName = "miditestfile.mid"
        val targetFile = javaClass.getResource(resourceName)
        val file = File(targetFile?.file.toString())
        if(!file.isFile) {
            throw FileNotFoundException("No file $resourceName found in test-resources")
        }
        val fileInputStream = FileInputStream(file)
        val dataInputStream = DataInputStream(fileInputStream)
        //endregion

        //region #File Read Information
        println("Reading file $resourceName")
        println()
        println()
        //endregion

        //region #Header
        println("HEADER")

        //MThd
        Assertions.assertEquals("MThd", String(dataInputStream.readNBytes(4), StandardCharsets.UTF_8))
        println("File is in MIDI format")

        //Header_Length
        val expectedHeaderChunkSize = 6
        val actualHeaderChunkSize = BigInteger(1, dataInputStream.readNBytes(4)).toInt()
        Assertions.assertEquals(expectedHeaderChunkSize, actualHeaderChunkSize)
        println("Header Chunk Size is $expectedHeaderChunkSize bytes")

        //Format
        val format = BigInteger(1, dataInputStream.readNBytes(2)).toShort()
        assertThat(format, anyOf(`is`(0), `is`(1), `is`(2)))
        when (format) {
            (0).toShort() -> println("File uses Single Track File Format")
            (1).toShort() -> println("File uses Multiple Track File Format")
            (2).toShort() -> println("File uses Multiple Song File Format")
        }

        //N
        val trackCount = BigInteger(1, dataInputStream.readNBytes(2)).toShort()
        println("File has $trackCount tracks")

        //Division
        val division = BigInteger(1, dataInputStream.readNBytes(2)).toShort()
        println("Ticks/Beat: $division")

        midiHeader = MIDIHeader(actualHeaderChunkSize, format, trackCount, division)

        println()
        println()
        //endregion

        //region #Track
        println("TRACK")
        //MTrk
        Assertions.assertEquals("MTrk", String(dataInputStream.readNBytes(4), StandardCharsets.UTF_8))
        println("MTrk: Begin of track")

        val trackLength = BigInteger(1, dataInputStream.readNBytes(4)).toInt()
        println("Track is $trackLength bytes long")
        //endregion

        midiFile = MIDIFile(midiHeader)
    }
}