enum class Template {
    CLEF_F,
    CLEF_G,
    NOTE_1,
    NOTE_2,
    NOTE_4,
    NOTE_8,
    NOTE_HEAD;

    fun fileName() = name.toLowerCase().replace('_', '-').plus(".png")
}