package rockyouapi.operation

/** ENUM to define what user can do with favorite entity. */
internal enum class FavoriteOperation(val operationArgument: Int) {
    REMOVE(0),
    ADD(1),
}