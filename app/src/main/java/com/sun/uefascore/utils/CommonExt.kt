package com.sun.uefascore.utils

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.Fragment

fun ImageView.loadImageUrl(url: String) {
    LoadImageUrl {
        this.setImageBitmap(it)
    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url)
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
