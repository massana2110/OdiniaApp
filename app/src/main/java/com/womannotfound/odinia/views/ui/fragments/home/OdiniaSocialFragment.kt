package com.womannotfound.odinia.views.ui.fragments.home

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentOdiniaSocialBinding
import com.womannotfound.odinia.databinding.FragmentOdiniaSocialPurchasesBinding
import com.womannotfound.odinia.viewmodel.SocialViewModel
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.SocialAdapter
import kotlinx.android.synthetic.main.activity_authentication.*
import kotlinx.android.synthetic.main.social_items.view.*

/**
 * A simple [Fragment] subclass.
 */
class OdiniaSocialFragment : Fragment() {

    private lateinit var vm: SocialViewModel

    private lateinit var rv: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm = activity?.run {
            ViewModelProvider(
                this,
                defaultViewModelProviderFactory
            ).get(SocialViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        val binding = DataBindingUtil.inflate<FragmentOdiniaSocialBinding>(
            inflater,
            R.layout.fragment_odinia_social, container, false
        )

        binding.button.setOnClickListener {

            it.findNavController().navigate(R.id.nav_odiniaSocialPurchases)

        }
        

        viewManager = LinearLayoutManager(this.context)
        viewAdapter = SocialAdapter(vm.list)
        rv = binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return binding.root
    }



}
