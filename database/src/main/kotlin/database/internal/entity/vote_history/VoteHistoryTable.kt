package database.internal.entity.vote_history

import database.internal.entity.user.UserTable
import org.jetbrains.exposed.dao.id.IntIdTable

internal object VoteHistoryTable : IntIdTable("VoteHistory") {

    val userID = reference("userID", UserTable)

    // TODO: Должна быть связь с таблицей контента
    val contentID = integer("contentID")

    val vote = byte("vote")
}