package ge.nlatsabidze.walletfluent.extensions

import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View


fun showDialogError(message: String, context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setMessage(message)
    builder.setPositiveButton("yes") { _: DialogInterface, _: Int -> }
    builder.show()
}

fun View.setOnSafeClickListener(
    onSafeClick: (View) -> Unit
) {
    setOnClickListener(SafeClickListener { v ->
        onSafeClick(v)
    })
}

fun View.setOnSafeClickListener(
    interval: Int,
    onSafeClick: (View) -> Unit
) {
    setOnClickListener(SafeClickListener(interval) { v ->
        onSafeClick(v)
    })
}

fun changeVisibility(views: List<View>, visibility: Int) {
    for (view in views) {
        view.visibility = visibility
    }
}