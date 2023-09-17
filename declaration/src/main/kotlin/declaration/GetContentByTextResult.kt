package declaration

import declaration.entity.Picture
import declaration.entity.Story
import declaration.entity.StoryChapter
import declaration.entity.Video
import kotlinx.serialization.Serializable

@Serializable
data class GetContentByTextResult(
    val pictures: List<Picture> = emptyList(),
    val videos: List<Video> = emptyList(),
    val stories: List<Story> = emptyList(),
    val chapters: List<StoryChapter> = emptyList()
)

fun GetContentByTextResult.isEmpty() = pictures.isEmpty() && videos.isEmpty() && stories.isEmpty() && chapters.isEmpty()