package ge.nlatsabidze.walletfluent.extensions

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import ge.nlatsabidze.walletfluent.R

fun ImageView.setImage(url:String?) {
    Glide.with(context).load(url).placeholder(R.drawable.borderlayout).into(this)
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