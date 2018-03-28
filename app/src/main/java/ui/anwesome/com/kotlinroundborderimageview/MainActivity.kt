package ui.anwesome.com.kotlinroundborderimageview

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.roundborderimageview.RoundBorderImageView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.stp)
        var x = 0f
        var y = 10f
        for( i in 0..6) {
            val view = RoundBorderImageView.create(this, bitmap)
            view.x = x
            view.y = y
            x += bitmap.width * 1.1f
            if (x >  1000) {
                x = 0f
                y += bitmap.height * 1.1f
            }
        }
    }
}
