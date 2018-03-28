package ui.anwesome.com.roundborderimageview

import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 28/03/18.
 */
val INTERVAL = 50L
class AnimatorQueue {
    val runner : Runner = Runner(0)
    var thread : Thread ?= null
    val startcb : () -> Unit = {
        thread = Thread(runner)
        thread?.start()
    }
    val stopcb : () -> Unit = {
        while (true) {
            try {
                thread?.join()
                break
            }
            catch (ex : Exception) {

            }
        }
    }
    fun addView(view : RoundBorderImageView) {
        runner.addView(view, startcb)
    }
    fun pause() {
        if (runner.pause()) {
            stopcb()
        }
    }
    fun resume() {
        runner.resume(startcb)
    }
    companion object {
        private val animatorQueue = AnimatorQueue()
        fun getInstance() : AnimatorQueue = animatorQueue
    }
    data class Runner(var i : Int) : Runnable {
        var running : Boolean = false
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
        fun pause() : Boolean{
            if (running) {
                running = false
                paused = true
            }
            return paused
        }
        fun resume(startcb : () -> Unit) {
            if (!running && paused) {
                paused = false
                running = true
                startcb()
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