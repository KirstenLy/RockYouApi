package database.external.result

/** @see database.external.DatabaseAPI.register */
sealed interface AddOrRemoveFavoriteResult {

    data object Ok : AddOrRemoveFavoriteResult

    data object AlreadyInFavorite : AddOrRemoveFavoriteResult

    data object NotInFavorite : AddOrRemoveFavoriteResult

    class Error(val t: Throwable) : AddOrRemoveFavoriteResult
}