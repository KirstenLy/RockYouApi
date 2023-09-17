package database.internal.executor

import database.external.filter.StoryByIDFilter
import database.external.result.SimpleListResult
import database.external.result.SimpleOptionalDataResult
import database.internal.AvailableLanguageModel
import database.internal.utils.getDefaultLangID
import database.internal.utils.selectByIdAndEnv
import declaration.entity.Author
import declaration.entity.Tag
import declaration.entity.User
import declaration.entity.story.StoryNew
import rockyouapi.DBTest

internal class GetStoryByIDRequestExecutorLegacy1(
    private val database: DBTest,
    private val availableLanguages: List<AvailableLanguageModel>,
    private val storyNodesRequestExecutor: GetStoryNodesRequestExecutor
) {

    suspend fun execute(filter: StoryByIDFilter): SimpleOptionalDataResult<StoryNew> {
        val storyRegisterID = filter.storyID
        val environmentLangID = filter.environmentLangID

        throw IllegalStateException()

//        val storyRecord = try {
//            throw IllegalStateException()
//            database.contentRegisterQueries
//                .selectStoryByID(storyRegisterID)
//                .executeAsOneOrNull()
                ?: return SimpleOptionalDataResult.DataNotFounded()
//        } catch (t: Throwable) {
//            return SimpleOptionalDataResult.Error(t)
//        }

//        val storyEntityID = storyRecord.entityID
//        val storyLanguageID = storyRecord.storyLanguageID
//        val storyLanguage = availableLanguages.selectByIdAndEnv(storyLanguageID.toByte(), availableLanguages.getDefaultLangID())
//
//        val storyAvailableLanguages = try {
//            database.relationStoryAndLanguageQueries
//                .selectStoryAvailableLanguage(storyEntityID)
//                .executeAsList()
//                .map { langID -> availableLanguages.selectByIdAndEnv(langID.toByte(), availableLanguages.getDefaultLangID()) }
//        } catch (t: Throwable) {
//            return SimpleOptionalDataResult.Error(t)
//        }
//
//        val storyAuthors = try {
//            database.relationStoryAndAuthorQueries
//                .selectStoryAuthors(storyEntityID)
//                .executeAsList()
//                .map { Author(it.authorID, it.authorName) }
//        } catch (t: Throwable) {
//            return SimpleOptionalDataResult.Error(t)
//        }
//
//        val storyUserID = storyRecord.storyUserID
//        val storyUser = try {
//            database.userProdQueries
//                .selectByID(storyUserID)
//                .executeAsOne()
//                .let { User(it.id, it.name) }
//        } catch (t: Throwable) {
//            return SimpleOptionalDataResult.Error(t)
//        }
//
//        val storyTags = try {
//            database.relationStoryAndTagQueries
//                .selectTagsForStory(storyEntityID)
//                .executeAsList()
//                .filter { it.tagLangID.toByte() == availableLanguages.getDefaultLangID() }
//                .map { Tag(it.tagID, it.tagTranslation!!) }
//        } catch (t: Throwable) {
//            return SimpleOptionalDataResult.Error(t)
//        }
//
//        val storyNodes =
//            when (val getNodesResult = storyNodesRequestExecutor.execute(storyRegisterID, environmentLangID)) {
//                is SimpleListResult.Data -> getNodesResult.data
//                is SimpleListResult.Error -> return SimpleOptionalDataResult.Error(getNodesResult.t)
//            }
//
//        val commentsCount = try {
//            database.storyQueries.countCommentsForStory(storyRegisterID).executeAsOne()
//        } catch (t: Throwable) {
//            return SimpleOptionalDataResult.Error(t)
//        }
//
//        val story = StoryNew(
//            id = storyRegisterID,
//            title = storyRecord.storyTitle,
//            language = storyLanguage,
//            availableLanguages = storyAvailableLanguages,
//            authors = storyAuthors,
//            user = storyUser,
//            tags = storyTags,
//            rating = storyRecord.storyRating,
//            commentsCount = commentsCount,
//            nodes = storyNodes
//        )

//        return SimpleOptionalDataResult.Data(story)
    }
}