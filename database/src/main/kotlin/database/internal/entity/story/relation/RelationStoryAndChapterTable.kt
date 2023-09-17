package database.internal.entity.story.relation

import database.internal.entity.chapter.ChapterTable
import database.internal.entity.story.StoryTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationStoryAndChapterTable : IntIdTable("RelationStoryAndChapter") {

    val storyID = reference("storyID", StoryTable)

    val chapterID = reference("chapterID", ChapterTable)
}