package com.app.stories.segmentedprogressbar


/**
 * Created by Tiago Ornelas on 18/04/2020.
 * If you need change animation dynamic
 * **/
open class Segment {

    private var animationProgress: Int = 0

    var animationState: AnimationState = AnimationState.IDLE
        set(value) {
            animationProgress = when (value) {
                AnimationState.ANIMATED -> 100
                AnimationState.IDLE -> 0
                else -> animationProgress
            }
            field = value
        }

    /**
     * Represents possible drawing states of the segment
     */
    enum class AnimationState {
        ANIMATED,
        ANIMATING,
        IDLE
    }

    val progressPercentage: Float
        get() = animationProgress.toFloat() / 100

    fun progress() = animationProgress++
}