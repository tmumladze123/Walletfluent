package ge.nlatsabidze.walletfluent.extensions

import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.content.DialogInterface



fun AlertDialog.Builder.showDialogError(message: String, context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setMessage(message)
    builder.setPositiveButton("yes") { _: DialogInterface, _: Int -> }
    builder.show()
}
