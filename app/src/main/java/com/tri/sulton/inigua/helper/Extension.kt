package com.tri.sulton.inigua.helper

import android.animation.ObjectAnimator
import android.view.View

fun View.fadeIn(duration: Long = 500): ObjectAnimator {
    return ObjectAnimator.ofFloat(this, View.ALPHA, 1f).setDuration(duration)
}