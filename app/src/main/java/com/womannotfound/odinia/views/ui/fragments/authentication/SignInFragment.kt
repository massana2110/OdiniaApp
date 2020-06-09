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
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentSignInBinding
import com.womannotfound.odinia.views.ui.activities.AuthenticationActivity
import com.womannotfound.odinia.views.ui.activities.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class SignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val callbackManager = CallbackManager.Factory.create()

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
        binding.signUpNow.setOnClickListener {
            val signUpFragment: Fragment = SignUpFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragments_container, signUpFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        binding.signInButton.setOnClickListener {
            val email = binding.emailInputSignIn.text.toString()
            val password = binding.passwordInputSignIn.text.toString()

            signInWithEmailPassword(email, password)
        }

        binding.loginButton.setOnClickListener {

            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        result?.let {
                            val token = it.accessToken

                            val credential = FacebookAuthProvider.getCredential(token.token)

                            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                                if (it.isSuccessful){
                                    navigateHome(it.result?.user?.email?: "")
                                } else {
                                    Toast.makeText(requireContext(), "Hubo un problema de autenticacion", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    }

                    override fun onCancel() {

                    }

                    override fun onError(error: FacebookException?) {
                        Toast.makeText(requireContext(), "Hubo un problema", Toast.LENGTH_SHORT)
                            .show()
                    }

                })
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
        }
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
                        navigateHome(task.result?.user?.email ?: "")
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

    private fun navigateHome(email: String) {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
