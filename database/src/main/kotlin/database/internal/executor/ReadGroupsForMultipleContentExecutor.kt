package database.internal.executor

import common.utils.takeIfNotEmpty
import database.external.model.group.Group
import database.external.model.group.GroupMember
import database.external.result.common.SimpleDataResult
import database.external.result.common.SimpleListResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import rockyouapi.groupmember.SelectAllForGroup
import rockyouapi.groupmember.SelectAllGroupForContentList

/** @see execute */
internal class ReadGroupsForMultipleContentExecutor(private val database: Database) {

    /**
     * Get groups for multiple content.
     *
     * Respond as:
     * - [SimpleListResult.Data] Request finished without errors.
     * - [SimpleListResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(contentIDList: List<Int>): SimpleDataResult<Map<Int, List<Group>>> {
        if (contentIDList.isEmpty()) return SimpleDataResult.Data(hashMapOf())
        return withContext(Dispatchers.IO) {
            try {
                val contentToGroupIDListMap = database.selectGroupMemberQueries
                    .selectAllGroupForContentList(contentIDList)
                    .executeAsList()
                    .takeIfNotEmpty()
                    ?.groupBy(
                        keySelector = SelectAllGroupForContentList::contentID,
                        valueTransform = SelectAllGroupForContentList::groupID
                    )
                    ?: return@withContext SimpleDataResult.Data(emptyMap())

                val allGroupIDList = contentToGroupIDListMap.values
                    .flatten()
                    .distinct()
                    .takeIfNotEmpty()
                    ?: return@withContext SimpleDataResult.Data(emptyMap())

                val allGroupInfoList = database.selectGroupQueries
                    .selectGroupInfoByID(allGroupIDList)
                    .executeAsList()
                    .takeIfNotEmpty()
                    ?: return@withContext SimpleDataResult.Data(emptyMap())

                val allGroupMemberList = database.selectGroupMemberQueries
                    .selectAllForGroup(allGroupIDList)
                    .executeAsList()
                    .takeIfNotEmpty()
                    ?: return@withContext SimpleDataResult.Data(emptyMap())

                val contentToGroupListMap = contentIDList.associateWith { contentID ->
                    val groupIDListForContentID = contentToGroupIDListMap[contentID]
                        ?: return@associateWith emptyList()

                    val groupInfoListForContentID = allGroupInfoList
                        .filter { it.groupID in groupIDListForContentID }
                        .map { groupInfo ->
                            Group(
                                name = groupInfo.groupTitle,
                                memberList = allGroupMemberList
                                    .filterByGroupID(groupInfo.groupID)
                                    .map(SelectAllForGroup::toGroupMember)
                            )
                        }

                    groupInfoListForContentID
                }
                return@withContext SimpleDataResult.Data(contentToGroupListMap)
            } catch (t: Throwable) {
                return@withContext SimpleDataResult.Error(t)
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