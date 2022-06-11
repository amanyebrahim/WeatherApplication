package com.example.weatherapplication.utils


import android.content.ContextWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity


/**
 * ViewExtension
 */

/**
 * Get parent activity from given view.
 */
fun View.getParentActivity(): AppCompatActivity? {
    var context = this.context

    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }

        context = context.baseContext
    }

    return null
}
fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}
