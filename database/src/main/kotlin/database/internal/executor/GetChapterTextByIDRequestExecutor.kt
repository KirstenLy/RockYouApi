package database.internal.executor

import database.external.result.SimpleOptionalDataResult
import database.internal.entity.chapter_text.ChapterTextEntity
import database.internal.entity.content_register.ContentRegisterEntity
import rockyouapi.DBTest

internal class GetChapterTextByIDRequestExecutor(private val database: DBTest) {

    fun execute(chapterRegisterID: Int): SimpleOptionalDataResult<String> {
        val chapterText = try {
            database.chapterQueries
                .selectChapterTextByRegisterID(chapterRegisterID)
                .executeAsOneOrNull()
                ?: return SimpleOptionalDataResult.DataNotFounded()
        } catch (t: Throwable) {
            return SimpleOptionalDataResult.Error(t)
        }

        return SimpleOptionalDataResult.Data(chapterText)
    }
}