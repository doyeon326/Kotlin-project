package com.doyeon.chapter11.motionintro

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import androidx.constraintlayout.motion.widget.MotionLayout
import com.doyeon.chapter11.motionintro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var isGatheringMotionAnimating: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.scrollView.viewTreeObserver.addOnScrollChangedListener{
            if (binding.scrollView.scrollY > 150f.dpToPx(this).toInt()) {
                if(isGatheringMotionAnimating.not())  {
                    binding.gatheringDigitalThingsLayout.transitionToEnd()
                    binding.buttonShownMotionLayout.transitionToEnd()
                }


            } else {
                if(isGatheringMotionAnimating.not())  {
                    binding.gatheringDigitalThingsLayout.transitionToStart()
                    binding.buttonShownMotionLayout.transitionToStart()
                }
            }
        }

        binding.gatheringDigitalThingsLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
                 isGatheringMotionAnimating = true
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {

            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
               isGatheringMotionAnimating = false
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }

        })
    }


    private fun Float.dpToPx(context: Context): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)
}