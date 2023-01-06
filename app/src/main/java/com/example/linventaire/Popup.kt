package com.example.linventaire

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils

open class Popup : AppCompatActivity() {

     fun openPopup(background : ConstraintLayout, card : CardView){
        val popup_background = background
        val alpha = 100
        val othercolor = ColorUtils.setAlphaComponent(Color.parseColor("#619dfd"), alpha)
        val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#8b7bff"), alpha)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), othercolor, alphaColor)

        colorAnimation.duration = 700
        colorAnimation.addUpdateListener {
                animator -> popup_background.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()

        val popup_view = card
        popup_view.alpha = 0f
        popup_view.animate().alpha(1f).setDuration(500).setInterpolator(DecelerateInterpolator()).start()

    }

   //animation pour clore la popup
   fun buttonPressed(background : ConstraintLayout, card : CardView) {
      val popup_background = background
      val popup_view = card
      val alpha = 100
      val othercolor = ColorUtils.setAlphaComponent(Color.parseColor("#619dfd"), alpha)
      val alphaColor = ColorUtils.setAlphaComponent(Color.parseColor("#8b7bff"), alpha)
      val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), alphaColor, othercolor)
      colorAnimation.duration = 700
      colorAnimation.addUpdateListener { animator ->
         popup_background.setBackgroundColor(
            animator.animatedValue as Int
         )
      }

      popup_view.animate().alpha(0f).setDuration(500).setInterpolator(DecelerateInterpolator()).start()

      // Fermeture de l'activité après animation
      colorAnimation.addListener(object : AnimatorListenerAdapter() {
         override fun onAnimationEnd(animation: Animator) {
            finish()
            overridePendingTransition(0, 0)
         }
      })
      colorAnimation.start()
   }

}