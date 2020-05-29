package com.womannotfound.odinia.views.ui.fragments.controls

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2

import com.womannotfound.odinia.R
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.HelpSlide
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.HelpSlideAdapter
import kotlinx.android.synthetic.main.fragment_help.*

/**
 * A simple [Fragment] subclass.
 */
class HelpFragment : Fragment() {

    private val introSlideAdapter = HelpSlideAdapter(
        listOf(
            HelpSlide(
                "Bienvenido a Odinia",
                "Sera un placer ayudarte",
                R.drawable.ic_logo_vertical
            ),
            HelpSlide(
                "Cuentas",
                "Tu dinero se mueve a traves de diferentes cuentas, las cuales pueden ser de 3 tipos: Tarjeta de credito, Efectivo, Tarjeta de debito.",
                R.drawable.ic_accounts_slider
            ),
            HelpSlide(
                "Movimientos",
                "Tu dinero entra y sale de tus cuentas, lo cual provoca movimientos. Los movimientos los categorizamos en: Ingresos, Gastos, Pagos.",
                R.drawable.ic_movements_slider
            ),
            HelpSlide(
                "Actividad",
                "Visualiza cada movimiento registrado para que lleves el control. Ademas despliega graficos para monitorear el flujo de tu dinero.",
                R.drawable.ic_activity_slider
            )
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        introSliderViewPager.adapter = introSlideAdapter
        setupIndicators()
        setCurrentIndicator(0)
        introSliderViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        buttonNext.setOnClickListener {
            if (introSliderViewPager.currentItem + 1 < introSlideAdapter.itemCount){
                introSliderViewPager.currentItem += 1
            }
        }
    }

    private fun setupIndicators(){
        val indicators = arrayOfNulls<ImageView>(introSlideAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for(i in indicators.indices){
            indicators[i] = ImageView(requireContext())
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            indicatorsContainer.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int){
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount){
            val imageView = indicatorsContainer[i] as ImageView
            if (i == index){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }
}
