package com.example.todoapp.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object AlertDialog {

    private lateinit var alertDialog: AlertDialog

    private fun cantShowDialog(): Boolean =
        this::alertDialog.isInitialized && alertDialog.isShowing


    fun showDialogConfirmation(context: Context, title: String, msg: String, callback: () -> Unit) {

        if (cantShowDialog()) {
            return
        }

        alertDialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(msg)
            .setCancelable(true)
            .setNegativeButton("No", null)
            .setPositiveButton("Yes") { _, _ ->
                callback()
            }

            .create()
            .apply {
                setCanceledOnTouchOutside(false)
                show()
            }
    }
}


