package com.algostack.nir.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FileCompressor(context: Context) {

    private var maxWidth = 800
    private var maxHeight = 800
    private var compressFormat = Bitmap.CompressFormat.JPEG
    private var quality = 100
    private var destinationDirectoryPath = ""

    init {
        destinationDirectoryPath =
            context.cacheDir.path + File.separator.toString() + "images"
    }

    @Throws(IOException::class)
    fun compressToFile(imageFile: File): File? {
        return compressToFile(imageFile, imageFile.name)
    }

    @Throws(IOException::class)
    fun compressToFile(imageFile: File?, compressedFileName: String): File? {
        return compressImage(
            imageFile!!,
            maxWidth,
            maxHeight,
            compressFormat,
            quality,
            destinationDirectoryPath + File.separator.toString() + compressedFileName
        )
    }

    @Throws(IOException::class)
    fun compressToBitmap(imageFile: File?): Bitmap {
        return decodeSampledBitmapFromFile(
            imageFile!!,
            maxWidth,
            maxHeight
        )
    }

    @Throws(IOException::class)
    fun compressImage(
        imageFile: File, reqWidth: Int, reqHeight: Int,
        compressFormat: Bitmap.CompressFormat?, quality: Int, destinationPath: String?
    ): File? {
        val parentDir = File(destinationPath!!).parentFile
        if (!parentDir.exists()) {
            parentDir.mkdirs()
        }

        FileOutputStream(destinationPath).use { fileOutputStream ->
            val bitmap = decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight)
            if (compressFormat != null) {
                bitmap.compress(compressFormat, quality, fileOutputStream)
            }
        }

        return File(destinationPath)
    }

    @Throws(IOException::class)
    fun decodeSampledBitmapFromFile(imageFile: File, reqWidth: Int, reqHeight: Int): Bitmap {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(imageFile.absolutePath, options)

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        options.inJustDecodeBounds = false
        var scaledBitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)

        scaledBitmap = rotateImageIfRequired(imageFile, scaledBitmap)

        return scaledBitmap
    }

    private fun rotateImageIfRequired(imageFile: File, bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(imageFile.absolutePath)
        val orientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        val matrix = Matrix()

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                matrix.postRotate(90f)
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                matrix.postRotate(180f)
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                matrix.postRotate(270f)
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            }
            else -> bitmap
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())

            inSampleSize = if (heightRatio < widthRatio) {
                heightRatio
            } else {
                widthRatio
            }
        }

        return inSampleSize
    }
}