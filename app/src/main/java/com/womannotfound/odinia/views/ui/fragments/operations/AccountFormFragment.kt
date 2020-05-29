package com.womannotfound.odinia.views.ui.fragments.operations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentAccountFormBinding
import com.womannotfound.odinia.viewmodel.AccountsViewModel


class AccountFormFragment : Fragment() {
    private lateinit var vm: AccountsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAccountFormBinding>(inflater,R.layout.fragment_account_form,container,false)

        vm = activity?.run {
            ViewModelProvider(this, defaultViewModelProviderFactory).get(AccountsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        binding.btnAdd.setOnClickListener{
            vm.name = binding.textEntryNameAcc.text.toString()
            vm.type= binding.accType.selectedItem.toString()
            vm.balance = binding.balanceEntry.text.toString()

            if( (vm.type == "" && vm.balance == "")){
                Toast.makeText(context,"Proporcione datos validos", Toast.LENGTH_SHORT).show()
            }else{
                it.findNavController().navigate(AccountFormFragmentDirections.actionNavFormAccountToNavHome())
            }

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
