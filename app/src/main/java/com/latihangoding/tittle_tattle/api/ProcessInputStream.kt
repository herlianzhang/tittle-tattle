package com.latihangoding.tittle_tattle.api

import timber.log.Timber
import java.io.InputStream
import java.text.DecimalFormat

class ProcessInputStream(
    private val inputStream: InputStream,
    private val length: Long,
    private val listener: ProgressListener
) : InputStream() {
    private var sumRead: Int = 0
    private var percent: Float = 0f

    override fun read(b: ByteArray): Int {
        val readCount = inputStream.read(b)
        evaluatePercent(readCount.toLong())
        return readCount
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        val readCount = inputStream.read(b, off, len)
        evaluatePercent(readCount.toLong())
        return readCount
    }

    override fun skip(n: Long): Long {
        val skip = inputStream.skip(n)
        evaluatePercent(skip)
        return skip
    }

    override fun read(): Int {
        val read = inputStream.read()
        if (read != -1) {
            evaluatePercent(1)
        }
        return read
    }

    private fun evaluatePercent(readCount: Long) {
        if (readCount != -1L) {
            try {
                sumRead += readCount.toInt()
                val tmp = DecimalFormat("#.##")
                    .format(sumRead * 1f / length)
                    .replace(',', '.')
                    .toFloat()
                if (tmp >= percent + 0.01) {
                    percent = tmp
                    notifyListener()
                }
            } catch (e: Exception) {
                Timber.e("evaluatePercent error cause ${e.message}")
            }
        }
    }

    private fun notifyListener() {
        listener.onProgress(percent)
    }
}
