package com.vito.misur.currencytracker.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vito.misur.currencytracker.R
import com.vito.misur.currencytracker.screen.welcome.ModalBottomFragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_home, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currencySymbol.setOnClickListener {
            parentFragmentManager.beginTransaction().add(ModalBottomFragment(), "modal").commit()
        }

        favorites.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.toFavorites())
        }
    }
}