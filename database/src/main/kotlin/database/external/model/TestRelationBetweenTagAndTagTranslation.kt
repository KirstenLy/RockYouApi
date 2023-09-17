package database.external.model

/**
 * Content register model for test database.
 * @see database.external.TestDatabaseBuilder
 * */ // COMMENT
data class TestRelationBetweenTagAndTagTranslation(
    val tagID: Int,
    val translationID: Int,
)