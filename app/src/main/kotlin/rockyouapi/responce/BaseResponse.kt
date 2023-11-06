package rockyouapi.responce

import kotlinx.serialization.Serializable

/** Base response model. */
@Serializable
internal data class BaseResponse<T>(val errorText: String?, val data: T?)