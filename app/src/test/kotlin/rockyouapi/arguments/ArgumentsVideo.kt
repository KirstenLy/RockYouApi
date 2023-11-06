package rockyouapi.arguments

import java.util.*

internal val searchTextArgumentsVideo
    get() = listOf(
        null,
        "",
        "   ",
        "   Pict",
        "   Vid",
        "Vid",
        "ViDe",
        "deo",
        "deo   ",
        "deo   nUm",
        "Video",
        "video",
        "nu",
        "mbe",
        "mBe",
        "deo numb",
        "Video",
        "ViDeO",
        UUID.randomUUID().toString(),
    )