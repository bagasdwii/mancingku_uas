package com.example.mancingku.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import com.example.mancingku.R
import com.example.mancingku.databinding.FragmentMapsBinding
import com.google.firebase.auth.FirebaseAuth


class MapsFragment : Fragment() {
    private var _binding : FragmentMapsBinding? = null
    private val binding get() = _binding!!
    lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        binding.spotMancing.setOnClickListener {
            val navController = findNavController(view)
            navController.navigate(MainFragmentDirections.actionMainFragmentToSpotPancingFragment())
        }
        if (user != null) {
            binding.welcomeMessageTextView.setText("Selamat Datang " + getUsernameFromEmail(user.email ?: ""))
        }
    }
    private fun getUsernameFromEmail(email: String): String {
        return email.substringBefore('@')
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
