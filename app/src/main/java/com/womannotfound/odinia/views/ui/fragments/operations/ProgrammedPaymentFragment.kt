package com.womannotfound.odinia.views.ui.fragments.operations

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
import com.womannotfound.odinia.databinding.FragmentProgrammedPaymentBinding
import com.womannotfound.odinia.viewmodel.PaymentsViewModel
import java.text.SimpleDateFormat
import java.util.*

class ProgrammedPaymentFragment : Fragment(), AdapterView.OnItemSelectedListener {


    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: PaymentsViewModel
    private lateinit var db: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentProgrammedPaymentBinding>(
            inflater,
            R.layout.fragment_programmed_payment,
            container,
            false
        )

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val userID = auth.currentUser?.uid.toString()
        val spinnerAccounts: Spinner = binding.spinnerAccounts

        viewModel = activity?.run {
            ViewModelProvider(
                this,
                defaultViewModelProviderFactory
            ).get(PaymentsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        binding.btnAdd.setOnClickListener {
            viewModel.name = binding.namePayment.text.toString()
            viewModel.category = binding.addCategory.selectedItem.toString()
            viewModel.amount = binding.addAmount.text.toString()
            viewModel.account= binding.spinnerAccounts.selectedItem.toString()
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            viewModel.inputDate= currentDate
            //date
            val day = binding.addDate.dayOfMonth.toString()
            val month = binding.addDate.month + 1
            val year = binding.addDate.year.toString()
            val date = "$day/$month/$year"

            viewModel.date = date

            if ((viewModel.name == " " && viewModel.amount == " ") || (viewModel.name == "" && viewModel.amount == "")) {
                Toast.makeText(context, "Proporcione datos validos", Toast.LENGTH_SHORT).show()
            } else {
                it.findNavController()
                    .navigate(ProgrammedPaymentFragmentDirections.actionProgrammedPaymentFragmentToNavPayments())
            }
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.accounts,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAccounts.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_payments,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.addCategory.adapter = adapter
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
