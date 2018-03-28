package ui.anwesome.com.kotlinroundborderimageview

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Point
import android.hardware.display.DisplayManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import ui.anwesome.com.roundborderimageview.RoundBorderImageView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.stp)
        val displayManager : DisplayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        val display : Display = displayManager.getDisplay(0)
        val size : Point = Point()
        display?.getRealSize(size)
        val w = size.x
        var x = 0f
        var y = 10f
        for( i in 0..3) {
            val view = RoundBorderImageView.create(this, bitmap)
            view.x = x
            view.y = y
            x += bitmap.width * 1.1f
            if (x + w >  w) {
                x = 0f
                y += bitmap.height * 1.1f
            }
        }
    }
}
