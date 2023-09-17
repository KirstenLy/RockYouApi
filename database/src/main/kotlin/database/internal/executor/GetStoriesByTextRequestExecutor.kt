package database.internal.executor

import database.external.result.SimpleListResult
import database.external.result.SimpleOptionalDataResult
import database.internal.ContentType
import database.internal.entity.content_register.ContentRegisterTable
import database.internal.entity.story.StoryTable
import database.internal.executor.common.selectByContentType
import declaration.entity.Story
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.leftJoin

@Suppress("RemoveRedundantQualifierName")
internal class GetStoriesByTextRequestExecutor(
    private val getStoryByIDRequestExecutor: GetStoryByIDRequestExecutor
) {

    fun execute(text: String): SimpleListResult<Story> {
        val storiesEntityIDs = ContentRegisterTable.leftJoin(
            otherTable = StoryTable,
            onColumn = { ContentRegisterTable.contentID },
            otherColumn = { StoryTable.id }
        )
            .selectByContentType(ContentType.STORY)
            .andWhere { StoryTable.title like "%$text%" }
            .mapNotNull { it[ContentRegisterTable.contentID] }

        if (storiesEntityIDs.isEmpty()) {
            return SimpleListResult.Data(emptyList())
        }

        val response = storiesEntityIDs.mapNotNull {
            when (val getStoriesResult = getStoryByIDRequestExecutor.execute(it)) {
                is SimpleOptionalDataResult.Data -> getStoriesResult.model
                is SimpleOptionalDataResult.DataNotFounded -> null
                is SimpleOptionalDataResult.Error -> null
            }
        }

        return SimpleListResult.Data(response)
    }
}