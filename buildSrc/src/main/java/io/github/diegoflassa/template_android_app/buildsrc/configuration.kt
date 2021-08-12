package io.github.diegoflassa.template_android_app.buildsrc

import java.io.File
import java.io.FileInputStream
import java.util.*

object Config {
    const val applicationId = "io.github.diegoflassa.template_android_app"
    private const val versionMajor = 1
    private const val versionMinor = 1
    private const val versionPatch = 1
    private var versionClassifier = "debug"
    const val minimumSdkVersion = 24
    const val compileSdkVersion = 31
    const val compileSdkPreviewVersion = "S"
    const val targetSdkVersion = 31
    const val targetSdkPreviewVersion = "S"
    const val buildToolsVersion = "31.0.0"
    val versionCode = buildVersionCode()
    val versionName = buildVersionName()

    private fun buildVersionCode(): Int {
        return minimumSdkVersion * 10000000 + versionMajor * 10000 + versionMinor * 100 + versionPatch
    }

    private fun buildVersionNameWithoutClassifier(): String {
        return "template_android_app"
    }

    @Suppress("Unused")
    fun buildVersionName(): String {
        val versionProps = Properties()
        val versionPropsFile = File("version.properties")
        val fileInputStream = FileInputStream(versionPropsFile)
        fileInputStream.use { fis ->
            if (versionPropsFile.exists()) {
                versionProps.load(fis)
            }
            val code = (versionProps["VERSION_CODE"] ?: "0").toString().toInt() + 1
            versionProps["VERSION_CODE"] = code.toString()
            versionProps.store(versionPropsFile.writer(), null)

            var versionName = buildVersionNameWithoutClassifier()
            if (versionClassifier.isNotEmpty()) {
                //versionName = versionName + "-" + versionClassifier + "-Build:${code}"
            } else {
                versionName += "-Build:${code}"
            }
        }
        return versionName
    }
}
