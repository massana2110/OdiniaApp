package com.womannotfound.odinia.views.ui.fragments.operations

import android.app.DatePickerDialog
import android.media.MediaDescription
import android.media.MediaRouter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentEgressMoneyBinding
import com.womannotfound.odinia.viewmodel.EgressMoneyViewModel
import com.womannotfound.odinia.views.ui.activities.MainActivity
import kotlinx.android.synthetic.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class EgressMoneyFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var db: FirebaseFirestore
    private lateinit var user: FirebaseUser
    private lateinit var egressvm: EgressMoneyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentEgressMoneyBinding>(
            inflater,
            R.layout.fragment_egress_money,
            container,
            false
        )

        egressvm = activity?.run {
            ViewModelProvider(
                this,
                defaultViewModelProviderFactory
            ).get(EgressMoneyViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        db = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance().currentUser!!

        val spinnerAccounts: Spinner = binding.spinnerAccounts
        val spinnerCategories = binding.spinnerEgressCategories

        val btnDatePicker: Button = binding.btnDatePicker
        val textDate: TextView = binding.textDatePicked

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)


        btnDatePicker.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view: DatePicker?, dpYear: Int, dpMonth: Int, dpDay: Int ->
                    //Set to text View
                    textDate.text = "${dpDay}/${dpMonth + 1}/${dpYear}"
                }, year, month, day
            )
            datePickerDialog.show()
        }

        populateSpinnerAccountS(spinnerAccounts, user.uid)
        populateSpinnerExpensesCategories(spinnerCategories)

        spinnerAccounts.onItemSelectedListener = this
        spinnerCategories.onItemSelectedListener = this

        binding.btnAddEgress.setOnClickListener {
            if(binding.inputEgressMoney.text.isEmpty() || binding.inputDescriptionEgress.text.isEmpty()){
                Toast.makeText(context, "Por favor, no dejar campos vacios", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else {
                egressvm.date = textDate.text.toString()
                egressvm.account = spinnerAccounts.selectedItem.toString()
                egressvm.amount = binding.inputEgressMoney.text.toString().toFloat()
                egressvm.category = spinnerCategories.selectedItem.toString()
                egressvm.note = binding.inputDescriptionEgress.text.toString()

                if (egressvm.amount <= 0F || egressvm.date == "Selecciona Fecha") {
                    Toast.makeText(context, "Por favor ingrese datos validos", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    recordExpense(
                        user.uid,
                        egressvm.date,
                        egressvm.account,
                        egressvm.amount,
                        egressvm.category,
                        egressvm.note
                    )
                }
            }
        }

        return binding.root
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}

    private fun populateSpinnerExpensesCategories(spinner: Spinner) {
        val categoriesRef: CollectionReference = db.collection("expenses_categories")
        val expensesCategories: ArrayList<String> = ArrayList()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            expensesCategories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter;

        categoriesRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val category = document.getString("name")
                    expensesCategories.add(category!!)
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun populateSpinnerAccountS(spinner: Spinner, userId: String) {
        val accountsRef: CollectionReference = db.collection("accounts")
        val userAccounts: ArrayList<String> = ArrayList()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            userAccounts
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        accountsRef.whereEqualTo("userID", userId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val account = document.getString("nameAccount")
                    userAccounts.add(account!!)
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun recordExpense(
        userId: String,
        date: String,
        account: String,
        amount: Float,
        category: String,
        description: String
    ) {
        val expense = hashMapOf(
            "userId" to userId,
            "dateEgress" to date,
            "userAccount" to account,
            "amountEgress" to amount,
            "categoryEgress" to category,
            "descriptionEgress" to description
        )

        db.collection("expenses").add(expense)
            .addOnSuccessListener { Toast.makeText(context, "Gasto registrado exitosamente", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(context, "Gasto no pudo ser registrado, intente de nuevo.", Toast.LENGTH_SHORT).show() }
    }
}
