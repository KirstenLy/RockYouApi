package common.utils

private const val BYTE_IN_ONE_KB = 1024
private const val BYTE_IN_ONE_MB = BYTE_IN_ONE_KB * 1024
private const val BYTE_IN_ONE_GB = BYTE_IN_ONE_MB * 1024

/** Bytes -> Megabytes.*/
fun bytesToMegabytes(bytes: Int) = bytes / BYTE_IN_ONE_MB

/** Megabytes -> Bytes.*/
fun megabytesToBytes(megabytes: Int) = megabytes * BYTE_IN_ONE_MB

/** Kilobytes -> Bytes.*/
fun kilobytesToBytes(kilobytes: Int) = kilobytes * BYTE_IN_ONE_KB