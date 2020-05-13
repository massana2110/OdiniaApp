package com.womannotfound.odinia.views.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.womannotfound.odinia.R


class HomeFragment : Fragment() {

//    private lateinit var homeViewModel: HomeViewModel
    private lateinit var demoCollectionPagerAdapter: DemoCollectionPagerAdapter
    private lateinit var viewPager: ViewPager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =
//            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        demoCollectionPagerAdapter = DemoCollectionPagerAdapter(childFragmentManager)
        viewPager = view.findViewById(R.id.viewPager)
        llenarViewPager(viewPager)
        viewPager.adapter = demoCollectionPagerAdapter

        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun llenarViewPager(viewPager: ViewPager){
        demoCollectionPagerAdapter.addFragments(AccountsFragment(), "Cuentas")
        demoCollectionPagerAdapter.addFragments(OdiniaSocialFragment(), "Social")
    }
}
