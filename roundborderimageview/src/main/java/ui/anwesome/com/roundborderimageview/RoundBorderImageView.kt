package ui.anwesome.com.roundborderimageview

/**
 * Created by anweshmishra on 28/03/18.
 */
import android.content.*
import android.graphics.*
import android.view.*
class RoundBorderImageView(ctx : Context, var bitmap : Bitmap) : View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas : Canvas) {

    }
    fun update(stopcb : () -> Unit) {
        postInvalidate()
    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class State(var prevScale : Float = 0f, var dir : Float = 0f, var scale : Float = 0f) {
        fun update(stopcb : (Float) -> Unit) {
            scale += 0.1f * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                stopcb(scale)
            }
        }
        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }
    data class RoundBorderImage (var i : Int, var bitmap : Bitmap, val state : State = State()) {
        var resized : Boolean = false
        fun draw(canvas : Canvas, paint : Paint) {
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()
            val size = Math.min(w, h).toInt()
            if (!resized) {
                bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true)
                resized = true
            }
            val r = size.toFloat() / 4
            canvas.save()
            canvas.translate(w/2, h/2)
            canvas.rotate(180f * state.scale)
            val updatedScale = 0.5f + 0.5f * state.scale
            canvas.scale(updatedScale, updatedScale)
            paint.color = Color.WHITE
            canvas.drawCircle(0f, 0f, 6 * r/ 5 , paint)
            canvas.save()
            val path = Path()
            path.addCircle(0f, 0f, r, Path.Direction.CW)
            canvas.clipPath(path)
            canvas.restore()
            canvas.restore()
        }
        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }
}