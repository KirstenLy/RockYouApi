package database.internal.executor

import database.external.result.SimpleOptionalDataResult
import database.internal.entity.chapter_text.ChapterTextEntity
import database.internal.entity.content_register.ContentRegisterEntity

internal class GetChapterTextByIDRequestExecutorLegacy() {

    fun execute(chapterRegisterID: Int): SimpleOptionalDataResult<String> {
        val chapterEntityID = ContentRegisterEntity
            .findById(chapterRegisterID)
            ?.contentID
            ?: return SimpleOptionalDataResult.DataNotFounded()

        val chapterText = ChapterTextEntity
            .findById(chapterEntityID)
            ?.text
            ?: return SimpleOptionalDataResult.DataNotFounded()
        return SimpleOptionalDataResult.Data(chapterText)
    }
}