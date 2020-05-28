package com.womannotfound.odinia.views.ui.fragments.controls

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentSettingsSetPinBinding

/**
 * A simple [Fragment] subclass.
 */
class SettingsSetPinFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =DataBindingUtil.inflate<FragmentSettingsSetPinBinding>(inflater,
        R.layout.fragment_settings_set_pin, container, false)

        binding.btnAdd.setOnClickListener{

            val password = binding.editText.text.toString()

            it.findNavController().navigate(SettingsSetPinFragmentDirections.actionNavSettingsSetPinFragmentToNavSettings(password))

        }

        return binding.root
    }

}
