package de.niklasenglmeier.liocreator.models

data class MIDIHeader(val headerLength: Int, val format: Short, val trackCount: Short, val unitsPerBeat: Short)