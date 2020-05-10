package com.womannotfound.odinia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val animation = AnimationUtils.loadAnimation(this, R.anim.animacion)
        iv_logoOdinia.startAnimation(animation)

        //Para pasar a main activity
        val intent = Intent(this,MainActivity::class.java)

        animation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
                //No es necesario
            }

            override fun onAnimationEnd(animation: Animation?) {
                startActivity(intent)
                finish()
            }

            override fun onAnimationStart(animation: Animation?) {
                //No es necesario
            }
        })
    }
}
