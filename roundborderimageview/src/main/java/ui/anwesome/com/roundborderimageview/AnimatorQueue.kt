package ui.anwesome.com.roundborderimageview

import android.view.View
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Created by anweshmishra on 28/03/18.
 */
class AnimatorQueue {
    var views : ConcurrentLinkedQueue<RoundBorderImageView> = ConcurrentLinkedQueue()
    fun addView(view : RoundBorderImageView) {
        views.add(view)
    }
    var updatecb : () -> Unit =  {
        views.forEach { view ->
            view.update {
                views.remove(view)
                if (views.size == 0) {

                }
            }
        }
    }
    companion object {
        private val animatorQueue = AnimatorQueue()
        fun getInstance() : AnimatorQueue = animatorQueue
    }
}