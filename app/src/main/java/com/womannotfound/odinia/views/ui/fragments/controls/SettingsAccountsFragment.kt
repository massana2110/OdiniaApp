package com.womannotfound.odinia.views.ui.fragments.controls

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentSettingsAccountsBinding

/**
 * A simple [Fragment] subclass.
 */
class SettingsAccountsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =DataBindingUtil.inflate<FragmentSettingsAccountsBinding>(inflater,
        R.layout.fragment_settings_accounts, container, false)

        binding.btnAdd.setOnClickListener{

            val name = binding.editText.text.toString()
            val type = binding.spinnerAccounts2.selectedItem.toString()
            val balance = binding.inputAmountMoney.text.toString()

            it.findNavController().navigate(SettingsAccountsFragmentDirections.actionNavSettingsAccountsFragmentToNavSettings(name,type,balance))

        }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.types_of_accounts,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerAccounts2.adapter = adapter
        }


        return binding.root
    }

}
