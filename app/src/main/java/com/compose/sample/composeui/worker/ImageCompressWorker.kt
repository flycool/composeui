package com.compose.sample.composeui.worker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.use
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.roundToInt

class ImageCompressWorker(
    private val appContext: Context,
    private val params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val uriString = params.inputData.getString(KEY_CONTENT_URI)
            val compressThreshold = params.inputData.getLong(KEY_COMPRESS_THRESHOLD, 0)

            val uri = Uri.parse(uriString)

            val bytes = appContext.contentResolver.openInputStream(uri)?.use {
                it.readBytes()
            } ?: return@withContext Result.failure()


            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            lateinit var  outputBytes: ByteArray
            var quality = 100
            do {
                val outputStream = ByteArrayOutputStream()
                outputStream.use {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                    outputBytes = outputStream.toByteArray()
                    quality -= (quality * 0.1).roundToInt()
                }
            } while (outputBytes.size > compressThreshold && quality > 5)


            val file = File(appContext.cacheDir, "${params.id}.jpg")
            file.writeBytes(outputBytes)
            Result.success(
                workDataOf(
                    KEY_RESULT_PATH to file.absolutePath
                )
            )
        }
    }

    companion object {
        const val KEY_CONTENT_URI = "KEY_CONTENT_URI"
        const val KEY_COMPRESS_THRESHOLD = "KEY_COMPRESS_THRESHOLD"
        const val KEY_RESULT_PATH = "KEY_RESULT_PATH"
    }
}