package database.external

import database.external.contract.ProductionDatabaseAPI
import database.external.contract.TestDatabaseAPI
import database.internal.creator.createProductionDatabaseAPI
import database.internal.creator.createProductionDatabaseWithTestAPI
import org.jetbrains.annotations.TestOnly

/** API of Database feature.*/
object DatabaseFeature {

    /**
     * Init database API.
     * Connect to a filled database.
     * Make migrations if needed.
     * */
    suspend fun connectToProductionDatabase(connectionConfig: DatabaseConfiguration): ProductionDatabaseAPI {
        return createProductionDatabaseAPI(connectionConfig)
    }

    /** Same as [connectToProductionDatabase], but have additional api for test purpose. */
    @TestOnly
    suspend fun connectToProductionDatabaseWithTestApi(connectionConfig: DatabaseConfiguration): Pair<ProductionDatabaseAPI, TestDatabaseAPI> {
        return createProductionDatabaseWithTestAPI(connectionConfig)
    }
}