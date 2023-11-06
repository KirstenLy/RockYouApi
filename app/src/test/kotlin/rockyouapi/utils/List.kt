package rockyouapi.utils

import org.junit.jupiter.api.Assertions

/** For every pair check is [Pair.first] == [Pair.second] and fail is any not.*/
internal fun <A, B> List<Pair<A, B>>.assertAllLeftEqualsTheirRight() {
    forEach { (left, right) -> Assertions.assertEquals(left, right) }
}