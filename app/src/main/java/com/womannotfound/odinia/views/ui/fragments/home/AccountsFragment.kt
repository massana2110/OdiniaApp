package com.womannotfound.odinia.views.ui.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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
    private lateinit var viewAdapter: AccountAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var userID: String
    private lateinit var accountRef: CollectionReference

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

        userID = auth.currentUser?.uid.toString()
        if( vm.type != "" && vm.balance != ""){
            binding.accountLayout.removeView(binding.txtMessageNoAcc)
            binding.accountLayout.removeView(binding.txtMessageAddAcc)

            addAccount(userID,vm.name,vm.type,vm.balance)
        }

        accountRef = db.collection("accounts")

        vm.name = ""
        vm.type = ""
        vm.balance= ""


        setUpRecyclerView(binding, userID)
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

    private fun setUpRecyclerView(binding: FragmentAccountsBinding, userID: String){
        val query : Query = accountRef.whereEqualTo("userID", userID)
        val options = FirestoreRecyclerOptions.Builder<AccountsItems>().setQuery(query, AccountsItems::class.java).build()

        viewManager = LinearLayoutManager(context)
        viewAdapter = AccountAdapter(options)
        recyclerView = binding.recyclerViewAccounts.apply {
            setHasFixedSize(true)
            layoutManager= viewManager
            adapter= viewAdapter
        }

        query.addSnapshotListener { querySnapshot, _ ->
            if(!querySnapshot?.isEmpty!!){
                binding.accountLayout.removeView(binding.txtMessageAddAcc)
                binding.accountLayout.removeView(binding.txtMessageNoAcc)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        viewAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        viewAdapter.stopListening()
    }
}
