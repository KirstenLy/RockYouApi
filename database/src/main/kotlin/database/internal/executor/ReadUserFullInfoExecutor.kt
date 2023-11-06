package database.internal.executor

import database.external.model.*
import database.external.model.achievement.*
import database.external.model.achievement.additional.CommentatorAchievementAdditionalData
import database.external.model.achievement.additional.ContentMakerAchievementAdditionalData
import database.external.model.achievement.additional.TasterAchievementAdditionalData
import database.external.model.user.UserFull
import database.external.result.RegisterUserResult
import database.external.result.common.SimpleOptionalDataResult
import database.internal.index.AchievementIndex
import database.internal.indexToUserRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import kotlin.random.Random

/** @see execute */
internal class ReadUserFullInfoExecutor(private val database: Database) {

    /**
     * Read user full info.
     * Don't check login and password conditions (password length or smith else), it is a caller's task.
     * Save password hash, don't save password itself.
     *
     * Respond as:
     * - [RegisterUserResult.Ok] User created.
     * - [RegisterUserResult.SameUserAlreadyExist] User with same [login] already exists.
     * - [RegisterUserResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(userID: Int): SimpleOptionalDataResult<UserFull> {
        return withContext(Dispatchers.IO) {
            try {
                val isUserExist = database.selectUserQueries
                    .isUserExistByID(userID)
                    .executeAsOne()

                if (!isUserExist) {
                    return@withContext SimpleOptionalDataResult.DataNotFounded()
                }

                val userInfo = database.selectUserQueries
                    .selectOneByID(userID)
                    .executeAsOne()

                val achievementIndexes = AchievementIndex.entries.map(AchievementIndex::index)

                // Some achievements have simple state (reached or not reached) without any additional conditions.
                // Select all these achievements for user.
                // Most achievements can be selected so easily, so they will be selected other way.
                val userSimpleAchievementList = database.selectRelationUserAndAchievementQueries
                    .selectAchievementIDListForUser(userID)
                    .executeAsList()
                    .filter { it in achievementIndexes }

                val achievementData = AchievementData(
                    repairAchievementData = getRepairAchievementData(userSimpleAchievementList),
                    advisorAchievementData = getAdvisorAchievementData(userSimpleAchievementList),
                    commentatorAchievementData = getCommentatorAchievementData(userID),
                    tasterAchievementData = getTasterAchievementData(userID),
                    contentMakerAchievementData = getContentMakerAchievementData(userID)
                )

                val result = UserFull(
                    id = userInfo.id,
                    name = userInfo.name,
                    avatarURL = userInfo.avatarURL,
                    role = indexToUserRole(userInfo.role),
                    registrationDate = userInfo.registerDate,
                    achievementData = achievementData
                )
                return@withContext SimpleOptionalDataResult.Data(result)
            } catch (t: Throwable) {
                return@withContext SimpleOptionalDataResult.Error(t)
            }
        }
    }

    suspend fun execute(userName: String): SimpleOptionalDataResult<UserFull> {
        return withContext(Dispatchers.IO) {
            val isUserExist = database.selectUserQueries
                .isUserExistByName(userName)
                .executeAsOne()

            if (!isUserExist) {
                return@withContext SimpleOptionalDataResult.DataNotFounded()
            }

            val userID = database.selectUserQueries
                .selectOneIDByName(userName)
                .executeAsOne()

            return@withContext execute(userID)
        }
    }

    private fun getRepairAchievementData(userSimpleAchievementIDList: List<Int>) = RepairAchievementData(
        isReached = userSimpleAchievementIDList.contains(AchievementIndex.Repairer.index)
    )

    private fun getAdvisorAchievementData(userSimpleAchievementIDList: List<Int>) = AdvisorAchievementData(
        isReached = userSimpleAchievementIDList.contains(AchievementIndex.Advisor.index)
    )

    private fun getCommentatorAchievementData(userID: Int): CommentatorAchievementData {
        val userCommentsCount = database.selectCommentQueries
            .countForUser(userID)
            .executeAsOne()

        return when {
            userCommentsCount > 3 -> CommentatorAchievementData(
                isReached = true,
                additionalData = null
            )

            else -> CommentatorAchievementData(
                isReached = false,
                additionalData = CommentatorAchievementAdditionalData(
                    totalCommentCount = userCommentsCount.toInt(),
                    requirementCommentCount = 3
                )
            )
        }
    }

    private fun getTasterAchievementData(userID: Int): TasterAchievementData {
        val userRatingEventsCount = database.selectVoteHistoryQueries
            .countForUser(userID)
            .executeAsOne()
            .toInt()

        return when {
            userRatingEventsCount > 3 -> TasterAchievementData(
                isReached = true,
                additionalData = null
            )

            else -> TasterAchievementData(
                isReached = false,
                additionalData = TasterAchievementAdditionalData(
                    totalRateActions = userRatingEventsCount,
                    requirementRateActions = 3
                )
            )
        }
    }

    private fun getContentMakerAchievementData(userID: Int): ContentMakerAchievementData {
        val userContentCount = Random.nextInt()
        return when {
            userContentCount > 3 -> ContentMakerAchievementData(
                isReached = true,
                additionalData = null
            )

            else -> ContentMakerAchievementData(
                isReached = false,
                additionalData = ContentMakerAchievementAdditionalData(
                    totalApprovedContent = userContentCount,
                    requirementApprovedContent = 3
                )
            )
        }
    }
}