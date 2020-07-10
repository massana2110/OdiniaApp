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

        val spinnerType: Spinner =binding.spinnerType

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.typeCategory,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerType.adapter = adapter
        }








        binding.btnAdd.setOnClickListener {
            var name = binding.editText.text.toString()
            val type = binding.spinnerType.selectedItem.toString()

            var i = 0;
            db.collection("expenses_categories")
                .whereEqualTo("userID", userID)
                .whereEqualTo("type",type)
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

                        if(type=="Egreso"){val expenses_categories = hashMapOf(
                            "userID" to userID,
                            "name" to name,
                            "type" to type
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
                        }else if(type=="Ingreso"){
                            val expenses_categories = hashMapOf(
                                "userID" to userID,
                                "name" to name,
                                "type" to type
                            )
                            db.collection("entries_categories")
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

        }








        spinnerType.onItemSelectedListener = this

        return binding.root
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }


}
