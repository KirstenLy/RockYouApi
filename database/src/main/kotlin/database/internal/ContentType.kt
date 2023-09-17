package database.internal

/**
 * All clients entities types.
 * Other words, all entities that sends to client.
 * */
internal enum class ContentType(val typeID: Int) {
    PICTURE(1),
    VIDEO(2),
    STORY(3),
    STORY_CHAPTER(4)
}