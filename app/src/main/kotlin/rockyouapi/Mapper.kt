package rockyouapi

import rockyouapi.model.*
import rockyouapi.model.achievement.*
import rockyouapi.model.achievement.additional.CommentatorAchievementAdditionalData
import rockyouapi.model.achievement.additional.ContentMakerAchievementAdditionalData
import rockyouapi.model.achievement.additional.TasterAchievementAdditionalData
import rockyouapi.model.group.Group
import rockyouapi.model.group.GroupMember
import rockyouapi.model.story.Story
import rockyouapi.model.story.StoryForkVariant
import rockyouapi.model.story.StoryNode
import rockyouapi.model.user.UserFull
import rockyouapi.model.user.UserRole
import rockyouapi.model.user.UserSimple
import database.external.model.Author as DBAuthor
import database.external.model.Chapter as DBChapter
import database.external.model.CharacterBasicInfo as DBCharacter
import database.external.model.Comment as DBComment
import database.external.model.Picture as DBPicture
import database.external.model.Video as DBVideo
import database.external.model.Vote as DBVote
import database.external.model.achievement.AchievementData as DBAchievementData
import database.external.model.achievement.AdvisorAchievementData as DBAdvisorAchievementData
import database.external.model.achievement.CommentatorAchievementData as DBCommentatorAchievementData
import database.external.model.achievement.ContentMakerAchievementData as DBContentMakerAchievementData
import database.external.model.achievement.RepairAchievementData as DBRepairAchievementData
import database.external.model.achievement.TasterAchievementData as DBTasterAchievementData
import database.external.model.achievement.additional.CommentatorAchievementAdditionalData as DBCommentatorAchievementAdditionalData
import database.external.model.achievement.additional.ContentMakerAchievementAdditionalData as DBContentMakerAchievementAdditionalData
import database.external.model.achievement.additional.TasterAchievementAdditionalData as DBTasterAchievementAdditionalData
import database.external.model.group.Group as DBGroup
import database.external.model.group.GroupMember as DBGroupMember
import database.external.model.language.LanguageSimple as DBLanguageSimple
import database.external.model.story.Story as DBStory
import database.external.model.story.StoryForkVariant as DBStoryForkVariant
import database.external.model.story.StoryNode as DBStoryNode
import database.external.model.tag.TagSimple as DBTagSimple
import database.external.model.user.UserFull as DBUserFullInfo
import database.external.model.user.UserRole as DBUserRole
import database.external.model.user.UserSimple as DBUserSimple

internal fun DBPicture.toWeb() = Picture(
    id = id,
    originalContentID = originalContentID,
    title = title,
    url = url,
    language = language?.toWeb(),
    availableLanguages = availableLanguages.map(DBLanguageSimple::toWeb),
    authors = authors.map(DBAuthor::toWeb),
    user = user?.toWeb(),
    tags = tags.map(DBTagSimple::toWeb),
    translatorName = translatorName,
    rating = rating,
    characters = characters.map(DBCharacter::toWeb),
    commentsCount = commentsCount,
    groups = groups.map(DBGroup::toWeb)
)

internal fun DBVideo.toWeb() = Video(
    id = id,
    title = title,
    url = url,
    language = language?.toWeb(),
    availableLanguages = availableLanguages.map(DBLanguageSimple::toWeb),
    authors = authors.map(DBAuthor::toWeb),
    user = user?.toWeb(),
    tags = tags.map(DBTagSimple::toWeb),
    rating = rating,
    characters = characters.map(DBCharacter::toWeb),
    commentsCount = commentsCount,
    groups = groups.map(DBGroup::toWeb)
)

internal fun DBStory.toWeb() = Story(
    id = id,
    title = title,
    language = language?.toWeb(),
    availableLanguages = availableLanguages.map(DBLanguageSimple::toWeb),
    authors = authors.map(DBAuthor::toWeb),
    user = user?.toWeb(),
    tags = tags.map(DBTagSimple::toWeb),
    rating = rating,
    characters = characters.map(DBCharacter::toWeb),
    commentsCount = commentsCount,
    groups = groups.map(DBGroup::toWeb),
    nodes = nodes.map(DBStoryNode::toWeb)
)

