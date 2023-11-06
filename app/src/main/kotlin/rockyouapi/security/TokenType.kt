package rockyouapi.security

/** Types of token. Using for authentication.*/
internal enum class TokenType(val typeID: Int) {
    ACCESS(0),
    REFRESH(1)
}