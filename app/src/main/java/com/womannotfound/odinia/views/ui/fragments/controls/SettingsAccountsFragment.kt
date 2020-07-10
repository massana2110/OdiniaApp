package com.womannotfound.odinia.views.ui.fragments.controls

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentSettingsAccountsBinding
import com.womannotfound.odinia.viewmodel.AccountsViewModel
import com.womannotfound.odinia.viewmodel.PaymentsViewModel
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class SettingsAccountsFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var vm: AccountsViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSettingsAccountsBinding>(
            inflater,
            R.layout.fragment_settings_accounts, container, false
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
        val spinnerAccounts: Spinner = binding.spinnerAccounts
        val spinnerCategory: Spinner = binding.spinnerAccounts2

        binding.btnAdd.setOnClickListener {
            val accountSelected = binding.spinnerAccounts.selectedItem.toString()
            var name = binding.editText.text.toString()
            var type = binding.spinnerAccounts2.selectedItem.toString()
            var balance = binding.inputAmountMoney.text.toString()
            var j = 0

            db.collection("accounts")
                .whereEqualTo("userID", userID)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.getString("nameAccount").toString() == name) {
                            j = +1
                        }
                    }
                    if (j != 0) {
                        Toast.makeText(
                            context,
                            "Error: Nombre de cuenta existente",
                            Toast.LENGTH_SHORT

                        ).show()

                    } else if (accountSelected == "" || name == "" || balance == "") {
                        Toast.makeText(context, "Proporcione datos validos", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        db.collection("accounts")
                            .whereEqualTo("userID", userID)
                            .whereEqualTo("nameAccount", accountSelected)
                            .addSnapshotListener { snapshot, e ->
                                if (e != null) {
                                    Log.w(ContentValues.TAG, "Listen Failed", e)
                                    return@addSnapshotListener
                                }
                                if (snapshot != null) {
                                    val documents = snapshot.documents
                                    documents.forEach {
                                        val expense = it.toObject(PaymentsViewModel::class.java)
                                        if (expense != null) {
                                            val indocument = it.id
                                            db.collection("accounts")
                                                .document(indocument)
                                                .update("nameAccount", name)
                                            db.collection("accounts")
                                                .document(indocument)
                                                .update("typeAccount", type)
                                            db.collection("accounts")
                                                .document(indocument)
                                                .update("balanceAccount", balance)
                                        }


                                    }


                                }

                            }
                        it.findNavController()
                            .navigate(SettingsAccountsFragmentDirections.actionNavSettingsAccountsFragmentToNavSettings())
                        Toast.makeText(
                            context,
                            "Cuenta editada exitosamente",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }
                }


        }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.types_of_accounts,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.accounts,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAccounts.adapter = adapter
        }
        populateSpinnerAccounts(spinnerAccounts, userID)
        spinnerAccounts.onItemSelectedListener = this

        return binding.root
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    private fun populateSpinnerAccounts(spinner: Spinner, UserID: String) {
        val accountsRef: CollectionReference =
            db.collection("accounts")
        val accountsCategories: ArrayList<String> = ArrayList()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            accountsCategories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = adapter;

        accountsRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                for (document in task.result!!) {
                    if (document.getString("userID").toString() == UserID) {
                        val category = document.getString("nameAccount")
                        accountsCategories.add(category!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }
        }
    }


}
