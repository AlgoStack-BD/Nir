package com.algostack.nir.utils

import android.content.Context
import android.graphics.Bitmap
import java.io.File
import java.io.IOException
import android.graphics.BitmapFactory
import android.media.ExifInterface
import coil.decode.DecodeUtils.calculateInSampleSize
import android.graphics.Matrix
import java.io.FileOutputStream

class FileCompressor(context: Context) {

     private var maxWidth = 800
        private var maxHeight = 800
        private var compressFormat = Bitmap.CompressFormat.JPEG
           private var quality = 100
             private var destinationDirectoryPath = ""



    init {
        destinationDirectoryPath = context.cacheDir.path + File.separator.toString() + "images"
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
/*
    @Throws(IOException::class)
    fun compressToBitmap(imageFile: File?): Bitmap {
        return decodeSampledBitmapFromFile(
            imageFile!!,
            maxWidth,
            maxHeight
        )
    }


 */
    @Throws(IOException::class)
    fun compressImage(
        imageFile: File, reqWidth: Int, reqHeight: Int,
        compressFormat: Bitmap.CompressFormat?, quality: Int, destinationPath: String?
    ): File? {
        var fileOutputStream: FileOutputStream? = null
        val file = File(destinationPath).parentFile
        if (!file.exists()) {
            file.mkdirs()
        }
        try {
            fileOutputStream = FileOutputStream(destinationPath)
            if (compressFormat != null) {
              //  decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight)
                   // .compress(compressFormat, quality, fileOutputStream)
            }
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush()
                fileOutputStream.close()
            }
        }
        return File(destinationPath)
    }
/**
    @Throws(IOException::class)
    fun decodeSampledBitmapFromFile(imageFile: File, reqWidth: Int, reqHeight: Int): Bitmap {

        // First decode with inJustDecodeBounds=true to check dimensions
        val options = Bitmap.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFile.absolutePath, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        var scaledBitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)

        val exif = ExifInterface(imageFile.absolutePath)
        val orintation : Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
        val matrix = android.graphics.Matrix()


        when(orintation){
            6 -> matrix.postRotate(90f)
            3 -> matrix.postRotate(180f)
            8 -> matrix.postRotate(270f)
        }

        scaledBitmap = Bitmap.createBitmap(
            scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height,
            matrix, true
        )

        return scaledBitmap



    }
    **/

}