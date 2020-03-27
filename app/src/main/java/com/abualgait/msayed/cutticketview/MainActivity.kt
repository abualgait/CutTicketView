package com.abualgait.msayed.cutticketview

import android.animation.AnimatorSet
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var interact = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        interactive.setOnCheckedChangeListener { compoundButton, b ->
            interact = compoundButton.isChecked
        }

        bottomTicketView.setOnTouchListener { view, motionEvent ->

            when (motionEvent.action) {

                MotionEvent.ACTION_MOVE -> {
                    val set = AnimatorSet()
                    val translateView =
                            getValueAnimator(true, DURATION, AccelerateInterpolator()) { progress ->
                                view.translationY = progress * container.height
                            }

                    if (interact) {
                        //enable interactive cutting with finger movement
                        val x = motionEvent.rawX
                        bottomTicketView.setSkew(0.0f, -(x / (bottomTicketView.width) * 100) / 1000)
                        cutter.translationX = ((x / (bottomTicketView.width) * 100) / 100 * view.width)
                        set.play(translateView)
                        set.doOnEnd {
                            //reset view
                            bottomTicketView.setSkew(0.0f, 0.0f)
                            view.translationY = 0f
                            cutter.translationX = bottomTicketView.x - 25
                        }
                        if (x > bottomTicketView.x + bottomTicketView.width) {
                            set.start()
                        }
                    } else {
                        //using animation as one time shot.
                        val skewAnimation =
                                getValueAnimator(true, DURATION, AccelerateInterpolator()) { progress ->
                                    bottomTicketView.setSkew(0.0f, -progress / 10)
                                }

                        val translateCutter =
                                getValueAnimator(false, DURATION, AccelerateInterpolator()) { progress ->
                                    cutter.translationX = view.width - (progress * view.width)

                                }

                        set.play(translateCutter).with(skewAnimation).before(translateView)
                        set.doOnEnd {

                            //reset view
                            bottomTicketView.setSkew(0.0f, 0.0f)
                            view.translationY = 0f
                            cutter.translationX = bottomTicketView.x - 25
                        }
                        set.start()
                    }


                }

                else -> bottomTicketView.rotation = 180f
            }
            true
        }


    }

    companion object {
        val DURATION = 2000L
    }

}

fun getValueAnimator(
        forward: Boolean = true,
        duration: Long,
        interpolator: TimeInterpolator,
        updateListener: (progress: Float) -> Unit
): ValueAnimator {
    val a =
            if (forward) ValueAnimator.ofFloat(0f, 1f)
            else ValueAnimator.ofFloat(1f, 0f)
    a.addUpdateListener { updateListener(it.animatedValue as Float) }
    a.duration = duration
    a.interpolator = interpolator
    return a
}
