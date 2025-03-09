package top.cha01.geofence.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import androidx.compose.ui.graphics.vector.ImageVector


fun getBitmapFromVector(vectorDrawable: ImageVector, context: Context): Bitmap? {
    val drawable = androidx.compose.ui.graphics.vector.VectorPainter.createVectorDrawable(
        vectorDrawable,
        context
    )
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    } else if (drawable is VectorDrawable) {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
    return null
}