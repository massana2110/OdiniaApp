package com.womannotfound.odinia.views.ui.fragments.controls

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentSettingsBinding



/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= DataBindingUtil.inflate<FragmentSettingsBinding>(inflater,
            R.layout.fragment_settings, container, false)

        binding.textView.setOnClickListener{
            it.findNavController().navigate(R.id.action_nav_settings_to_settingsProfileFragment)
        }
        binding.textView2.setOnClickListener{
            it.findNavController().navigate(R.id.action_nav_settings_to_settingsProfileFragment)
        }
        binding.textView3.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_settings_to_settingsAccountsFragment)
        }
        binding.textView4.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_settings_to_settingsAccountsFragment)
        }
        binding.textView5.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_settings_to_settingsCategoriesFragment)
        }
        binding.textView6.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_settings_to_settingsCategoriesFragment)
        }
        binding.textView7.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_settings_to_settingsNotificationCenterFragment)
        }
        binding.textView8.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_settings_to_settingsNotificationCenterFragment)
        }
        binding.textView9.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_settings_to_settingsSetPinFragment)
        }
        binding.textView10.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_settings_to_settingsSetPinFragment)
        }
        return binding.root
    }

}
