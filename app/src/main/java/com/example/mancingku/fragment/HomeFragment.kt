package com.example.mancingku.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mancingku.R
import com.example.mancingku.adapter.RecyclerPost
import com.example.mancingku.adapter.RecyclerTokoFragment
import com.example.mancingku.databinding.FragmentHomeBinding
import com.example.mancingku.databinding.FragmentPostBinding
import com.example.mancingku.model.modelPost
import com.example.mancingku.model.modelToko
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    fun generateUniqueKey(): String {
        return UUID.randomUUID().toString()
    }
    val uniqueKey = generateUniqueKey()
    lateinit var auth : FirebaseAuth
    private lateinit var imgUri : Uri
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvPost)
//        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
//        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
//
//        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
//        actionBar?.apply {
//            setDisplayHomeAsUpEnabled(true) // Menampilkan tombol back
//            title = "" // Mengatur judul toolbar
//        }

        val databaseReference = FirebaseDatabase.getInstance().getReference("post-mancing")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val postList = mutableListOf<modelPost>()

                for (snapshot in dataSnapshot.children) {

                    val username = snapshot.child("user").getValue(String::class.java) ?: ""
                    val linkpost = snapshot.child("linkpost").getValue(String::class.java) ?: ""
                    val status = snapshot.child("status").getValue(String::class.java) ?: ""

                    val post = modelPost(username, linkpost, status, "","")
                    postList.add(post)

                    // Ambil URL gambar sesuai dengan kunci yang ada di Firebase Realtime Database
                    val storageRef = FirebaseStorage.getInstance().reference.child("img_post/${snapshot.key}/image.jpg")
                    val storageRefProfil = FirebaseStorage.getInstance().reference.child("img_user/${username}")
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Simpan URL gambar ke properti imgURL pada objek spot yang sesuai
                        post.gambar = uri.toString()
                    }.addOnFailureListener {
                        // Handle error jika gagal mengambil URL gambar dari Cloud Storage
                    }
                    storageRefProfil.downloadUrl.addOnSuccessListener { uri ->
                        // Simpan URL gambar ke properti imgURL pada objek spot yang sesuai
                        post.profil = uri.toString()
                    }.addOnFailureListener {
                        // Handle error jika gagal mengambil URL gambar dari Cloud Storage
                    }
                }


                // Gunakan spotList dalam RecyclerViewAdapter
                val adapter = RecyclerPost(postList)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adapter

                // Set URL gambar ke setiap entri dalam RecyclerView

                adapter.notifyDataSetChanged()
//iki sing tk tanbah
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error jika terjadi pembatalan pengambilan data dari Realtime Database
            }
        })
    }

}