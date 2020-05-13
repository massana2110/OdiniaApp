package com.womannotfound.odinia.views.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class DemoCollectionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var collectionFragments: MutableList<Fragment> = ArrayList()
    private var titles: MutableList<String> = ArrayList()

    override fun getCount(): Int  = collectionFragments.size

    override fun getItem(i: Int): Fragment {
        return collectionFragments[i]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }

    fun addFragments(fragment: Fragment, title: String){
        collectionFragments.add(fragment)
        titles.add(title)
    }
}