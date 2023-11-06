package rockyouapi.model.result

import kotlinx.serialization.Serializable
import rockyouapi.model.Chapter
import rockyouapi.model.Picture
import rockyouapi.model.Video
import rockyouapi.model.story.Story

/** All content read by ID. */
@Serializable
internal data class GetContentByIDResult(
    val pictures: List<Picture> = emptyList(),
    val videos: List<Video> = emptyList(),
    val stories: List<Story> = emptyList(),
    val chapters: List<Chapter> = emptyList()
)