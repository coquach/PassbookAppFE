package com.se104.passbookapp.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.UUID
import kotlin.apply
import kotlin.io.copyTo
import kotlin.io.outputStream
import kotlin.io.readBytes
import kotlin.io.use
import kotlin.let
import kotlin.text.startsWith

object ImageUtils {
    fun getImagePart(context: Context, imageUrl: Uri?): MultipartBody.Part? {
        val imageFile = if (imageUrl != null && !imageUrl.toString().startsWith("https")) {
            getFileFromUri(context, imageUrl)
        } else {
            null
        }

        return imageFile?.toMultipartBodyPartOrNull()
    }

    /**
     * Chuyển Uri thành File
     */
    private fun getFileFromUri(context: Context, uri: Uri): File {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: throw IllegalArgumentException("Unable to open input stream from URI.")

            val file = File.createTempFile(
                "temp-${System.currentTimeMillis()}-foodapp",
                ".jpg",
                context.cacheDir
            )

            inputStream.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            return file
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Failed to create file from URI", e)
        }
    }
    private fun File?.toMultipartBodyPartOrNull(partName: String = "imageUrl"): MultipartBody.Part? {
        return this?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(partName, it.name, requestFile)
        }
    }

    /**
     * Chuyển Uri thành ByteArray (nếu muốn upload dưới dạng dữ liệu nhị phân)
     */
    fun getBytesFromUri(context: Context, uri: Uri): ByteArray? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            inputStream.readBytes()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun createImageUri(context: Context): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/Camera")
        }

        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }


}
