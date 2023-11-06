package rockyouapi.base

import common.storage.StaticMapStorage.getOrCreateValue
import database.external.DatabaseFeature
import database.external.contract.ProductionDatabaseAPI
import database.external.contract.TestDatabaseAPI
import database.external.reader.readDatabaseConfigurationFromEnv
import kotlinx.coroutines.runBlocking

internal class DatabaseApiDelegateImpl : DatabaseApiDelegate {

    private val databaseAPI by lazy {
        runBlocking {
            getOrCreateValue(KEY_STATIC_MAP_DB) {
                DatabaseFeature.connectToProductionDatabaseWithTestApi(readDatabaseConfigurationFromEnv())
            }
        }

    }

    override val productionDatabaseAPI get() = databaseAPI.first

    override val testDatabaseAPI: TestDatabaseAPI = databaseAPI.second
}

internal interface DatabaseApiDelegate {
    val testDatabaseAPI: TestDatabaseAPI
    val productionDatabaseAPI: ProductionDatabaseAPI
}