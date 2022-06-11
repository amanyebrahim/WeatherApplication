package com.example.weatherapplication.utils

import android.view.View
import android.widget.*
import androidx.annotation.Nullable
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData


/**
 * BindingAdapters
 *
 */

//region Binding properties
/**
 * Set [View] properties.
 */
@BindingAdapter(
    "mutableVisibility",
    requireAll = false
)
fun bindingAdapterView(
    view: View,
    @Nullable visibility: LiveData<Int?>?,
) {
    view.getParentActivity()?.let { parentActivity ->
        visibility?.let { visibility ->
            visibility.observe(
                parentActivity
            ) { value -> value?.let { view.visibility = it } }
        }
    }
}
/**
 * Set [TextView] properties.
 */
@BindingAdapter(
    "mutableText",
    "mutableTextId",
    requireAll = false
)
fun bindingAdapterTextView(
    view: TextView,
    @Nullable text: LiveData<String?>?,
    @Nullable textId: LiveData<Int?>?,
) {
    view.getParentActivity()?.let { parentActivity ->
        text?.let { text ->
            text.observe(parentActivity) { value -> value?.let { view.text = it } }
        }

        textId?.let { textId ->
            textId.observe(parentActivity) { value -> value?.let { view.setText(it) } }
        }

    }
}
//endregion
