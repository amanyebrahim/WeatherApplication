package com.example.weatherapplication.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import com.example.weatherapplication.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator


/**
 * ActivityExtension
 *
 */


/**
 * Show message in dialog with two buttons positive and cancel.
 *
 * @param message  The message to display in dialog.
 * @param positiveStringId The string resource id for positive button.
 * @param positiveClicked Method called when positive button clicked.
 */
fun Activity.showMessageTwoButtonsDialog(
    message: String?,
    positiveClicked: () -> Unit
) {
    message?.let { nonNullMessage ->
        val activity = this

        activity.let {
            // Inflate layout for dialog.
            val builder: AlertDialog.Builder = AlertDialog.Builder(it)
            val inflater = it.layoutInflater
            val view = inflater.inflate(R.layout.dialog_message_two_buttons, null)

            // Get dialog views.
            val messageTV = view.findViewById<AppCompatTextView?>(R.id.tv_message)
            val positiveBT = view.findViewById<MaterialButton?>(R.id.bt_positive)
            val cancelBT = view.findViewById<MaterialButton?>(R.id.bt_cancel)
            val loadingProgressBar =
                view.findViewById<CircularProgressIndicator?>(R.id.progress_loading)

            // Fill data.
            messageTV?.text = nonNullMessage.trim()
            loadingProgressBar.makeInVisible()

            // Create the dialog.
            val dialog = builder.setView(view).create()

            // Click events.
            positiveBT?.setOnClickListener {
                positiveClicked()
                dialog.dismiss()
                dialog.cancel()
            }

            cancelBT?.setOnClickListener {
                dialog.dismiss()
            }

            // Set dialog background and show it.
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
    }
}

