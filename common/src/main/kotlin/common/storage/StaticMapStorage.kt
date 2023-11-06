package common.storage

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/** Simple realisation of static key - value holder. */
object StaticMapStorage {

    private val data: HashMap<String, Any> = hashMapOf()

    private val getOrCreateMutex = Mutex()

    suspend fun <T> getOrCreateValue(key: String, createValueAction: suspend () -> T) : T = getOrCreateMutex.withLock {
        val existedObject = data[key]
        if (existedObject != null) {
            return@withLock existedObject as T
        } else {
            val createdObject = createValueAction()
            data[key] = createdObject as Any
            return@withLock createdObject
        }
    }
}