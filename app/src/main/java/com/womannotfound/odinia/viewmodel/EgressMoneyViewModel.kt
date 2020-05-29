package com.womannotfound.odinia.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

class EgressMoneyViewModel: ViewModel() {
    var date: String = ""
    var account: String = ""
    var amount: String = ""
    var category: String = ""
    var note: String = ""

    init {
        Log.i("EgressMoneyViewModel", "EgressMoneyVM Created")
    }
}