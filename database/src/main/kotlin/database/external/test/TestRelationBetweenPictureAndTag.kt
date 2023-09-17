package database.external.test

/**
 * Content register model for test database.
 * @see database.external.TestDatabaseBuilder
 * */ // COMMENT
data class TestRelationBetweenPictureAndTag(
    val pictureID: Int,
    val tagID: Int,
)