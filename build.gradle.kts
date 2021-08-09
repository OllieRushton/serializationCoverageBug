plugins {
    kotlin("multiplatform") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
}

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "serializationCoverageBug.main"
            }
        }

        binaries.getTest("DEBUG").apply {
            freeCompilerArgs += listOf("-Xlibrary-to-cover=${compilations["main"].output.classesDirs.singleFile.absolutePath}")
        }
    }

    
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val nativeMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
            }
        }
        val nativeTest by getting
    }

    tasks.create("nativeCoverageReport") {
        dependsOn("nativeTest")

        description = "Create native coverage report"

        doLast {
            val testDebugBinary = nativeTarget.binaries.getTest("DEBUG").outputFile
            exec {
                commandLine("llvm-profdata", "merge", "$testDebugBinary.profraw", "-o", "$testDebugBinary.profdata")
            }
            exec {
                commandLine("llvm-cov", "show", "$testDebugBinary", "-instr-profile", "$testDebugBinary.profdata", "-format", "html", "-output-dir", "$projectDir")
            }
        }
    }
}
