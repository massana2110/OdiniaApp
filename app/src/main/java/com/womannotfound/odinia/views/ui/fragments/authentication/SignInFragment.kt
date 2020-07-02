package com.womannotfound.odinia.views.ui.fragments.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentSignInBinding
import com.womannotfound.odinia.views.ui.activities.AuthenticationActivity
import com.womannotfound.odinia.views.ui.activities.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class SignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSignInBinding>(
            inflater,
            R.layout.fragment_sign_in,
            container,
            false
        )

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        //sign up fragment navigation
        binding.signUpNow.setOnClickListener {
            val signUpFragment: Fragment = SignUpFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragments_container, signUpFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        //sign in with email and pass
        binding.signInButton.setOnClickListener {
            val email = binding.emailInputSignIn.text.toString()
            val password = binding.passwordInputSignIn.text.toString()

            signInWithEmailPassword(email, password)
        }

        callbackManager = CallbackManager.Factory.create();
        //sign in with Facebook
        binding.loginButton.fragment = this
        binding.loginButton.setReadPermissions("email", "public_profile")
        binding.loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("SignInFacebook", "facebook:onSuccess:$loginResult")
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {
                Log.d("SignInFacebook", "facebook:onError", error)

            }
        })
        return binding.root
    }

    private fun signInWithEmailPassword(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Error: Campos vacios", Toast.LENGTH_SHORT).show()
            return
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("SignIn", "SignInWithEmail: success")
                        navigateHome()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Algo salio mal. Intenta nuevamente",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken){
        Log.d("SignIn", "handleFacebookToken: $token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.d("SignIn", "signInWithCredential:success")

                val user: FirebaseUser? = auth.currentUser

                if (user != null){
                    val uid = user.uid
                    val name = user.displayName.toString()
                    val email = user.email.toString()
                    val photoUrl = user.photoUrl.toString()
                    val provider = "Facebook"

                    val documentReference: DocumentReference =
                        db.collection("users").document(uid)

                    val fbuser = hashMapOf(
                        "username" to name,
                        "email" to email,
                        "photoUrl" to photoUrl,
                        "provider" to provider
                    )

                    documentReference.set(fbuser).addOnSuccessListener {
                        Log.d(
                            "SingInFragment",
                            "DocumentSnapshot successfully written!"
                        )
                    }.addOnFailureListener { e ->
                        Log.w(
                            "SignInFragment",
                            "Error writing document",
                            e
                        )
                    }
                }


                navigateHome()
            } else {
                Toast.makeText(requireContext(), "Authentication with FB failed.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateHome() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
