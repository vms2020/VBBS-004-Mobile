// AndroidLibraryConventionPlugin.kt
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            val libs = the<VersionCatalogsExtension>().named("libs")
            dependencies.add(
                "implementation",
                libs.findLibrary("kotlinx-serialization-json").get().get()
            )
//            dependencies.add("ksp", libs.findLibrary("hilt-compiler").get().get())
            dependencies.add(
                "coreLibraryDesugaring",
                libs.findLibrary("desugar-jdk-libs")
                    .orElseThrow { IllegalStateException("Catalog alias 'desugar_jdk_libs' not found") }
                    .get()   // ← same pattern as your hilt plugin
            )
            extensions.configure<LibraryExtension> {
                compileSdk { version = release(37) }
                defaultConfig { minSdk = 24 }
                compileOptions {
//                sourceCompatibility = JavaVersion.VERSION_17
//                targetCompatibility = JavaVersion.VERSION_17
                    isCoreLibraryDesugaringEnabled = true
                }
            }
        }
    }
}
