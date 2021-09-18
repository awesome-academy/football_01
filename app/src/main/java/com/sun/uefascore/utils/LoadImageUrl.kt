package com.sun.uefascore.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.net.HttpURLConnection
import java.net.URL

class LoadImageUrl(private val functionSetBitMap: (Bitmap) -> Unit) :
    AsyncTask<String, Unit, Bitmap?>() {

    override fun doInBackground(vararg params: String?): Bitmap? =
        try {
            val bitmap: Bitmap?
            val url = URL(params[0])
            val httpURLConnection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = METHOD_GET
                readTimeout = TIME_OUT
                connectTimeout = TIME_OUT
                doOutput = false
                connect()
            }
            bitmap = BitmapFactory.decodeStream(url.openStream())
            httpURLConnection.disconnect()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        result?.let {
            functionSetBitMap(it)
        }
    }

    companion object {
        private const val TIME_OUT = 20000
        private const val METHOD_GET = "GET"
    }
}
