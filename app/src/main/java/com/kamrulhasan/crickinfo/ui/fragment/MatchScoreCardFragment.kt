package com.kamrulhasan.crickinfo.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.databinding.FragmentHomeBinding
import com.kamrulhasan.crickinfo.databinding.FragmentMatchScoreCardBinding

class MatchScoreCardFragment : Fragment() {

    private var _binding : FragmentMatchScoreCardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMatchScoreCardBinding.inflate(layoutInflater)
        return binding.root
    }
}