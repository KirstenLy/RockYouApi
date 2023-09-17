package database.external

/** All entity types that can be operated. */
enum class ContentType(val typeID: Int) {
    PICTURE(1),
    VIDEO(2),
    STORY(3),
    STORY_CHAPTER(4)
}