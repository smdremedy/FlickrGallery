package pl.szkoleniaandroid.flickrgallery.binding

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.databinding.BindingAdapter
import coil.api.load

@BindingAdapter("imageUrl")
fun ImageView.imageUrl(url: String) {
    this.load(url)
}

@BindingAdapter("html")
fun TextView.setHtml(html: String) {
    this.text = HtmlCompat.fromHtml(html, FROM_HTML_MODE_LEGACY)
}

@BindingAdapter("visible")
fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}