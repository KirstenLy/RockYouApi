package database.external.result

/** @see database.external.contract.ProductionDatabaseAPI.addOrRemoveFavorite */
sealed interface AddOrRemoveFavoriteResult {

    /** Favorite added or removed. */
    data object Ok : AddOrRemoveFavoriteResult

    /** User not exist. */
    data object UserNotExists : AddOrRemoveFavoriteResult

    /** Content not exist. */
    data object ContentNotExists : AddOrRemoveFavoriteResult

    /** Content already in favorite for this user. */
    data object AlreadyInFavorite : AddOrRemoveFavoriteResult

    /** Content not in favorite for this user. */
    data object NotInFavorite : AddOrRemoveFavoriteResult

    /** Error. */
    class Error(val t: Throwable) : AddOrRemoveFavoriteResult
}