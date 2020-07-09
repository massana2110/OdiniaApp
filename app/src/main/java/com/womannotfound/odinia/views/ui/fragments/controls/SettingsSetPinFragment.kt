package com.womannotfound.odinia.views.ui.fragments.controls

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentSettingsSetPinBinding
import com.womannotfound.odinia.viewmodel.AccountsViewModel
import com.womannotfound.odinia.viewmodel.PaymentsViewModel

/**
 * A simple [Fragment] subclass.
 */
class SettingsSetPinFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSettingsSetPinBinding>(
            inflater,
            R.layout.fragment_settings_set_pin, container, false
        )

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val userID = auth.currentUser?.uid.toString()


        binding.btnAdd.setOnClickListener {
            var passwordPin = binding.editText.text.toString()


            if (passwordPin == "") {
                Toast.makeText(context, "Proporcione datos validos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                db.collection("users")
                    .document(userID)
                    .update("userPin", passwordPin)
                it.findNavController()
                    .navigate(SettingsSetPinFragmentDirections.actionNavSettingsSetPinFragmentToNavSettings())
                Toast.makeText(
                    context,
                    "Pin editado exitosamente",
                    Toast.LENGTH_SHORT
                )
                    .show()
                passwordPin=""
            }

        }


        return binding.root
    }

}
