package com.womannotfound.odinia.views.ui.fragments.operations

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentAccountsBinding
import com.womannotfound.odinia.databinding.FragmentEntryMoneyBinding
import com.womannotfound.odinia.viewmodel.AccountsViewModel
import com.womannotfound.odinia.viewmodel.EntryMoneyViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class EntryMoneyFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var entryMoneyViewModel: EntryMoneyViewModel
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val binding = DataBindingUtil.inflate<FragmentEntryMoneyBinding>(inflater,
            R.layout.fragment_entry_money,container,false)

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        entryMoneyViewModel = activity?.run {
            ViewModelProvider(this, defaultViewModelProviderFactory).get(EntryMoneyViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        binding.btnDate.setOnClickListener{
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view: DatePicker?, dpYear: Int, dpMonth: Int, dpDay: Int ->
                    //Set to text View
                    binding.selectedDate.text = "${dpDay}/${dpMonth + 1}/${dpYear}"
                }, year, month, day)
            datePickerDialog.show()
        }

        val userID = auth.currentUser?.uid.toString()

        binding.btnAdd!!.setOnClickListener{
            entryMoneyViewModel.date = binding.selectedDate.text.toString()
            entryMoneyViewModel.account = binding.spinnerAccounts.selectedItem.toString()
            entryMoneyViewModel.amount = binding.addAmount!!.text.toString()
            entryMoneyViewModel.category = binding.spinnerEntryCategories.selectedItem.toString()
            entryMoneyViewModel.note = binding.editText7!!.text.toString()

            if(entryMoneyViewModel.amount == "" || entryMoneyViewModel.date == ""){
                Toast.makeText(context, "Por favor ingrese datos validos", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Sus ingresos han sido añadidos exitosamente", Toast.LENGTH_SHORT).show()
                addEntryMoney(userID,entryMoneyViewModel.date,entryMoneyViewModel.account,entryMoneyViewModel.amount,
                    entryMoneyViewModel.category,entryMoneyViewModel.note, currentDate)
            }
        }

        val spinnerCategories = binding.spinnerEntryCategories
        val spinnerAccounts:Spinner = binding.spinnerAccounts


        populateSpinnerCategories(spinnerCategories as Spinner)
        populateSpinnerAccounts(spinnerAccounts as Spinner, userID)

        binding.spinnerAccounts.onItemSelectedListener = this
        binding.spinnerEntryCategories.onItemSelectedListener = this

        return binding.root
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    private fun populateSpinnerAccounts(spinner: Spinner, userID: String){
        val userAccounts: ArrayList<String> = ArrayList()
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            userAccounts
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = adapter;

        db.collection("accounts").whereEqualTo("userID",userID).get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                for(document: QueryDocumentSnapshot in task.result!!){
                    val account: String? = document.getString("nameAccount")
                    userAccounts.add(account!!)
                }
                adapter.notifyDataSetChanged()
            }
        }
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

    /*private fun updateAccountBalance(amountEntry: Double, nameAccount:String){
        val balanceRef = db.collection("account").document()
        db.runTransaction{
            transaction ->
            val snapshot = transaction.get(balanceRef)
            val newBalance = snapshot.getDouble("balanceAccount")!! + amountEntry
            transaction.update(balanceRef, "balanceAccount", newBalance)
        }.addOnSuccessListener { Log.d("updateBalance", "Transaction Success!") }
            .addOnFailureListener { Log.w("updateBalance", "Transaction Failure") }
    }*/

    private fun updateAccountBalance(amountEntry: Float, nameAccount:String){
        val balanceRef = db.collection("accounts")
        balanceRef
            .whereEqualTo("nameAccount", nameAccount)
            .get()
            .addOnSuccessListener { document ->
                if(document != null){
                    val balance =
                }
            }


    }




    private fun addEntryMoney(userId: String, dateEntry: String, accountEntry: String, amountEntry: String,
                              categoryEntry: String, noteEntry: String, createdAt: String){
        val entry = hashMapOf(
            "userID" to userId,
            "dateEntry" to dateEntry,
            "accountEntry" to accountEntry,
            "amountEntry" to amountEntry,
            "categoryEntry" to categoryEntry,
            "noteEntry" to noteEntry,
            "createdAt" to createdAt
        )
        db.collection("entries_money")
            .add(entry)
            .addOnSuccessListener { Log.d("addEntry", "DocumentSnapshot succesfully written!") }
            .addOnFailureListener { Log.w("addEntry", "Error writing document") }
    }

}
