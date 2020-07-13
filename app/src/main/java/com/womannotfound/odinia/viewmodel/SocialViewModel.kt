package com.womannotfound.odinia.viewmodel

import androidx.lifecycle.ViewModel
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.SocialItems

class SocialViewModel: ViewModel() {
    var description: String? = ""
    var date: String = ""
    var color: String = ""
    val list = ArrayList<SocialItems>()
}