package id.co.gradien.tepav.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.database.ValueEventListener
import id.co.gradien.tepav.R

object Tools : AppCompatActivity() {
    /**
     * Display a simple [AlertDialog] with a simple OK button.
     * If the dismiss listener is specified, the dialog becomes uncancellable
     * @param context The context
     * @param title The title string
     * @param message The message string
     * @param dismissListener The dismiss listener
     */
    fun showMessageDialog(
        context: Context, title: String?, message: String?,
        dismissListener: DialogInterface.OnDismissListener? = null
    ) {
        val builder = AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.ok, { dialog, _ -> dialog.dismiss() })

        val dialog = builder.create()
        if (dismissListener != null) {
            dialog.setOnDismissListener(dismissListener)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
        }
        dialog.show()
    }

    /**
     * Display a [SweetAlertDialog]
     * @param context The context
     * @param title The title string
     * @param message The message string
     * @param alertType The alert type int
     */
    fun showPopUpDialog(
        context: Context,
        title: String,
        message: String,
        alertType: Int = SweetAlertDialog.SUCCESS_TYPE
    ) {
        val dialog = SweetAlertDialog(context, alertType)
        dialog.apply {
            titleText = title
            contentText = message
            confirmText = "OK"
            setConfirmClickListener(null)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }
        dialog.show()
    }

    fun showPopUpDialogWaring(
        context: Context,
        title: String,
        message: String,
        alertType: Int = SweetAlertDialog.WARNING_TYPE
    ) {
        val dialog = SweetAlertDialog(context, alertType)
        dialog.apply {
            titleText = title
            contentText = message
            confirmText = "OK"
            setConfirmClickListener(null)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }
        dialog.show()
    }
}