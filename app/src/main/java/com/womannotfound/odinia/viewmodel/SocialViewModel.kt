package com.womannotfound.odinia.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.SocialItems

class SocialViewModel: ViewModel() {
    var userID:String = ""
    var username: String = ""
    var description: String? = ""
    var date: String = ""
    var color: String = ""
    var userImg: String = ""
    val list = ArrayList<SocialItems>()
}