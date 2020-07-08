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
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentSettingsCategoriesBinding
import com.womannotfound.odinia.viewmodel.PaymentsViewModel
import com.womannotfound.odinia.views.ui.fragments.operations.ProgrammedPaymentFragmentDirections
import kotlinx.android.synthetic.main.fragment_settings_categories.*
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class SettingsCategoriesFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: PaymentsViewModel
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSettingsCategoriesBinding>(
            inflater,
            R.layout.fragment_settings_categories, container, false
        )

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val userID = auth.currentUser?.uid.toString()
        val spinnerCategory: Spinner = binding.spinnerCategory

        binding.btnAdd.setOnClickListener {
            var name = binding.editText.text.toString()
            var i = 0;
            db.collection("expenses_categories")
                .whereEqualTo("userID", userID)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.getString("name").toString() == name) {
                            i = +1;
                        }
                    }
                    if (i != 0) {
                        Toast.makeText(
                            context,
                            "Error: Nombre de categoria existente",
                            Toast.LENGTH_SHORT

                        ).show()
                        name = ""

                    } else if (name == "") {
                        Toast.makeText(context, "Proporcione datos validos", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        val expenses_categories = hashMapOf(
                            "userID" to userID,
                            "name" to name
                        )
                        db.collection("expenses_categories")
                            .add(expenses_categories)
                            .addOnSuccessListener {
                                Log.d(
                                    "AddCategory",
                                    "DocumentSnapshot successfully written!"
                                )
                            }
                            .addOnFailureListener { Log.w("AddCategory", "Error writing document") }
                        it.findNavController()
                            .navigate(SettingsCategoriesFragmentDirections.actionNavSettingsCategoriesFragmentToNavSettings())
                        Toast.makeText(
                            context,
                            "Categoria añadida exitosamente",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        name = ""
                    }
                }

        }


        binding.btnAdd2.setOnClickListener {
            var j = 0;
            val categoryToEdit = binding.spinnerCategory.selectedItem.toString()
            var newName = binding.inputNewName.text.toString()

            db.collection("expenses_categories")
                .whereEqualTo("userID", userID)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.getString("name").toString() == newName) {
                            j = +1;
                        }
                    }
                    if (j != 0) {
                        Toast.makeText(
                            context,
                            "Error: Nombre de categoria existente",
                            Toast.LENGTH_SHORT

                        ).show()

                    } else if (newName == "") {
                        Toast.makeText(context, "Proporcione datos validos", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        db.collection("expenses_categories")
                            .whereEqualTo("userID", userID)
                            .whereEqualTo("name", categoryToEdit)
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
                                            db.collection("expenses_categories")
                                                .document(indocument)
                                                .update("name", newName)
                                        }


                                    }


                                }

                            }
                        it.findNavController()
                            .navigate(SettingsCategoriesFragmentDirections.actionNavSettingsCategoriesFragmentToNavSettings())
                        Toast.makeText(
                            context,
                            "Categoria editada  exitosamente",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }
                }
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_payments,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter
        }

        populateSpinnerExpensesCategories(spinnerCategory, userID)
        spinnerCategory.onItemSelectedListener = this
        return binding.root
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    private fun populateSpinnerExpensesCategories(spinner: Spinner, userID: String) {
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
                    if (document.getString("userID").toString() == userID) {
                        val category = document.getString("name")
                        expensesCategories.add(category!!)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }


}
