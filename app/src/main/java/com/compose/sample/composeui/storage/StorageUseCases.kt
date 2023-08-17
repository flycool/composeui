package com.compose.sample.composeui.storage

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.io.OutputStream


// Downloading a file to internal storage
fun downloadFile(context: Context) {
    val CONFIG_URL = "https://ww.baidu.com"
    val client = OkHttpClient()
    val request = Request.Builder().url(CONFIG_URL).build()

    /**
     * By using .use() method will close any underlying network socket
     * automatically at the end of lambdas to avoid memory leaks
     */
    client.newCall(request).execute().use { response ->
        response.body?.byteStream()?.use { input ->
            // using context.filesDir data will be stored into app's internal storage
            val target = File(context.filesDir, "user-config.json")

            target.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
}

// Store Files based on available location
fun findAvailableStorageFile(context: Context): File {
    val file_size = 500_000_000L // 500MB

    // check if filesDir has usable space bigger than our file size,
    // if not we can check into app's external storage directory.
    val target = if (context.filesDir.usableSpace > file_size) {
        context.filesDir
    } else {
        context.getExternalFilesDirs(null).find { externalStorage ->
            externalStorage.usableSpace > file_size
        }
    } ?: throw IOException("Not Enough Space")

    // create and save the file based on the target
    val file = File(target, "big-file.asset")
    return file
}

// Add image to shared storage
fun saveMediaToStorage(context: Context, bitmap: Bitmap) {
    val filename = "BILL_FILE_NAME" + "_${System.currentTimeMillis()}.jpg"

    var fos: OutputStream? = null

    // > android 10
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context.contentResolver?.also { resolver ->
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DCIM + "/BILL_FILE_DIR"
                )
            }

            // Inserting the contentValues to contentResolver
            // and getting the Uri
            val imageUri: Uri? = resolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )

            // Opening an output stream with the Uri that we got
            fos = imageUri?.let { resolver.openOutputStream(it) }
        }
    } else {
        val imageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DCIM + "/BILL_FILE_DIR"
        )
        if (!imageDir.exists()) {
            imageDir.mkdir()
        }

        val image = File(imageDir, filename)
        fos = image.outputStream()

        // request the media scanner to scan the files
        // at the specified path with a callback
        MediaScannerConnection.scanFile(
            context,
            arrayOf(image.toString()),
            arrayOf("image/jpeg"),
        ) { path, uri ->
            Log.d("Media Scanner", "new Image - $path || $uri ")
        }
    }

    fos?.use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, it)
    }
}

// Select a file with the document picker
@Composable
fun SelectFileWithDocumentPicker(context: Context) {
    val documentPick = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        context.contentResolver.openInputStream(uri)?.use {
            // we can copy the file content, you can refer to above code
            // to save that content to file or use it other way.
        }
    }

    // usage: launch our intent-handler with MIME type of PDF
    documentPick.launch(arrayOf("application/pdf"))
}
