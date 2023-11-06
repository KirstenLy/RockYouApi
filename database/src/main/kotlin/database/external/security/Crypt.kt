package database.external.security

import at.favre.lib.crypto.bcrypt.BCrypt

/** Variable to define hash difficult level. */
internal const val HASH_DIFFICULT_DEFAULT = 6

/** Crypt and hash password. */
internal fun hashPassword(password: String) = BCrypt.withDefaults().hashToString(
    HASH_DIFFICULT_DEFAULT,
    password.toCharArray()
)