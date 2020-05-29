package com.womannotfound.odinia.views.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentAccountsBinding
import com.womannotfound.odinia.viewmodel.AccountsViewModel
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.AccountAdapter
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.AccountsItems
import kotlinx.android.synthetic.main.fragment_accounts.*

class AccountsFragment : Fragment() {
    private lateinit var vm: AccountsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAccountsBinding>(inflater, R.layout.fragment_accounts,container,false)

        vm = activity?.run {
            ViewModelProvider(this, defaultViewModelProviderFactory).get(AccountsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        binding.btnAddAccount.setOnClickListener{
            Navigation.findNavController(it).navigate(HomeFragmentDirections.actionNavHomeToNavFormAccount())
        }

        if( vm.type != "" && vm.balance != "" && vm.list.isEmpty()){
            binding.accountLayout.removeView(binding.txtMessageNoAcc)
            binding.accountLayout.removeView(binding.txtMessageAddAcc)
            binding.recyclerViewAccounts.isVisible = true

            val balance = "$${vm.balance}"
            val itemB = AccountsItems(
                R.drawable.ic_ingresos,
                vm.name,
                vm.type,
                balance
            )
            vm.list += itemB
            vm.name = ""
            vm.type = ""
            vm.balance= ""
        }else if(vm.list.isNotEmpty()){
            binding.accountLayout.removeView(binding.txtMessageNoAcc)
            binding.accountLayout.removeView(binding.txtMessageAddAcc)
            binding.recyclerViewAccounts.isVisible = true

            if( vm.type != "" && vm.balance != ""){
                val balance = "$${vm.balance}"
                val itemB = AccountsItems(
                    R.drawable.ic_ingresos,
                    vm.name,
                    vm.type,
                    balance
                )
                vm.list += itemB
                vm.name = ""
                vm.type = ""
                vm.balance= ""
            }
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        recycler_view_accounts.adapter = AccountAdapter(vm.list)
        recycler_view_accounts.layoutManager = LinearLayoutManager(context)
        recycler_view_accounts.setHasFixedSize(true)
    }
}
