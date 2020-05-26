package com.womannotfound.odinia.views.ui.fragments.operations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentAccountFormBinding

class AccountFormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAccountFormBinding>(inflater,R.layout.fragment_account_form,container,false)

        binding.btnAdd.setOnClickListener{
            var name = binding.textEntryNameAcc.text.toString()
            var type = binding.accType.selectedItem.toString()
            var balance = binding.balanceEntry.text.toString()

            it.findNavController().navigate(AccountFormFragmentDirections.actionNavFormAccountToNavHome(name,type,balance))
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.types_of_accounts,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.accType.adapter = adapter
        }
        
        return binding.root
    }

}
