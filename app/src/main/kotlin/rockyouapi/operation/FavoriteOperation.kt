package rockyouapi.operation

/** ENUM to define what user can do with favorite entity. */
internal enum class FavoriteOperation(val operationArgument: Int) {
    ADD(0),
    REMOVE(1)
}