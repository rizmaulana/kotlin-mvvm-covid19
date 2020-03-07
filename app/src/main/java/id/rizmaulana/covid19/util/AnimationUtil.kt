package id.rizmaulana.covid19.util

import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

class AnimationUtil internal constructor() {

    class AnimationListenerAdapter : Animation.AnimationListener {

        override fun onAnimationStart(animation: Animation) {}

        override fun onAnimationEnd(animation: Animation) {}

        override fun onAnimationRepeat(animation: Animation) {}
    }

    companion object {

        val LINEAR_INTERPOLATOR: Interpolator = LinearInterpolator()
        val FAST_OUT_SLOW_IN_INTERPOLATOR: Interpolator = FastOutSlowInInterpolator()
        val DECELERATE_INTERPOLATOR: Interpolator = DecelerateInterpolator()

        internal fun lerp(startValue: Float, endValue: Float, fraction: Float): Float {
            return startValue + fraction * (endValue - startValue)
        }

        internal fun lerp(startValue: Int, endValue: Int, fraction: Float): Int {
            return startValue + Math.round(fraction * (endValue - startValue).toFloat())
        }
    }

}
