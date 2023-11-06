import org.gradle.api.tasks.testing.Test

/** Default iplementation of applying parallel mode in tests. */
fun Test.applyParallelTest() {
    systemProperties["junit.jupiter.execution.parallel.enabled"] = true
    systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
}