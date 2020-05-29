package com.womannotfound.odinia.views.ui.fragments.operations

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider

import com.womannotfound.odinia.R
import com.womannotfound.odinia.viewmodel.EgressMoneyViewModel
import com.womannotfound.odinia.views.ui.activities.MainActivity
import kotlinx.android.synthetic.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class EgressMoneyFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var egressMoneyViewModel: EgressMoneyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_egress_money, container, false)
        val spinnerAccounts: Spinner = view.findViewById(R.id.spinner_accounts)
        val spinnerCategories: Spinner = view.findViewById(R.id.spinnerCategories)

        val btnDatePicker: Button = view.findViewById(R.id.btn_datePicker)
        val textDate: TextView = view.findViewById(R.id.textDatePicked)

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

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories_pays,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategories.adapter = adapter
        }

        spinnerAccounts.onItemSelectedListener = this
        spinnerCategories.onItemSelectedListener = this

        return view
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

}
