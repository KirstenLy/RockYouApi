package database

import common.storage.StaticMapStorage.getOrCreateValue
import database.external.reader.readDatabaseConfigurationFromEnv
import database.internal.creator.createDB
import database.internal.executor.ReadTagsForMultipleContentExecutor
import database.utils.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.TestInstance

/** Test of [ReadTagsForMultipleContentExecutor]. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GetTagListForContentListExecutorTest {

    private val database = runBlocking {
        getOrCreateValue(KEY_STATIC_MAP_DB) { createDB(readDatabaseConfigurationFromEnv()) }
    }

    private val executor by lazy {
        ReadTagsForMultipleContentExecutor(database)
    }

//    @RepeatedTest(5)
//    fun execute_with_empty_content_id_list_return_empty_result() {
//        runTest {
//            validEnvironmentIDList.forEach { environmentLangID ->
//
//                val actualTagMap = executor.execute(
//                    contentIDList = emptyList(),
//                    environmentID = environmentLangID
//                )
//                    .asDataOrFailed()
//
//                assertEquals(emptyMap<Int, List<Tag>>(), actualTagMap)
//            }
//        }
//    }
//
//    @RepeatedTest(5)
//    fun execute_with_non_existed_content_id_list_return_empty_result() {
//        runTest {
//            validEnvironmentIDList.forEach { environmentLangID ->
//
//                val actualTagMap = executor.execute(
//                    contentIDList = generateNotExistedEntityIDList(),
//                    environmentID = environmentLangID
//                )
//                    .asDataOrFailed()
//
//                assertEquals(emptyMap<Int, List<Tag>>(), actualTagMap)
//            }
//        }
//    }
//
//    @RepeatedTest(5)
//    fun execute_with_existed_content_id_but_not_existed_env_id_return_empty_result() {
//        runTest {
//            invalidEnvironmentIDList.forEach { environmentLangID ->
//
//                val actualTagMap = executor.execute(
//                    contentIDList = database.selectRandomContentIDList(),
//                    environmentID = environmentLangID
//                )
//                    .asDataOrFailed()
//
//                assertEquals(emptyMap<Int, List<Tag>>(), actualTagMap)
//            }
//        }
//    }
//
//    @RepeatedTest(5)
//    fun execute_with_existed_content_id_list_return_correct_filled_result() {
//        runTest {
//            val severalRandomPictureIDList = database.selectContentIDThatConnectedWithSomeTags()
//
//            validEnvironmentIDList.forEach { environmentLangID ->
//
//                val actualTagMap = executor.execute(
//                    contentIDList = severalRandomPictureIDList,
//                    environmentID = environmentLangID
//                )
//                    .asDataOrFailed()
//
//                val actualTagMapSize = actualTagMap.size
//                val expectedTagMapSize = severalRandomPictureIDList.size
//
//                assertEquals(expectedTagMapSize, actualTagMapSize)
//
//                actualTagMap.forEach { (actualContentID, actualTagList) ->
//                    val expectedTagIDList = database.selectTagIDListForContent(actualContentID)
//
//                    val actualTagListSize = actualTagList.size
//                    val expectedTagListSize = expectedTagIDList.size
//                    assertEquals(expectedTagListSize, actualTagListSize)
//
//                    actualTagList.forEach { actualTag ->
//                        val actualTagTranslation = actualTag.name
//                        val expectedTagTranslation = supportedTagList.selectTagByIdAndEnv(
//                            tagID = actualTag.id,
//                            environmentID = environmentLangID
//                        ).name
//
//                        assertEquals(expectedTagTranslation, actualTagTranslation)
//                    }
//                }
//            }
//        }
//    }
//
//    @RepeatedTest(5)
//    fun execute_with_existed_and_not_existed_content_id_list_return_correct_size_result() {
//        runTest {
//            validEnvironmentIDList.forEach { environmentLangID ->
//                val severalRandomPictureIDList = database.selectContentIDThatConnectedWithSomeTags().take(2)
//
//                val contentIDList = listOf(
//                    severalRandomPictureIDList[0],
//                    severalRandomPictureIDList[1],
//                    99999 // No data for this contentID expected
//                )
//
//                val actualTagMap = executor.execute(
//                    contentIDList = contentIDList,
//                    environmentID = environmentLangID
//                )
//                    .asDataOrFailed()
//
//                val actualTagMapSize = actualTagMap.size
//                val expectedTagModelsStorage = contentIDList.size - 1
//                assertEquals(expectedTagModelsStorage, actualTagMapSize)
//            }
//        }
//    }
}