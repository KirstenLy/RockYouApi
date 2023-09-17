package database.test.picture

import database.internal.creator.test.connectToDatabase
import database.internal.test.fillFullByGeneratedContent
import database.utils.*
import kotlin.test.Test

internal class PictureListTests {

    @Test
    fun select_base_picture_info_list_work_correct_test() {
        val database = connectToDatabase()
        val testModels = database.fillFullByGeneratedContent()

//        database.pictureQueries.selectLastBaseInfoList().insert(
//            id = 1,
//            contentType = 1,
//            contentID = 1
//        )
    }
}