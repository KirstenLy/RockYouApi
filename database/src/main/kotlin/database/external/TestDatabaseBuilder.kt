package database.external

import database.external.model.*

/** Contract of database builder that build database locally on device and can be used into tests. */
interface TestDatabaseBuilder {

    /** Add registers to database. */
    fun setContentRegisters(contentRegisters: List<TestContentRegister>)

    /** Add languages to database. */
    fun setLanguages(languages: List<TestLanguage>)

    /** Add users to database. */
    fun setUsers(users: List<TestUser>)

    /** Add authors to the database. */
    fun setAuthors(authors: List<TestAuthor>)

    /** Add pictures to the database. */
    fun setPictures(pictures: List<TestPicture>)

    /** Add videos to database. */
    fun setVideos(videos: List<TestVideo>)

    /** Add stories to the database. */
    fun setStories(stories: List<TestStory>)

    /** Add chapters to database. */
    fun setStoryChapters(storyChapters: List<TestStoryChapter>)

    /** Add tags to database. */
    fun setTags(tags: List<TestTag>)

    /** Add comments to database. */
    fun setComments(comments: List<TestComment>)

    /**
     * Add relations between tags and their translations.
     * There is no any check about tags or translation existence, so it will crush at runtime if wrong relations set.
     * */
    fun setRelationsBetweenTagsAndTranslations(tagsAndTranslationsRelations: List<TestRelationBetweenTagAndTagTranslation>)

    /**
     * Add relations between pictures and tags.
     * There is no any check about picture or tag existence, so it will crush at runtime if wrong relations set.
     * */
    fun setRelationsBetweenPicturesAndTags(picturesAndTagsRelations: List<TestRelationBetweenPictureAndTag>)

    /** Build and connect to the database. */
    fun build(): DatabaseAPI
}