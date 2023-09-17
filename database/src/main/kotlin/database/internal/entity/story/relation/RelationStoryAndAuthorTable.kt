package database.internal.entity.story.relation

import database.internal.entity.author.AuthorTable
import database.internal.entity.story.StoryTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationStoryAndAuthorTable : IntIdTable("RelationStoryAndAuthor") {

    val storyID = reference("storyID", StoryTable)

    val authorID = reference("authorID", AuthorTable)
}