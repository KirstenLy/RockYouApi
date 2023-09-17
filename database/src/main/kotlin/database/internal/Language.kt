package database.internal

/** All available languages. */
@Deprecated("Это ушло в БД, нужно удалить")
internal enum class Language(val langID: Int) {
    ENGLISH(1),
    RUSSIAN(2),
    SPAIN(3),
    FRENCH(4)
}