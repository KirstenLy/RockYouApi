package database.internal

import database.external.model.*
import database.external.model.language.LanguageFull
import database.external.model.language.LanguageTranslation
import database.external.model.story.StoryForkVariant
import database.external.model.story.StoryNode
import database.external.model.tag.TagFull
import database.external.model.tag.TagTranslation
import database.external.model.user.UserRole
import database.internal.index.UserRoleIndex
import database.internal.model.*

/** Extract all chapter ID from database story scheme model. */
internal fun List<DBStoryNode>.extractAllChaptersIDs(): List<Int> {
    val chaptersIDs = mutableListOf<Int>()
    forEach { node ->
        when (node) {
            is DBStoryNode.Chapter -> chaptersIDs.add(node.chapter)
            is DBStoryNode.ForkNode -> chaptersIDs.addAll(node.extractAllChaptersIDs())
        }
    }
    return chaptersIDs
}


/** Extract all chapter ID from database fork node. */
internal fun DBStoryNode.ForkNode.extractAllChaptersIDs() = variants
    .map { it.nodes.extractAllChaptersIDs() }
    .flatten()


/** Convert database scheme to declaration scheme. */
internal fun List<DBStoryNode>.toDeclarationScheme(chapters: List<Chapter>): List<StoryNode> {
    val resultScheme = mutableListOf<StoryNode>()
    forEach { node ->
        when (node) {
            is DBStoryNode.Chapter -> {
                val chapter = chapters.first { it.id == node.chapter }
                resultScheme.add(StoryNode.ChapterNode(chapter))
            }

            is DBStoryNode.ForkNode -> {
                val forkNode = StoryNode.ForkNode(
                    variants = node.variants.toDeclarationForkVariants(chapters)
                )
                resultScheme.add(forkNode)
            }
        }
    }
    return resultScheme
}


/** Convert database fork variant to declaration fork variant. */
internal fun List<DBForkVariant>.toDeclarationForkVariants(chapters: List<Chapter>): List<StoryForkVariant> {
    val resultScheme = mutableListOf<StoryForkVariant>()
    forEach { forkVariant ->
        resultScheme.add(
            StoryForkVariant(
                variantText = forkVariant.variantText,
                nodes = forkVariant.nodes.toDeclarationScheme(chapters)
            )
        )
    }
    return resultScheme
}

internal fun DBLanguage.toSupportedLanguage() = LanguageFull(
    languageID = id,
    isDefault = isDefault,
    translations = translations.map(DBLanguageTranslation::toSupportedLanguageTranslation)
)

internal fun DBLanguageTranslation.toSupportedLanguageTranslation() = LanguageTranslation(
    languageID = id,
    environmentID = envID,
    translation = name
)

internal fun DBTag.toSupportedTag() = TagFull(
    tagID = id,
    translations = translations.map(DBTagTranslation::toSupportedTag)
)

internal fun DBTagTranslation.toSupportedTag() = TagTranslation(
    tagID = id,
    environmentID = envID,
    translation = name,
)

internal fun indexToUserRole(index: Int) = when (index) {
    UserRoleIndex.MEMBER.index -> UserRole.MEMBER
    UserRoleIndex.MODERATOR.index -> UserRole.MODERATOR
    UserRoleIndex.ADMIN.index -> UserRole.ADMIN
    else -> null
}

internal fun userRoleToIndex(userRole: UserRole) = when (userRole) {
    UserRole.MEMBER -> UserRoleIndex.MEMBER.index
    UserRole.MODERATOR -> UserRoleIndex.MODERATOR.index
    UserRole.ADMIN -> UserRoleIndex.ADMIN.index
}