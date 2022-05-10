package ge.nlatsabidze.walletfluent.extensions

import androidx.appcompat.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ge.nlatsabidze.walletfluent.R
import ge.nlatsabidze.walletfluent.util.SafeClickListener

fun ImageView.setImage(url:String?) {
    Glide.with(context).load(url).placeholder(R.drawable.borderlayout).into(this)
}

fun showDialogError(message: String, context: Context) {
    val builder = AlertDialog.Builder(context)
    builder.setMessage(message)
    builder.setPositiveButton("yes") { _: DialogInterface, _: Int -> }
    builder.show()
}

fun onSnack(view: View, text: String, color: Int) {
    val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
    snackbar.setActionTextColor(Color.BLUE)
    val snackbarView = snackbar.view
    snackbarView.setBackgroundColor(color)
    val textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    textView.textSize = 10f
    snackbar.show()
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

fun ViewGroup.hideAll() {
    this.children.forEach {
        if (it.tag == "error")
            it.visible()
        else
            it.gone()
    }
}
fun ViewGroup.showAll() {
    this.children.forEach {
        it.visible()
    }
}

fun View.visible(): View {
    visibility = View.VISIBLE
    return this
}

fun View.gone(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

fun animateItems(context:Context, text: TextInputEditText, item: TextInputLayout, resources: Resources) {
    val shake: Animation = AnimationUtils.loadAnimation(context, R.anim.vibrate)
    item.startAnimation(shake)
    item.helperText = resources.getString(R.string.invalidField)
    text.setBackgroundResource(R.drawable.border)
}
