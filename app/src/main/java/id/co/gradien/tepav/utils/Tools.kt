package id.co.gradien.tepav.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.database.ValueEventListener
import id.co.gradien.tepav.R
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

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

    /**
     * Display a result of [UTF+7]
     * @param time is the Time from UTF+0
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun parseTimeINSTANT(time: String?): String? {
        val f: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.from(ZoneOffset.UTC))
        val parseDate = Instant.from(f.parse(time))
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm")
                .withLocale(Locale.forLanguageTag("in_ID"))
                .withZone(ZoneId.of("Asia/Jakarta"))
        return formatter.format(parseDate)// could be written f.parse(time, Instant::from);
    }

    /**
     * Display a result of converted from Epoch Time to Human-readable Time [HumanTime]
     * @param time is the Epoch Time (Long/Int type data)
     */
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
    fun getDateString(time: Long) : String = dateFormat.format(time)
    fun getDateString(time: Int) : String = dateFormat.format(time)
}