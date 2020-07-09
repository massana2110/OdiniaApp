package com.womannotfound.odinia.views.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.womannotfound.odinia.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val userID = auth.currentUser?.uid.toString()

        var userPinBoolean=""
        db.collection("users").document(userID)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                userPinBoolean = documentSnapshot?.get("userPin").toString()
            }

        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        val animation = AnimationUtils.loadAnimation(this,
            R.anim.animacion
        )
        iv_logoOdinia.startAnimation(animation)

        //Para pasar a main activity
        val intentMainActivity = Intent(this, MainActivity::class.java)
        val intentAuthenticationPinActivity = Intent(this, AuthenticationPinActivity::class.java)
        val intentAuthenticationActivity = Intent(this, AuthenticationActivity::class.java)

        animation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
                //No es necesario
            }

            override fun onAnimationEnd(animation: Animation?) {
                if ((user != null) && (userPinBoolean!="null")){
                    startActivity(intentAuthenticationPinActivity)
                    finish()

                } else if(user != null){
                    startActivity(intentMainActivity)
                    finish()
                }else {
                    startActivity(intentAuthenticationActivity)
                    finish()
                }

            }

            override fun onAnimationStart(animation: Animation?) {
                //No es necesario
            }

        })

    }
}
