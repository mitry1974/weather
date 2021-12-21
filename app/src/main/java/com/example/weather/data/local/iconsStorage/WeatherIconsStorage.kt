package com.example.weather.data.local.iconsStorage

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.weather.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import javax.inject.Inject


class WeatherIconsStorage @Inject constructor(
    private val context: Context,
    private val subPath: String,
) {
    private val allWeatherIcons = mutableMapOf<String, String>()

    init {
        loadIconsFromFile()
    }

    private fun loadIconsFromFile() {
        val listAllFiles = getIconsPath().listFiles()

        if (listAllFiles != null && listAllFiles.isNotEmpty()) {
            for (currentFile in listAllFiles) {
                if (currentFile.name.endsWith(".png")) {
                    allWeatherIcons[currentFile.nameWithoutExtension] = currentFile.absolutePath
                }
            }
        }
    }

    private fun getIconsPath(): File {
        val fullPath = File(context.filesDir, subPath)
        if (!fullPath.exists()) {
            fullPath.mkdir()
        }
        return fullPath
    }

    private suspend fun loadIconFromApi(iconName: String): Bitmap? {
        val urlString = context.getString(R.string.URL_ICONS).replace("{icon}", iconName, false)

        val inputStream: InputStream =
            URL(urlString).openStream() // Download Image from URL

        val bmp = BitmapFactory.decodeStream(inputStream) // Decode Bitmap
        inputStream.close()
        return bmp
    }

    suspend fun getWeatherIconFileName(icon: String?): String? {
        if (icon.isNullOrBlank()) return ""

        if (!allWeatherIcons.containsKey(icon)) {
            val iconFileName = File(getIconsPath(),  "$icon.png").absolutePath
            val iconBitmap = loadIconFromApi(icon)
            iconBitmap?.let {
                saveBitmap(iconFileName, it)
                allWeatherIcons[icon] = iconFileName
            }
        }

        return allWeatherIcons[icon]
    }

//    private fun saveIcon(icon: String, image: ByteArray): String {
//        val file = File(getIconsPath(), "$icon.png")
//        val fOut = FileOutputStream(file)
//        fOut.use { out ->
//            out.write(image)
//            out.flush()
//        }
//        allWeatherIcons[icon] = file.absolutePath
//        return file.absolutePath
//    }
//
//
//
    fun saveBitmap(filename: String, image: Bitmap) {
        FileOutputStream(filename).use {
            image.compress(Bitmap.CompressFormat.PNG, 100, it)
            it.flush()
            it.close()
        }
    }

}