package ge.nlatsabidze.walletfluent.extensions

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.setImage(url:String?) {
    Glide.with(context).load(url).override(150, 150).into(this)
}