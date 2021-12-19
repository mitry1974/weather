package com.example.weather.data.repository.citiesWeather

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class WeatherIconsStorage @Inject constructor(private val context: Context, private val subPath: String) {
    private val allWeatherIcons = mutableMapOf<String, String>()

    init {
        loadIconsFromFile()
    }

    private fun getIconsPath(): File {
        val fullPath = File(context.filesDir, subPath)
        if (!fullPath.exists()) {
            fullPath.mkdir()
        }
        return fullPath
    }

    private fun loadIconsFromFile() {
        imageReader(getIconsPath())
    }

    private fun imageReader(root: File) {
        val listAllFiles = root.listFiles()

        if (listAllFiles != null && listAllFiles.isNotEmpty()) {
            for (currentFile in listAllFiles) {
                if (currentFile.name.endsWith(".png")) {
                    allWeatherIcons[currentFile.nameWithoutExtension] = currentFile.absolutePath
                }
            }
        }
    }

    fun saveIcon(icon: String, image: ByteArray): String {
        val file = File(getIconsPath(), "$icon.png")
        val fOut = FileOutputStream(file)
        fOut.use { out ->
            out.write(image)
            out.flush()
        }
        allWeatherIcons[icon] = file.absolutePath
        return file.absolutePath
    }

    fun getIconFileName(icon: String): String? {
        val fn = allWeatherIcons[icon]
        return fn
    }
}