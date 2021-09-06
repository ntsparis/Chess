package gr.paris.chess.common.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun Context.getBitmapFromVectorDrawable(
    @DrawableRes drawableId: Int,
    tintColor: Int = Color.RED
): Bitmap? {
    val drawable = ContextCompat.getDrawable(this, drawableId) ?: return null
    DrawableCompat.setTint(drawable, tintColor)
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    ) ?: return null
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}