package com.womannotfound.odinia.views.ui.fragments.controls

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentSettingsProfileBinding
import com.womannotfound.odinia.views.ui.activities.AuthenticationActivity

/**
 * A simple [Fragment] subclass.
 */
class SettingsProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =DataBindingUtil.inflate<FragmentSettingsProfileBinding>(inflater,
        R.layout.fragment_settings_profile, container, false)


        binding.btnAdd.setOnClickListener{
            val username = binding.editText.text.toString()
            val email = binding.inputEmail.text.toString()

            it.findNavController().navigate(SettingsProfileFragmentDirections.actionNavSettingsProfileFragmentToNavSettings(username,email))
        }

        binding.logOutButton.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            LoginManager.getInstance().logOut()
            val intent = Intent(requireContext(), AuthenticationActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return binding.root
    }

}
