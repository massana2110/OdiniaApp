package com.womannotfound.odinia.views.ui.fragments.authentication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentSignInBinding
import com.womannotfound.odinia.views.ui.activities.AuthenticationActivity
import com.womannotfound.odinia.views.ui.activities.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSignInBinding>(inflater,R.layout.fragment_sign_in, container, false)

        binding.signUpNow.setOnClickListener {
            val signUpFragment: Fragment = SignUpFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragments_container, signUpFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        binding.signInButton.setOnClickListener {
            val intent = Intent(requireContext(),
                MainActivity::class.java)

            startActivity(intent)
            activity?.finish()
        }
        return binding.root
    }

}
