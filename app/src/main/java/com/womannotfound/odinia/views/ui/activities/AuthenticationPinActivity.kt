package com.womannotfound.odinia.views.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.womannotfound.odinia.R
import com.womannotfound.odinia.views.ui.fragments.authentication.PinAuthenticationFragment
import com.womannotfound.odinia.views.ui.fragments.authentication.SignInFragment

class AuthenticationPinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        val fragment = PinAuthenticationFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragments_container, fragment)
        fragmentTransaction.commit()
    }
}