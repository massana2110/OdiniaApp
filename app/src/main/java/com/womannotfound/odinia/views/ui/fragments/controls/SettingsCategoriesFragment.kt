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
import com.womannotfound.odinia.databinding.FragmentSettingsCategoriesBinding

/**
 * A simple [Fragment] subclass.
 */
class SettingsCategoriesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =DataBindingUtil.inflate<FragmentSettingsCategoriesBinding>(inflater,
            R.layout.fragment_settings_categories, container, false)

        binding.btnAdd.setOnClickListener{
            val categoryAdd = binding.editText.text.toString()
            it.findNavController().navigate(SettingsCategoriesFragmentDirections.actionNavSettingsCategoriesFragmentToNavSettings(categoryAdd))
        }

        binding.btnAdd2.setOnClickListener{
            val categoryToEdit = binding.spinnerAccounts.selectedItem.toString()
            val newName = binding.inputNewName.text.toString()
            it.findNavController().navigate(SettingsCategoriesFragmentDirections.actionNavSettingsCategoriesFragmentToNavSettings(categoryToEdit,newName))
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_payments,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerAccounts.adapter = adapter
        }

        return binding.root
    }

}
