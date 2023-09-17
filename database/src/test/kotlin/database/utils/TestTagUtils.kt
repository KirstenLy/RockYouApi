package database.utils

import database.external.test.TestTag

internal fun TestTag.findTranslationByEnvironmentID(environmentID: Byte) = translations.first { it.envID == environmentID }.name