package com.womannotfound.odinia.views.ui.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentAccountsBinding
import com.womannotfound.odinia.viewmodel.AccountsViewModel
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.AccountAdapter
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.AccountsItems

class AccountsFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var vm: AccountsViewModel
    private lateinit var db: FirebaseFirestore

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAccountsBinding>(inflater, R.layout.fragment_accounts,container,false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        vm = activity?.run {
            ViewModelProvider(this, defaultViewModelProviderFactory).get(AccountsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        binding.btnAddAccount.setOnClickListener{
            Navigation.findNavController(it).navigate(HomeFragmentDirections.actionNavHomeToNavFormAccount())
        }

        val userID = auth.currentUser?.uid.toString()
        if( vm.type != "" && vm.balance != ""){
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
            vm.list.add(itemB)
            addAccount(userID,vm.name,vm.type,vm.balance)
        }else{
            if(vm.list.isNotEmpty()){
                binding.accountLayout.removeView(binding.txtMessageNoAcc)
                binding.accountLayout.removeView(binding.txtMessageAddAcc)
                binding.recyclerViewAccounts.isVisible = true
            }else{
                getAccounts(userID)
                Toast.makeText(context,"hay ${vm.list.size} en la lista",Toast.LENGTH_SHORT).show()
                if(vm.list.isNotEmpty()){
                    binding.accountLayout.removeView(binding.txtMessageNoAcc)
                    binding.accountLayout.removeView(binding.txtMessageAddAcc)
                    binding.recyclerViewAccounts.isVisible = true
                }
            }
        }

        vm.name = ""
        vm.type = ""
        vm.balance= ""

        viewManager = LinearLayoutManager(context)
        viewAdapter = AccountAdapter(vm.list)
        recyclerView = binding.recyclerViewAccounts.apply {
            setHasFixedSize(true)
            layoutManager= viewManager
            adapter= viewAdapter
        }

        return binding.root
    }

    private fun addAccount (userID: String, nameAccount: String, typeAccount: String, balanceAccount: String){
        val user = hashMapOf(
            "userID" to userID,
            "nameAccount" to nameAccount,
            "typeAccount" to typeAccount,
            "balanceAccount" to balanceAccount
        )
        db.collection("accounts")
            .add(user)
            .addOnSuccessListener { Log.d("AddAccount","DocumentSnapshot successfully written!") }
            .addOnFailureListener{ Log.w("AddAccount","Error writing document")}
    }

    private fun getAccounts (userID: String) {
        db.collection("accounts")
            .whereEqualTo("userID",userID)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val name = document.getString("nameAccount").toString()
                    val type = document.getString("typeAccount").toString()
                    val balance = document.getString("balanceAccount").toString()
                    val item = AccountsItems(R.drawable.ic_ingresos,name,type,balance)

                    vm.list.add(item)

                    Toast.makeText(context,"hay $name , $balance",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{ exception -> Log.w("getAccount", "Error getting documents", exception) }
    }
}
