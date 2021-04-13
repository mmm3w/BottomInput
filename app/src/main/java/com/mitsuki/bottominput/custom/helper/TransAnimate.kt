package com.mitsuki.bottominput.custom.helper

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator

class TransAnimate(private val referValue: ReferValue) {

    private var lastAnimatorSet: AnimatorSet? = null

    companion object {
        private const val DURATION = 100L
    }

    fun transHeight(targetHeight: Int, companionAnimator: Animator? = null) {
        lastAnimatorSet?.cancel()

        var mainAnimator: ObjectAnimator? = null

        val currentHeight = referValue.currentDisplayHeight

        when {
            targetHeight > currentHeight -> {
                //展示高度增高(展示)
                referValue.updateTargetHeight(targetHeight)
                referValue.updateTargetTranslationY(targetHeight - currentHeight)
                mainAnimator = ObjectAnimator.ofFloat(
                    referValue.translationYTarget(),
                    "translationY",
                    referValue.referTranslationY(),
                    0f
                ).apply {
                    addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            mainAnimator?.removeAllListeners()
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                            mainAnimator?.removeAllListeners()
                        }

                        override fun onAnimationRepeat(animation: Animator?) {
                        }

                    })
                }

            }
            targetHeight < currentHeight -> {
                //展示高度减少(隐藏)
                mainAnimator = ObjectAnimator.ofFloat(
                    referValue.translationYTarget(),
                    "translationY",
                    referValue.referTranslationY(),
                    referValue.referHeight() - targetHeight
                ).apply {
                    addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            mainAnimator?.removeAllListeners()
                            referValue.updateTargetHeight(targetHeight)
                            referValue.updateTargetTranslationY(0f)
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                            mainAnimator?.removeAllListeners()
                        }

                        override fun onAnimationRepeat(animation: Animator?) {
                        }

                    })
                }
            }
        }

        if (mainAnimator == null) return

        lastAnimatorSet = AnimatorSet()
            .apply {
                duration = DURATION
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        lastAnimatorSet?.removeAllListeners()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        lastAnimatorSet?.removeAllListeners()
                    }

                    override fun onAnimationRepeat(animation: Animator?) {
                    }
                })
                play(mainAnimator).apply { if (companionAnimator != null) with(companionAnimator) }
                start()
            }
    }
}