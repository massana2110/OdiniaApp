package com.womannotfound.odinia.views.ui.fragments.operations

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentEgressMoneyBinding
import com.womannotfound.odinia.viewmodel.EgressMoneyViewModel
import com.womannotfound.odinia.views.ui.activities.MainActivity
import kotlinx.android.synthetic.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class EgressMoneyFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var egressMoneyViewModel: EgressMoneyViewModel
    private lateinit var db: FirebaseFirestore

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

        db = FirebaseFirestore.getInstance()

        val spinnerAccounts: Spinner = binding.spinnerAccounts
        val spinnerCategories = binding.spinnerEgressCategories

        val btnDatePicker: Button = binding.btnDatePicker
        val textDate: TextView = binding.textDatePicked

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        Log.i("EgressFragment", "Called ViewModelProvider")
        //ViewModel
        egressMoneyViewModel = activity?.run {
            ViewModelProvider(this, defaultViewModelProviderFactory).get(EgressMoneyViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        btnDatePicker.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view: DatePicker?, dpYear: Int, dpMonth: Int, dpDay: Int ->
                //Set to text View
                textDate.text = "${dpDay}/${dpMonth + 1}/${dpYear}"
            }, year, month, day)
            datePickerDialog.show()
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.accounts,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAccounts.adapter = adapter
        }

        populateSpinnerExpensesCategories(spinnerCategories)

        spinnerAccounts.onItemSelectedListener = this
        spinnerCategories.onItemSelectedListener = this

        return binding.root
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    private fun populateSpinnerExpensesCategories(spinner: Spinner){
        val categoriesRef: CollectionReference = db.collection("expenses_categories")
        val expensesCategories: ArrayList<String> = ArrayList()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            expensesCategories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = adapter;

        categoriesRef.get().addOnCompleteListener {task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val category = document.getString("name")
                    expensesCategories.add(category!!)
                }
                adapter.notifyDataSetChanged()
            }
        }
    }
}
