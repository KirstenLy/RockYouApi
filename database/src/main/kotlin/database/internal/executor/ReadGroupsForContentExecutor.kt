package database.internal.executor

import common.utils.takeIfNotEmpty
import database.external.model.group.Group
import database.external.model.group.GroupMember
import database.external.result.common.SimpleListResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.groupmember.SelectAllForGroup

/** @see execute */
internal class ReadGroupsForContentExecutor(private val database: Database) {

    /**
     * Read language ID list for content.
     * If not existed [contentID] passed, return [SimpleListResult.Data] with emptyList.
     *
     * Respond as:
     * - [SimpleListResult.Data] Request finished without errors.
     * - [SimpleListResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(contentID: Int): SimpleListResult<Group> {
        return withContext(Dispatchers.IO) {
            try {
                val groupIDsForContent = database.selectGroupMemberQueries
                    .selectAllGroupForContent(contentID)
                    .executeAsList()
                    .takeIfNotEmpty()
                    ?: return@withContext SimpleListResult.Data(emptyList())

                val groupInfoList = database.selectGroupQueries
                    .selectGroupInfoByID(groupIDsForContent)
                    .executeAsList()
                    .takeIfNotEmpty()
                    ?: return@withContext SimpleListResult.Data(emptyList())

                val groupMemberList = database.selectGroupMemberQueries
                    .selectAllForGroup(groupIDsForContent)
                    .executeAsList()
                    .takeIfNotEmpty()
                    ?: return@withContext SimpleListResult.Data(emptyList())

                val result = groupInfoList.map { groupInfo ->
                    val groupID = groupInfo.groupID
                    val groupTitle = groupInfo.groupTitle

                    Group(
                        name = groupTitle,
                        memberList = groupMemberList
                            .filterByGroupID(groupID)
                            .map(SelectAllForGroup::toGroupMember)
                    )
                }
                return@withContext SimpleListResult.Data(result)
            } catch (t: Throwable) {
                return@withContext SimpleListResult.Error(t)
            }
        }
    }

    private fun List<SelectAllForGroup>.filterByGroupID(groupID: Int) = filter { it.groupID == groupID }
}

private fun SelectAllForGroup.toGroupMember() = GroupMember(
    contentID = contentID,
    name = contentTitle,
    order = orderIDX
)