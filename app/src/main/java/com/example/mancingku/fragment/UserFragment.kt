package com.example.mancingku.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mancingku.Login
import com.example.mancingku.R
import com.example.mancingku.databinding.FragmentUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class UserFragment : Fragment() {
    private var _binding : FragmentUserBinding? = null
    lateinit var auth : FirebaseAuth

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        //kondisi user sedang login atau tidak
        if (user != null){
            binding.edtName.setText(user.displayName)
            binding.edtEmail.setText(user.email)
        }
        binding.btnLogout.setOnClickListener {
            btnLogout()
        }
    }
    private fun btnLogout() {
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        val intent = Intent(context,Login::class.java)
        startActivity(intent)
        activity?.finish()
    }

}