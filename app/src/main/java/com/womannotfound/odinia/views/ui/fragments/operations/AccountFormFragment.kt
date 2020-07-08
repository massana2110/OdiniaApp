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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentAccountFormBinding
import com.womannotfound.odinia.viewmodel.AccountsViewModel


class AccountFormFragment : Fragment() {
    private lateinit var vm: AccountsViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAccountFormBinding>(
            inflater,
            R.layout.fragment_account_form,
            container,
            false
        )
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        vm = activity?.run {
            ViewModelProvider(
                this,
                defaultViewModelProviderFactory
            ).get(AccountsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        val userID = auth.currentUser?.uid.toString()

        binding.btnAdd.setOnClickListener {
            vm.name = binding.textEntryNameAcc.text.toString()
            vm.type = binding.accType.selectedItem.toString()
            vm.balance = binding.balanceEntry.text.toString()


            var i = 0;
            db.collection("accounts")
                .whereEqualTo("userID", userID)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.getString("nameAccount").toString() == vm.name) {
                            i = +1;
                        }
                    }
                    if (i != 0) {
                        Toast.makeText(
                            context,
                            "Error: Nombre de cuenta existente",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else if ((vm.type == "" && vm.balance == "") || vm.type == "" || vm.balance == "") {
                        Toast.makeText(context, "Proporcione datos validos", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        it.findNavController()
                            .navigate(AccountFormFragmentDirections.actionNavFormAccountToNavHome())
                    }

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
