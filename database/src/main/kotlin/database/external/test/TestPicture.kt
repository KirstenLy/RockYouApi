package database.external.test

/** // TODO: Написать, что модели отличаются, т.к так проще организовать связи и вставка
 * Picture model for test database.
 * @see database.external.TestDatabaseBuilder
 * */
data class TestPicture(
    val id: Int,
    val title: String,
    val url: String,
    val languageID: Byte?,
    val availableLanguagesIDs: List<Byte>?,
    val authorsIDs: List<Int>?,
    val userID: Int?,
    val tagsIDs: List<Short>?,
    val rating: Int,
)