internal fun DBChapter.toWeb() = Chapter(
    id = id,
    storyID = storyID,
    title = title,
    language = language.toWeb(),
    availableLanguages = availableLanguages.map(DBLanguageSimple::toWeb),
    authors = authors.map(DBAuthor::toWeb),
    user = user?.toWeb(),
    tags = tags.map(DBTagSimple::toWeb),
    rating = rating,
    commentsCount = commentsCount,
)

internal fun DBComment.toWeb() = Comment(
    id = id,
    contentID = contentID,
    userID = userID,
    userName = userName,
    text = text,
    creationDate = creationDate.toString()
)

internal fun DBVote.toWeb() = Vote(
    contentID = contentID,
    vote = vote
)

internal fun DBUserFullInfo.toWeb() = UserFull(
    id = id,
    name = name,
    registrationDate = registrationDate.toString(),
    avatarURL = avatarURL,
    role = role?.toWeb()?.roleValue,
    achievementData = achievementData.toWeb()
)

private fun DBLanguageSimple.toWeb() = Language(
    id = id,
    name = name
)

private fun DBAuthor.toWeb() = Author(
    id = id,
    name = name
)

private fun DBUserSimple.toWeb() = UserSimple(
    id = id,
    name = name
)

private fun DBTagSimple.toWeb() = ContentTag(
    id = id,
    name = name,
    isOneOfMainTag = isOneOfMainTag
)

private fun DBCharacter.toWeb() = Character(
    id = id,
    name = name,
)

private fun DBGroup.toWeb() = Group(
    name = name,
    memberList = memberList.map(DBGroupMember::toWeb)
)

private fun DBGroupMember.toWeb() = GroupMember(
    contentID = contentID,
    name = name,
    order = order
)

private fun DBStoryNode.toWeb() = when (this) {
    is DBStoryNode.ChapterNode -> StoryNode.ChapterNode(chapter.toWeb())
    is DBStoryNode.ForkNode -> StoryNode.ForkNode(variants.map { it.toWeb() })
}

private fun DBStoryForkVariant.toWeb(): StoryForkVariant {
    val webNodes = nodes.map { it.toWeb() }
    return StoryForkVariant(
        variantText = variantText,
        nodes = webNodes
    )
}

private fun DBUserRole.toWeb() = when (this) {
    DBUserRole.MEMBER -> UserRole.MEMBER
    DBUserRole.MODERATOR -> UserRole.MODERATOR
    DBUserRole.ADMIN -> UserRole.ADMIN
}

private fun DBAchievementData.toWeb() = AchievementData(
    repairAchievementData = repairAchievementData.toWeb(),
    advisorAchievementData = advisorAchievementData.toWeb(),
    commentatorAchievementData = commentatorAchievementData.toWeb(),
    tasterAchievementData = tasterAchievementData.toWeb(),
    contentMakerAchievementData = contentMakerAchievementData.toWeb(),
)

private fun DBRepairAchievementData.toWeb() = RepairAchievementData(isReached = isReached)

private fun DBAdvisorAchievementData.toWeb() = AdvisorAchievementData(isReached = isReached)

private fun DBCommentatorAchievementData.toWeb() = CommentatorAchievementData(
    isReached = isReached,
    additionalData = additionalData?.toWeb()
)

private fun DBCommentatorAchievementAdditionalData.toWeb() = CommentatorAchievementAdditionalData(
    totalCommentCount = totalCommentCount,
    requirementCommentCount = requirementCommentCount
)

private fun DBTasterAchievementData.toWeb() = TasterAchievementData(
    isReached = isReached,
    additionalData = additionalData?.toWeb()
)

private fun DBTasterAchievementAdditionalData.toWeb() = TasterAchievementAdditionalData(
    totalRateActions = totalRateActions,
    requirementRateActions = requirementRateActions
)

private fun DBContentMakerAchievementData.toWeb() = ContentMakerAchievementData(
    isReached = isReached,
    additionalData = additionalData?.toWeb()
)

private fun DBContentMakerAchievementAdditionalData.toWeb() = ContentMakerAchievementAdditionalData(
    totalApprovedContent = totalApprovedContent,
    requirementApprovedContent = requirementApprovedContent
)