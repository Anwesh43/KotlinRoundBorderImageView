package ui.anwesome.com.roundborderimageview

/**
 * Created by anweshmishra on 28/03/18.
 */
import android.app.Activity
import android.content.*
import android.graphics.*
import android.view.*
class RoundBorderImageView(ctx : Context, var bitmap : Bitmap) : View(ctx) {
    val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer : Renderer = Renderer(this)
    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }
    fun update(stopcb : () -> Unit) {
        renderer.update(stopcb)
        postInvalidate()
    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
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
            canvas.drawBitmap(bitmap, -r, -r , paint)
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
    data class Renderer (var view : RoundBorderImageView) {
        val roundBorderImage : RoundBorderImage = RoundBorderImage(0, view.bitmap)
        fun render (canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#BDBDBD"))
            roundBorderImage.draw(canvas, paint)
        }
        fun update(stopcb : () -> Unit) {
            roundBorderImage.update {
                stopcb()
            }
        }
        fun handleTap() {
            roundBorderImage.startUpdating {
                AnimatorQueue.getInstance().addView(view)
            }
        }
    }
    companion object {
        fun create(activity : Activity, bitmap : Bitmap) : RoundBorderImageView {
            val view = RoundBorderImageView(activity, bitmap)
            activity.addContentView(view, ViewGroup.LayoutParams(bitmap.width, bitmap.height))
            return view
        }
        fun pause() {
            AnimatorQueue.getInstance().pause()
        }
        fun resume() {
            AnimatorQueue.getInstance().resume()
        }
    }
}