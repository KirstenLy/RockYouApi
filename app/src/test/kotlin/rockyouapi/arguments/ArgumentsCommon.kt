package rockyouapi.arguments

import java.util.UUID
import kotlin.random.Random

/** Generate valid environmentID argument. */
internal fun generateEnvironmentID() = if (Random.nextBoolean()) Random.nextInt(1, 10) else null

/** Generate valid but not existed contentID argument. */
internal fun generateNotExistedContentID() = Random.nextInt(from = 1000000, until = Int.MAX_VALUE)

// Arguments 1
internal val limitArguments1 get() = listOf("0", "1", "100")
internal val offsetArguments1 get() = listOf("0", "1", "100")
internal val environmentIDArguments1 get() = listOf("0", "1", "100")
internal val langIDListArguments1 get() = listOf("[0]", "[1]", "[1, 100]", "[1, 0, 100]")
internal val authorIDListArguments1 get() = listOf("[0]", "[1]", "[1, 100]", "[1, 0, 100]")
internal val tagIDListArguments1 get() = listOf("[0]", "[1]", "[1, 100]", "[1, 0, 100]")
internal val userIDListArguments1 get() = listOf("[0]", "[1]", "[1, 100]", "[1, 0, 100]")

// Arguments 2
internal val limitArguments2 get() = listOf(null, "5", "10000")
internal val offsetArguments2 get() = listOf(null, "5", "10000")
internal val environmentIDArguments2 get() = listOf(null, "5", "null")
internal val langIDListArguments2 get() = listOf(null, "[2]", "[1000, 5]", "[-100, 5]")
internal val authorIDListArguments2 get() = listOf(null, "[2]", "[1000, 5]", "[-100, 5]")
internal val tagIDListArguments2 get() = listOf(null, "[2]", "[1000, 5]", "[-100, 5]")
internal val userIDListArguments2 get() = listOf(null, "[2]", "[1000, 5]", "[-100, 5]")

// Arguments 3
internal val limitArguments3 get() = listOf("-1", "15", "25")
internal val offsetArguments3 get() = listOf("-1", "15", "25")
internal val environmentIDArguments3 get() = listOf("-1", "15", "25")
internal val langIDListArguments3 get() = listOf("[-1]", "[3]", "[15]", "[25, 15]")
internal val authorIDListArguments3 get() = listOf("[-1]", "[3]", "[15]", "[25, 15]")
internal val tagIDListArguments3 get() = listOf("[-1]", "[3]", "[15]", "[25, 15]")
internal val userIDListArguments3 get() = listOf("[-1]", "[3]", "[15]", "[25, 15]")


// Arguments 4
internal val limitArguments4 get() = listOf("[900", "1", "100")
internal val offsetArguments4 get() = listOf("-900", "1", "100")
internal val environmentIDArguments4 get() = listOf("-900", "1", "100")
internal val langIDListArguments4 get() = listOf("[-900]", "[-1]", "[1, 25]", "[1, -900]")
internal val authorIDListArguments4 get() = listOf("[-900]", "[-1]", "[1, 25]", "[1, -900]")
internal val tagIDListArguments4 get() = listOf("[-900]", "[-1]", "[1, 25]", "[1, -900]")
internal val userIDListArguments4 get() = listOf("[-900]", "[-1]", "[1, 25]", "[1, -900]")


// Arguments 5
internal val limitArguments5 get() = listOf("1", "2", "3")
internal val offsetArguments5 get() = listOf("1", "2", "3")
internal val environmentIDArguments5 get() = listOf("1", "2", "3")
internal val langIDListArguments5 get() = listOf("[1]", "[1, 2]", "[1, 2, 3]", "[1, 2, 3]", "[1, 2, 3 ,4]")
internal val authorIDListArguments5 get() = listOf("[1]", "[1, 2]", "[1, 2, 3]", "[1, 2, 3]", "[1, 2, 3 ,4]")
internal val tagIDListArguments5 get() = listOf("[1]", "[1, 2]", "[1, 2, 3]", "[1, 2, 3]", "[1, 2, 3 ,4]")
internal val userIDListArguments5 get() = listOf("[1]", "[1, 2]", "[1, 2, 3]", "[1, 2, 3]", "[1, 2, 3 ,4]")

// Arguments 6
internal val limitArguments6 get() = listOf("1", UUID.randomUUID().toString(), "3")
internal val offsetArguments6 get() = listOf("1", UUID.randomUUID().toString(), "3")
internal val environmentIDArguments6 get() = listOf("1", UUID.randomUUID().toString(), "3")
internal val langIDListArguments6 get() = listOf("[1]", "[1, 2]", "[1, BROK, 3]", "[1, 2, 3]", "[1, 2, 3 ,4]")
internal val authorIDListArguments6 get() = listOf("[1]", "[1, 2]", "[1, 2, 3]", "[1, BROK, 3]", "[1, 2, 3 ,4]")
internal val tagIDListArguments6 get() = listOf("[]", "[1,]", "[1, 2, 3]", "[1, , 3]", "[1, 2, 3 ,4]")
internal val userIDListArguments6 get() = listOf("[1]", "[1, 2]", "[1, 2, 3]", "[1,       , 3]", "[1, 2, 3 ,4]")