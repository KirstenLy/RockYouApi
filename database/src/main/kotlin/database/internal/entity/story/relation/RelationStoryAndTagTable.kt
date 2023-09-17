package database.internal.entity.story.relation

import database.internal.entity.story.StoryTable
import database.internal.entity.tag.TagTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationStoryAndTagTable : IntIdTable("RelationStoryAndTag") {

    val storyID = reference("storyID", StoryTable)

    val tagID = reference("tagID", TagTable)
}