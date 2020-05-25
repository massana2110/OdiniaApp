package com.womannotfound.odinia.views.ui.fragments.operations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

import com.womannotfound.odinia.R
import com.womannotfound.odinia.views.ui.activities.MainActivity
import kotlinx.android.synthetic.*

/**
 * A simple [Fragment] subclass.
 */
class EgressMoneyFragment : Fragment(), AdapterView.OnItemSelectedListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_egress_money, container, false)
        val spinnerAccounts: Spinner = view.findViewById(R.id.spinner_accounts)
        val spinnerCategories: Spinner = view.findViewById(R.id.spinnerCategories)

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
