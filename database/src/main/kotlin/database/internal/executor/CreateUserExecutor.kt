package database.internal.executor

import database.external.model.achievement.*
import database.external.model.achievement.additional.CommentatorAchievementAdditionalData
import database.external.model.achievement.additional.ContentMakerAchievementAdditionalData
import database.external.model.achievement.additional.TasterAchievementAdditionalData
import database.external.model.user.UserFull
import database.external.model.user.UserRole
import database.external.result.RegisterUserResult
import database.external.security.hashPassword
import database.internal.indexToUserRole
import database.internal.userRoleToIndex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rockyouapi.Database
import java.time.LocalDateTime

/** @see execute */
internal class CreateUserExecutor(private val database: Database) {

    /**
     * Create user.
     * Don't check login and password conditions (password length or smith else), it is a caller's task.
     * Save password hash, don't save password itself.
     *
     * Respond as:
     * - [RegisterUserResult.Ok] User created.
     * - [RegisterUserResult.SameUserAlreadyExist] User with same [login] already exists.
     * - [RegisterUserResult.Error] Smith unexpected happens on query stage.
     * */
    suspend fun execute(login: String, password: String, role: UserRole): RegisterUserResult {
        return withContext(Dispatchers.IO) {
            try {
                val alreadyExistedUser = database.selectUserQueries
                    .isUserExistByName(login)
                    .executeAsOne()

                if (alreadyExistedUser) {
                    return@withContext RegisterUserResult.SameUserAlreadyExist
                }

                val user = database.insertUserQueries.transactionWithResult {

                    database.insertUserQueries.insert(
                        id = null,
                        name = login,
                        role = userRoleToIndex(role)
                    )

                    val insertedUserID = database.selectUserQueries
                        .selectLastInsertedUserID()
                        .executeAsOne()

                    database.insertAuthQueries.insert(
                        id = null,
                        userID = insertedUserID,
                        passwordHash = hashPassword(password),
                        refreshToken = null
                    )

                    database.selectUserQueries
                        .selectOneByID(insertedUserID)
                        .executeAsOne()
                }

                val result = UserFull(
                    id = user.id,
                    name = user.name,
                    role = indexToUserRole(user.role),
                    registrationDate = LocalDateTime.now(),
                    avatarURL = null,
                    achievementData = AchievementData(
                        repairAchievementData = RepairAchievementData(false),
                        advisorAchievementData = AdvisorAchievementData(false),
                        commentatorAchievementData = CommentatorAchievementData(
                            isReached = false,
                            additionalData = CommentatorAchievementAdditionalData(
                                totalCommentCount = 0,
                                requirementCommentCount = 20
                            )
                        ),
                        tasterAchievementData = TasterAchievementData(
                            isReached = false,
                            additionalData = TasterAchievementAdditionalData(
                                totalRateActions = 0,
                                requirementRateActions = 50
                            )
                        ),
                        contentMakerAchievementData = ContentMakerAchievementData(
                            isReached = false,
                            additionalData = ContentMakerAchievementAdditionalData(
                                totalApprovedContent = 0,
                                requirementApprovedContent = 5
                            )
                        )
                    )
                )

                return@withContext RegisterUserResult.Ok(result)
            } catch (t: Throwable) {
                return@withContext RegisterUserResult.Error(t)
            }
        }
    }
}