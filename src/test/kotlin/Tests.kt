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
import javax.sound.midi.*
import javax.sound.midi.ShortMessage.*


//Reference: https://ccrma.stanford.edu/~craig/14q/midifile/MidiFileFormat.html
class Tests {
    lateinit var midiFile: MIDIFile
    lateinit var midiHeader: MIDIHeader

    @Test
    fun loadMidiFile() {
        //region #Field Initializations
        val resourceName = "miditestfile.mid"
        val targetFile = javaClass.getResource(resourceName)
        val file = File(targetFile?.file.toString())
        if (!file.isFile) {
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

    @Test
    fun yetAnotherMidiTest() {
        val sequence = MidiSystem.getSequence(File(javaClass.getResource("miditestfile.mid")?.toURI()));
        val NOTE_NAMES = arrayOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")

        val synthesizer = MidiSystem.getSynthesizer()
        var instruments: Array<Instrument> = arrayOf()
        val sb: Soundbank = synthesizer.defaultSoundbank
        if (sb != null) instruments = synthesizer.defaultSoundbank.instruments

        var trackNumber = 0
        for (track in sequence.tracks) {

            trackNumber++
            println("Track " + trackNumber + ": size = " + track.size())
            println()
            for (i in 0 until track.size()) {
                val event = track.get(i)

                //print("@" + event.tick + " ");
                val message = event.message

                if (message is ShortMessage) {
                    //print("Channel: " + message.channel + " ");

                    when (message.command) {
                        NOTE_ON -> {
                            val key = message.data1
                            val octave = (key / 12) - 1
                            val note = key % 12
                            val noteName = NOTE_NAMES[note]
                            val velocity = message.data2
                            //println("Note on, $noteName$octave key=$key velocity: $velocity");
                        }

                        NOTE_OFF -> {
                            val key = message.data1
                            val octave = (key / 12) - 1
                            val note = key % 12
                            val noteName = NOTE_NAMES[note]
                            val velocity = message.data2
                            //println("Note off, $noteName$octave key=$key velocity: $velocity");
                        }

                        CONTROL_CHANGE -> {
                            val key = message.data1
                            val octave = (key / 12) - 1
                            val note = key % 12
                            val noteName = NOTE_NAMES[note]
                            val velocity = message.data2
                            //println("Control changed, $noteName$octave key=$key velocity: $velocity");
                        }

                        PROGRAM_CHANGE -> {
                            println("Channel: ${message.channel} ${instruments[message.data1]}")
                        }

                        PITCH_BEND -> {

                        }

                        else -> {
                            println("Command:" + message.command)
                        }
                    }
                }
                else if(message is MetaMessage) {
                    print("Meta Event: ")
                    when(message.type) {
                        0x00 -> { println("Sequence Number") }
                        0x01 -> { println("Text Event") }
                        0x02 -> { println("Copyright Notice") }
                        0x03 -> { println("Sequence or Track Name") }
                        0x04 -> { println("Instrument Name") }
                        0x05 -> { println("Lyric Text") }
                        0x06 -> { println("Marker Text") }
                        0x07 -> { println("Cue Point") }
                        0x20 -> { println("MIDI Channel Prefix Assignment") }
                        0x2F -> { println("End of Track") }
                        0x51 -> { println("Tempo Setting") }
                        0x54 -> { println("SMPTE Offset") }
                        0x58 -> { println("Time Signature") }
                        0x59 -> { println("Key Signature") }
                        0x7F -> { println("Sequencer Specific Event") }
                    }
                }
                else {
                    println("Other message: " + message.javaClass)
                }
            }

            println()
        }
    }
}