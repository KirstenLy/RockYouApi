package database.internal.entity.story.relation

import database.internal.entity.comment.CommentTable
import database.internal.entity.story.StoryTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object RelationStoryAndCommentTable : IntIdTable("RelationStoryAndComment") {

    val storyID = reference("storyID", StoryTable)

    val commentID = reference("commentID", CommentTable)
}