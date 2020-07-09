package com.womannotfound.odinia.views.ui.fragments.authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentPinAuthenticationBinding

import com.womannotfound.odinia.views.ui.activities.MainActivity
import com.womannotfound.odinia.views.ui.fragments.controls.SettingsSetPinFragmentDirections

class PinAuthenticationFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: FragmentPinAuthenticationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pin_authentication,
            container,
            false
        )


        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val userID = auth.currentUser?.uid.toString()
        var userPinBoolean=""


        db.collection("users").document(userID)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                userPinBoolean = documentSnapshot?.get("userPin").toString()
            }

        binding.btnAdd.setOnClickListener {
            var passwordPin = binding.inputPin.text.toString()

            if (passwordPin == "") {
                Toast.makeText(context, "Proporcione datos validos", Toast.LENGTH_SHORT)
                    .show()
            } else if(userPinBoolean!=passwordPin){
                Toast.makeText(
                    context,
                    "PIN incorrecto",
                    Toast.LENGTH_SHORT
                )
                    .show()
                passwordPin = ""
            }
            else{
                navigateHome()
                Toast.makeText(
                    context,
                    "Bienvenido nuevamente a Odinia",
                    Toast.LENGTH_SHORT
                )
                    .show()
                passwordPin = ""
            }
        }

        return binding.root
    }

    private fun navigateHome() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}