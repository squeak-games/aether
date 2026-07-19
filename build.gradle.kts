plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
}

val forbiddenNetworkLibraries = listOf(
    "okhttp3", "okhttp", "retrofit2", "retrofit",
    "com.squareup.okhttp", "com.squareup.retrofit",
    "ktor", "firebase", "volley",
)

tasks.register("privacyLint") {
    group = "verification"
    description = "Check for forbidden network dependencies in all modules"
    doLast {
        val configs = setOf(
            "compileClasspath",
            "runtimeClasspath",
            "testCompileClasspath",
            "testRuntimeClasspath",
        )
        val violations = mutableListOf<String>()
        allprojects.forEach { p ->
            p.configurations.matching { it.name in configs }.forEach { config ->
                config.resolvedConfiguration.resolvedArtifacts
                    .mapNotNull { it.moduleVersion.id }
                    .forEach { id ->
                        val coord = "${id.group}:${id.name}"
                        if (forbiddenNetworkLibraries.any { coord.contains(it, ignoreCase = true) }) {
                            violations.add("${p.path} [${config.name}]: $coord")
                        }
                    }
            }
        }
        if (violations.isNotEmpty()) {
            throw GradleException(
                "Privacy violation: forbidden network dependencies found:\n" +
                    violations.joinToString("\n")
            )
        }
        logger.lifecycle("privacyLint: PASS — no forbidden network dependencies")
    }
}
