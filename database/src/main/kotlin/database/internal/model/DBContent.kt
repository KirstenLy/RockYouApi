package database.internal.model

import common.utils.generateRandomTextByUUID

/** Content register model to fill test database. */
internal data class DBContent(
    val id: Int,
    val originalContentID: Int? = null,
    val contentTypeID: Int,
    val title: String,
    val description: String = generateRandomTextByUUID(),
    val rating: Int = 0
)