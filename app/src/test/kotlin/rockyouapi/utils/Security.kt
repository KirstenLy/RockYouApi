package rockyouapi.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import rockyouapi.base.DEFAULT_SECRET_WORD

/** Get default JWT encryption.*/
internal fun getJWTDecryptByDefaultSecretWord() = JWT
    .require(Algorithm.HMAC256(DEFAULT_SECRET_WORD))
    .build()
