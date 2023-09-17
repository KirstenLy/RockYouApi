package database.test

import rockyouapi.DBTest

internal fun DBTest.isRecordInContentRegisterExist(contentRegisterID: Int): Boolean {
    return contentRegisterSelectQueries
        .checkIfRecordExist(contentRegisterID)
        .executeAsOneOrNull() != null
}

internal fun DBTest.deleteFromContentRegisterByID(contentRegisterID: Int) {
    contentRegisterDeleteQueries.deleteContentRegister(contentRegisterID)
}