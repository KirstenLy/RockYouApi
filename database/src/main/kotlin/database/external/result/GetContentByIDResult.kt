package database.external.result

import database.external.model.Chapter
import database.external.model.Picture
import database.external.model.story.Story
import database.external.model.Video

/** @see database.internal.executor.ReadContentByIDListExecutor */
data class GetContentByIDResult(
    val pictures: List<Picture> = emptyList(),
    val videos: List<Video> = emptyList(),
    val stories: List<Story> = emptyList(),
    val chapters: List<Chapter> = emptyList()
)