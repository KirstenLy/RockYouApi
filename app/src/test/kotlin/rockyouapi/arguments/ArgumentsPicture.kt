package rockyouapi.arguments

import java.util.*

internal val searchTextArgumentsPicture
    get() = listOf(
        null,
        "",
        "   ",
        "   Pict",
        "   Vid",
        "Pict",
        "PiCt",
        "ure",
        "ure   ",
        "ure   nUm",
        "Picture",
        "nu",
        "mbe",
        "mBe",
        "ure numb",
        "Video",
        "ViDeO",
        UUID.randomUUID().toString(),
    )