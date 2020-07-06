package com.womannotfound.odinia.views.ui.fragments.operations

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentEntryMoneyBinding
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class EntryMoneyFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        db = FirebaseFirestore.getInstance()

        val binding = DataBindingUtil.inflate<FragmentEntryMoneyBinding>(inflater,
            R.layout.fragment_entry_money,container,false)

        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        binding.btnDate.setOnClickListener{
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view: DatePicker?, dpYear: Int, dpMonth: Int, dpDay: Int ->
                    //Set to text View
                    binding.selectedDate.text = "${dpDay}/${dpMonth + 1}/${dpYear}"
                }, year, month, day)
            datePickerDialog.show()
        }

        val spinnerCategories = binding.spinnerEntryCategories

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.accounts,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerAccounts.adapter = adapter
        }

        populateSpinnerCategories(spinnerCategories as Spinner)

//        ArrayAdapter.createFromResource(
//            requireContext(),
//            R.array.categories_pays,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            binding.spinnerCategories?.adapter = adapter
//        }

        binding.spinnerAccounts.onItemSelectedListener = this
        binding.spinnerEntryCategories.onItemSelectedListener = this

        return binding.root
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    private fun populateSpinnerCategories(spinner: Spinner){
        val categoriesRef: CollectionReference = db.collection("entries_categories")
        val entryCategories: ArrayList<String> = ArrayList()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            entryCategories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = adapter;
        
        categoriesRef.get().addOnCompleteListener {task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    val category = document.getString("name")
                    entryCategories.add(category!!)
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

}
