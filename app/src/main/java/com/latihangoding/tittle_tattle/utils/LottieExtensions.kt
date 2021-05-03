package com.latihangoding.tittle_tattle.utils

import android.animation.Animator
import com.airbnb.lottie.LottieAnimationView

fun LottieAnimationView.play(speed: Float = 1f) {
    this.speed = if (speed < 0) -speed else speed
    this.playAnimation()
}

fun LottieAnimationView.reversePlay(speed: Float = -1f) {
    this.speed = if (speed > 0) -speed else speed
    this.playAnimation()
}

fun LottieAnimationView.setOnAnimationEnd(callback: () -> Unit) {
    addAnimatorListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {}
        override fun onAnimationEnd(animation: Animator?) {
            callback()
        }
        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationRepeat(animation: Animator?) {}
    })
}