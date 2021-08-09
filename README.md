# Kotlinx Serialization Coverage Bug
A Kotlin Multiplatform (Desktop only) project to replicate the kotlinx.serialization coverage bug.

## Replcate Bug
To replicate the bug, just clone this repo and run `gradlew nativeCoverageReport`.

You should get a `BUILD FAILED` and in the logs you should see that the nativeCoverageReport task failed with `Failed to load coverage: Malformed instrumentation profile data`.
