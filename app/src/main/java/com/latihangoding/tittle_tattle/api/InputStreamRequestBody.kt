package com.latihangoding.tittle_tattle.api

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.IOException

class InputStreamRequestBody(
    private val contentType: MediaType?,
    private val contentResolver: ContentResolver,
    private val uri: Uri,
    private var context: Context,
    private val listener: ProgressListener
) : RequestBody() {

    override fun contentType() = contentType

    override fun contentLength(): Long = -1

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val input = contentResolver.openInputStream(uri)
        val size = DocumentFile.fromSingleUri(context, uri)?.length()
        input?.use {
            var processInputStream = ProcessInputStream(it, size ?: 0, listener)
            sink.writeAll(processInputStream.source())
        } ?: throw IOException("Could not open $uri")

    }

}
