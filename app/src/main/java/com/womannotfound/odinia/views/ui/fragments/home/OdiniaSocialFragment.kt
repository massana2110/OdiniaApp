package com.womannotfound.odinia.views.ui.fragments.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentOdiniaSocialBinding
import com.womannotfound.odinia.viewmodel.SocialViewModel
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.SocialAdapter
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.SocialItems
import kotlinx.android.synthetic.main.social_items.view.*

/**
 * A simple [Fragment] subclass.
 */
class OdiniaSocialFragment : Fragment(), SocialAdapter.OnSocialItemClickListener {

    private lateinit var vm: SocialViewModel
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var rv: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
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

        vm.list.clear()
        getPosts()
        //changeCardViewColor(binding)
        binding.textViewUsername.setText(vm.username)
        binding.textView27.setText(vm.likeCounter)
        binding.textView28.setText(vm.dislikeCounter)
        Glide.with(this).load(vm.userImg).into(binding.imageViewUser)


        viewManager = LinearLayoutManager(this.context)
        viewAdapter = SocialAdapter(vm.list, this)
        rv = binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        //val socialCard : CardView = binding.recyclerView.cardView
        //changeCardViewColor(socialCard)

        return binding.root
    }

    private fun changeCardViewColor(
        binding: FragmentOdiniaSocialBinding
    ) {
        val socialCard: CardView = binding.recyclerView.cardView
        when (vm.color) {
            "Azul Oscuro" -> socialCard.setCardBackgroundColor(Color.parseColor("#252850"))
            "Gris Oscuro" -> socialCard.setCardBackgroundColor(Color.parseColor("#828282"))
            "Verde Oscuro" -> socialCard.setCardBackgroundColor(Color.parseColor("#2d572c"))
            "Ocre" -> socialCard.setCardBackgroundColor(Color.parseColor("#b9935a"))
            "Plateado" -> socialCard.setCardBackgroundColor(Color.parseColor("#8a9597"))
            else -> socialCard.setCardBackgroundColor(Color.parseColor("#393F56"))

        }
    }

    private fun getPosts() {
        db.collection("posts").get().addOnSuccessListener { documents ->
            for (document in documents) {
                vm.description = document["descriptionPost"].toString()
                vm.date = document["datePost"].toString()
                vm.color = document["cardColor"].toString()
                vm.username = document["username"].toString()
                val item: SocialItems = SocialItems(R.drawable.ic_gastos, vm.description, vm.date)

                vm.list.add(item)
                viewAdapter.notifyDataSetChanged()
            }
        }.addOnFailureListener { exception ->
            Log.w(
                "getExpenses",
                "Error getting documents",
                exception
            )
        }
    }

    override fun onItemClick(item: SocialItems, position: Int) {
        db.collection("posts").whereEqualTo("descriptionPost", item.category).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    vm.username = document["username"].toString()
                    vm.likeCounter = document["likeCounter"].toString()
                    vm.dislikeCounter = document["dislikeCounter"].toString()
                }
            }
    }
}


