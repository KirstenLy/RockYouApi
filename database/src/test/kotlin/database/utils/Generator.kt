package database.utils

import kotlin.random.Random

internal fun generateNotExistedContentID() = Random.nextInt(from = 1000, until = Int.MAX_VALUE)

internal fun generateNotExistedUserID() = Random.nextInt(from = 100, until = Int.MAX_VALUE)

internal fun generateNotExistedEntityIDList() = List(10) { (10000..100000).random() }