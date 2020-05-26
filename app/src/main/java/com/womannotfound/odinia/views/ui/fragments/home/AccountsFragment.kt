package com.womannotfound.odinia.views.ui.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentAccountsBinding
import com.womannotfound.odinia.views.ui.fragments.operations.PaymentsFragmentArgs

class AccountsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAccountsBinding>(inflater, R.layout.fragment_accounts,container,false)

        binding.btnAddAccount.setOnClickListener{
            it.findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavFormAccount())
        }
        return binding.root
    }
}
