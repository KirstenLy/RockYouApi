package declaration

import declaration.entity.Picture
import declaration.entity.story.Story
import declaration.entity.story.Chapter
import declaration.entity.Video
import declaration.entity.story.StoryNew
import kotlinx.serialization.Serializable

@Serializable
data class GetContentByTextResult(
    val pictures: List<Picture> = emptyList(),
    val videos: List<Video> = emptyList(),
    val stories: List<StoryNew> = emptyList(),
    val chapters: List<Chapter> = emptyList()
)

fun GetContentByTextResult.isEmpty() = pictures.isEmpty() && videos.isEmpty() && stories.isEmpty() && chapters.isEmpty()