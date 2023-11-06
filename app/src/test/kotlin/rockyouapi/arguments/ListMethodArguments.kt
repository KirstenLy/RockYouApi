package rockyouapi.arguments

/** Default arguments for "getEntityList" methods. */
internal class ListMethodArguments(
    val limit: String?,
    val offset: String?,
    val languageID: String?,
    val environmentID: String?
)