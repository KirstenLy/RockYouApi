package database.external.test

/**
 * Content register model for test database.
 * @see database.external.TestDatabaseBuilder
 * */ // COMMENT
data class TestRelationBetweenTagAndTagTranslation(
    val tagID: Int,
    val translationID: Int,
)