package database.internal.executor

import database.external.result.SimpleDataResult
import database.external.result.SimpleListResult
import database.external.ContentType
import database.internal.entity.chapter.ChapterTable
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.executor.common.selectByContentType
import declaration.entity.story.Chapter
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.leftJoin

@Suppress("RemoveRedundantQualifierName")
internal class GetChaptersByTextRequestExecutorLegacy(
    private val getStoryByIDRequestExecutor: GetChaptersRequestExecutor
) {

    fun execute(text: String): SimpleListResult<Chapter> {
        val chaptersEntityIDs = ContentRegisterTable.leftJoin(
            otherTable = ChapterTable,
            onColumn = { ContentRegisterTable.contentID },
            otherColumn = { ChapterTable.id }
        )
            .selectByContentType(ContentType.STORY_CHAPTER)
            .andWhere { ChapterTable.title like "%$text%" }
            .mapNotNull { it[ContentRegisterTable.contentID] }

        if (chaptersEntityIDs.isEmpty()) {
            return SimpleListResult.Data(emptyList())
        }

//        val response = chaptersEntityIDs.mapNotNull {
//            when (val getStoriesResult = getStoryByIDRequestExecutor.execute(listOf(it), null)) {
//                is SimpleDataResult.Data -> getStoriesResult.data
//                is SimpleDataResult.Error -> null
//            }
//        }
//            .map { it.values }
//            .flatten()
//            .flatten()


        return SimpleListResult.Error(IllegalStateException())
    }
}