package com.womannotfound.odinia.views.ui.fragments.authentication

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentSignUpBinding

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var database: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sign_up,
            container,
            false
        )
        val userName = binding.inputNameSignUp.text

        auth = FirebaseAuth.getInstance()
        //userName!!.isNotEmpty()
        binding.signUpButton.setOnClickListener {
            val email = binding.inputEmailSignUp.text.toString()
            val password = binding.passwordInputSignUp.text.toString()

            createAccount(email, password)
        }
        return binding.root
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error. Error: $message")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun createAccount(email: String, password: String){
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Campos vacios", Toast.LENGTH_SHORT).show()
            return
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@addOnCompleteListener
                    }
                    Log.d("SignUpFragment", "Create user with email: success")
                }
                .addOnFailureListener {
                    showAlert(it.message.toString())
                }
        }
    }
}
