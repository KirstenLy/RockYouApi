package database.external.test

import kotlinx.serialization.Serializable

@Serializable
sealed interface TestStoryNode {

    @Serializable
    data class TestChapterNode(val chapter: TestStoryChapter) : TestStoryNode

    @Serializable
    data class TestForkNode(val variants: List<TestStoryForkVariant>) : TestStoryNode
}