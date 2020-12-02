package opencv

object ImageProcessor {
    operator fun invoke(sourceFileName: String, targetFileName: String) {
//        val notes = heads.mapNotNull { head ->
//            val element = elements.find { head.center in it.rect } ?: return@mapNotNull null
//            val note = Note(element.rect, head.center)
//            val array = vertical[head.center.x.toInt(), head.center.y.toInt()] ?: return@mapNotNull note
//
//            val full = array[0] == 255.0 // the matrix is negated
//            val tail = element.rect.width / head.rect.width > 1.25
//            val bar = element.rect.height / head.rect.height > 1.5
//
//            note.duration = when {
//                !bar -> Note.Duration.WHOLE
//                !full -> Note.Duration.HALF
//                !tail -> Note.Duration.QUARTER
//                else -> Note.Duration.EIGHT
//            }
//            return@mapNotNull note
//        }
//
//        val clefs = elements.minus(notes).map { Clef(it.rect) }
//
//        clefs.forEach { clef ->
//            val stave = staves.find { clef in it } ?: return@forEach
//            stave.assign(clef)
//        }
//
//        notes.forEach { note ->
//            val stave = staves.find { note in it } ?: return@forEach
//            stave.assign(note)
//        }
    }
}
