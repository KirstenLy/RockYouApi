package database.data

import database.external.operation.FavoriteOperation
import database.internal.executor.CreateOrDeleteFavoriteExecutor
import database.internal.mock.*
import java.util.stream.Stream

/** Model of data to test [CreateOrDeleteFavoriteExecutor]. */
internal data class AddOrRemoveFavoriteTestDataWithoutOperation(
    val userID: Int,
    val contentID: Int
)

/** Model of data to test [CreateOrDeleteFavoriteExecutor] in test where operation argument not required. */
internal data class AddOrRemoveFavoriteTestData(
    val operation: FavoriteOperation,
    val userID: Int,
    val contentID: Int
)

/** Data creator to test [CreateOrDeleteFavoriteExecutor].*/
internal object AddOrRemoveFavoriteTestDataStreamCreator {

    /** Data to test [CreateOrDeleteFavoriteExecutor] when [AddOrRemoveFavoriteTestData.userID] link to not existed user. */
    @JvmStatic
    fun userNotExistScenarioTestData(): Stream<AddOrRemoveFavoriteTestData> = Stream.of(
        AddOrRemoveFavoriteTestData(
            operation = FavoriteOperation.ADD,
            userID = -1,
            contentID = picture1.id,
        ),
        AddOrRemoveFavoriteTestData(
            operation = FavoriteOperation.REMOVE,
            userID = -1,
            contentID = picture1.id,
        ),
        AddOrRemoveFavoriteTestData(
            operation = FavoriteOperation.ADD,
            userID = 0,
            contentID = picture1.id,
        ),
        AddOrRemoveFavoriteTestData(
            operation = FavoriteOperation.REMOVE,
            userID = 0,
            contentID = picture1.id,
        ),
        AddOrRemoveFavoriteTestData(
            operation = FavoriteOperation.ADD,
            userID = 100000,
            contentID = picture1.id,
        ),
        AddOrRemoveFavoriteTestData(
            operation = FavoriteOperation.REMOVE,
            userID = 100000,
            contentID = picture1.id,
        ),
    )

    /** Data to test [CreateOrDeleteFavoriteExecutor] when [AddOrRemoveFavoriteTestData.contentID] link to not existed content. */
    @JvmStatic
    fun contentNotExistScenarioTestData(): Stream<AddOrRemoveFavoriteTestData> = Stream.of(
        AddOrRemoveFavoriteTestData(
            operation = FavoriteOperation.ADD,
            userID = user8.id,
            contentID = -100000
        ),
        AddOrRemoveFavoriteTestData(
            operation = FavoriteOperation.REMOVE,
            userID = user8.id,
            contentID = -100000
        ),
        AddOrRemoveFavoriteTestData(
            operation = FavoriteOperation.ADD,
            userID = user8.id,
            contentID = -1
        ),
        AddOrRemoveFavoriteTestData(
            operation = FavoriteOperation.REMOVE,
            userID = user8.id,
            contentID = -1
        ),
        AddOrRemoveFavoriteTestData(
            operation = FavoriteOperation.ADD,
            userID = user8.id,
            contentID = 0
        ),
        AddOrRemoveFavoriteTestData(
            operation = FavoriteOperation.REMOVE,
            userID = user8.id,
            contentID = 0
        ),
        AddOrRemoveFavoriteTestData(
            operation = FavoriteOperation.ADD,
            userID = user8.id,
            contentID = 100000
        ),
        AddOrRemoveFavoriteTestData(
            operation = FavoriteOperation.REMOVE,
            userID = user8.id,
            contentID = 100000
        )
    )

    /** Data to test [CreateOrDeleteFavoriteExecutor] when user try to remove record by valid arguments, but record not exist. */
    @JvmStatic
    fun removeNotExistedRecordScenarioTestData(): Stream<AddOrRemoveFavoriteTestDataWithoutOperation> = Stream.of(
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user8.id,
            contentID = story1.id
        ),
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user8.id,
            contentID = storyChapter1.id
        ),
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user8.id,
            contentID = videoContent1.id
        ),
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user8.id,
            contentID = picture1.id
        ),
    )

    /** Data to test [CreateOrDeleteFavoriteExecutor] when user just add content to favorite. */
    @JvmStatic
    fun simpleAddToFavoriteScenarioTestData(): Stream<AddOrRemoveFavoriteTestDataWithoutOperation> = Stream.of(
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user2.id,
            contentID = picture1.id,
        ),
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user5.id,
            contentID = picture1.id,
        ),
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user7.id,
            contentID = picture1.id,
        ),
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user8.id,
            contentID = picture1.id,
        ),
    )

    /** Data to test [CreateOrDeleteFavoriteExecutor] when user try to add in favorite already added content.  */
    @JvmStatic
    fun duplicateAddScenarioTestData(): Stream<AddOrRemoveFavoriteTestDataWithoutOperation> = Stream.of(
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user1.id,
            contentID = videoContent1.id,
        ),
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user9.id,
            contentID = story1.id,
        ),
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user3.id,
            contentID = picture1.id,
        ),
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user2.id,
            contentID = storyChapter1.id,
        )
    )

    /** Data to test [CreateOrDeleteFavoriteExecutor] when user try to remove existed record. */
    @JvmStatic
    fun removeExistedRecordScenarioTestData(): Stream<AddOrRemoveFavoriteTestDataWithoutOperation> = Stream.of(
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user8.id,
            contentID = story5.id
        ),
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user8.id,
            contentID = storyChapter5.id
        ),
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user8.id,
            contentID = videoContent5.id
        ),
        AddOrRemoveFavoriteTestDataWithoutOperation(
            userID = user8.id,
            contentID = picture5.id
        )
    )
}