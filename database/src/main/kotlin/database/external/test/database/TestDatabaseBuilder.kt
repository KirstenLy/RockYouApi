package database.external.test.database

import database.external.DatabaseAPI
import database.external.test.*

/** Contract of database builder that builds database locally in memory and can be used into tests. */
interface TestDatabaseBuilder {

    /** Add registers to database. */
    fun setContentRegisters(contentRegisters: List<TestContentRegister>)

    /** Add languages to database. */
    fun setLanguages(languages: List<TestLanguage>)

    /** Add users to database. */
    fun setUsers(users: List<TestUser>)

    /**
     * Add users auth to database.
     * Don't check if auth data belong to not existed user or user has two or more auth data.
     * */
    fun setUsersAuthData(authData: List<TestUserAuthData>)

    /** Add authors to the database. */
    fun setAuthors(authors: List<TestAuthor>)

    /** Add pictures to the database. */
    fun setPictures(pictures: List<TestPicture>)

    /** Add videos to database. */
    fun setVideos(videos: List<TestVideo>)

    /** Add stories to the database. */
    fun setStories(stories: List<TestStory>)

    /**
     * Add story nodes to the database.
     * Don't check if the node belongs to a not existed story.
     * Don't check relationships between nodes.
     * */
    fun setStoriesNodes(nodes: List<Pair<Int, List<TestStoryNode>>>)

    /** Add chapters to database. */
    fun setStoryChapters(storyChapters: List<TestStoryChapter>)

    /**
     * Add favorite records to database.
     * Don't check if contentID and userID link to existence entities.
     * */
    fun setFavorites(favorites: List<TestFavorite>)

    /** Add tags to database. */
    fun setTags(tags: List<TestTag>)

    /** Add comments to database. */
    fun setComments(comments: List<TestComment>)

    /**
     * Add relations between tags and their translations.
     * There is no any check about tags or translation existence, so it will crush at runtime if wrong relations set.
     * */
    fun setRelationsBetweenTagsAndTranslations(tagsAndTranslationsRelations: List<TestRelationBetweenTagAndTagTranslation>)

    /** Build and connect to the database. */
    fun build(): DatabaseAPI
}