package ui.anwesome.com.roundborderimageview

import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 28/03/18.
 */
val INTERVAL = 50L
class AnimatorQueue {

    companion object {
        private val animatorQueue = AnimatorQueue()
        fun getInstance() : AnimatorQueue = animatorQueue
    }
    data class Runner(var i : Int, var updatecb : () -> Unit) : Runnable {
        var running : Boolean = true
        var paused : Boolean = false
        var views : ConcurrentLinkedQueue<RoundBorderImageView> = ConcurrentLinkedQueue()
        fun addView(view : RoundBorderImageView, startcb : () -> Unit) {
            views.add(view)
            if (!running) {
                running = true
                startcb()
            }
        }
        fun updatecb() {
            views.forEach { view ->
                view.update {
                    views.remove(view)
                    if (views.size == 0) {
                        running = false
                    }
                }
            }
        }
        fun pause() {
            if (running) {
                running = false
                paused = true
            }
        }
        fun resume(startcb : () -> Unit) {
            if (!running && paused) {
                paused = false
                running = true
            }
        }
        override fun run() {
            while (running) {
                try {
                    Thread.sleep(INTERVAL)
                    updatecb()
                }
                catch (ex : Exception) {

                }
            }
        }
    }
}