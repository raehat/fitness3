package com.example.solanatry2.onboarding

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.solanatry2.MyApplication
import com.example.solanatry2.R
import com.example.solanatry2.databinding.OnBoardingBinding
import com.example.solanatry2.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding : OnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        CoroutineScope(Dispatchers.Default).launch {
            (application as MyApplication).dataStoreManager.isOnBoardingFinished().collect() {
                if (it == true) {
                    startActivity(Intent(this@OnBoardingActivity, LoginActivity::class.java))
                }
            }
        }

        val getStartedButton = binding.getStartedButtonView
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.onboarding_frame_layout, OnBoardingSplashScreen())
            .commit()

        val scaleXAnimator = ObjectAnimator.ofFloat(getStartedButton, "scaleX", 1.0f, 0.5f)
        var buttonShapeChanged = false
        val onBoardingFragments = listOf(OnBoardingFragment1(), OnBoardingFragment2(), OnBoardingFragment3(), OnBoardingFragment4())
        var position = 0

        with(getStartedButton) {
            setOnClickListener {
                if (!buttonShapeChanged) {
                    scaleXAnimator.start()
                    buttonShapeChanged = true

                    animate()
                        .translationXBy(250F)
                        .setDuration(270)
                        .start()
                    with(binding) {
                        getStartedButtonText.text = ""
                        getStartedButtonArrow.visibility = View.VISIBLE
                    }
                }
                if (position == 3) {
                    binding.getStartedButtonArrow.visibility = View.GONE
                    binding.getStartedButtonText.text = "Start App"
                    binding.getStartedButtonText.scaleX = 2.0f
                } else if (position == 4) {
                    val intent = Intent(this@OnBoardingActivity, LoginActivity()::class.java)
                    startActivity(intent)
                    return@setOnClickListener
                }
                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.sliding_in_left, R.anim.sliding_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.onboarding_frame_layout, onBoardingFragments[position])
                    .commit()

                position++
            }
        }

    }
}