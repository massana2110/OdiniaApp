package com.womannotfound.odinia.viewmodel

import androidx.lifecycle.ViewModel
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.PaymentsItems

class PaymentsViewModel : ViewModel(){
    var name: String = ""
    var category: String = ""
    var amount: String = ""
    var date: String = ""
    val list = ArrayList<PaymentsItems>()
}