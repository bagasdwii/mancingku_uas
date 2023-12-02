package com.example.mancingku.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mancingku.R
import com.example.mancingku.activity.Login
import com.example.mancingku.adapter.RecyclerSpotManvcingAdapter
import com.example.mancingku.databinding.FragmentSpotPancingBinding
import com.example.mancingku.model.modelSpotMancing
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SpotPancingFragment : Fragment() {
    private var _binding : FragmentSpotPancingBinding? = null

    lateinit var auth : FirebaseAuth
    private lateinit var imgUri : Uri
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var adapter: RecyclerSpotManvcingAdapter
    private lateinit var spotList: MutableList<modelSpotMancing>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpotPancingBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.rvSpot)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Menampilkan tombol back
            title = "" // Mengatur judul toolbar
        }

        val databaseReference = FirebaseDatabase.getInstance().getReference("spot-mancing")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val spotList = mutableListOf<modelSpotMancing>()

                for (snapshot in dataSnapshot.children) {
                    val alamat = snapshot.child("alamat").getValue(String::class.java) ?: ""
                    val namaspot = snapshot.child("namaspot").getValue(String::class.java) ?: ""

                    val spot = modelSpotMancing(alamat, namaspot)
                    spotList.add(spot)
                }

                // Gunakan spotList dalam RecyclerViewAdapter
                val adapter = RecyclerSpotManvcingAdapter(spotList)
                recyclerView.layoutManager = LinearLayoutManager(requireContext()) // Atur layout manager jika belum diatur
                recyclerView.adapter = adapter


                adapter.notifyDataSetChanged() // Beri tahu adapter bahwa data telah berubah
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error jika terjadi pembatalan pengambilan data
            }
        })

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val admin = "untukrandom130@gmail.com"
        //kondisi user sedang login atau tidak

        //kondisi email sudah verifikasi atau belum
        if (user != null && user.email == admin) {
            binding.btnAdd.visibility = View.VISIBLE

        } else {
            binding.btnAdd.visibility = View.GONE

        }
        binding.btnAdd.setOnClickListener {
            TambahLokasiSpot()
        }
    }

    private fun TambahLokasiSpot() {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        database = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://mancingku-fa98f-default-rtdb.firebaseio.com/")

        binding.slotSpot.visibility = View.VISIBLE

        binding.btnCancel.setOnClickListener {
            binding.slotSpot.visibility = View.GONE
        }

        binding.btnConfirm.setOnClickListener {
            val userEmail = user?.email // Mengambil email dari currentUser
            val alamat = binding.edtAlamat.text.toString()
            val namaspot = binding.edtNama.text.toString()
            val deskripsispot = binding.edtDeskripsi.text.toString()
            val linkspot = binding.edtLink.text.toString()

            if (userEmail.isNullOrEmpty() || alamat.isEmpty() || namaspot.isEmpty() || deskripsispot.isEmpty() || linkspot.isEmpty()) {
                Toast.makeText(
                    activity,
                    "Ada Data Yang Masih Kosong!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val spotRef = database.child("spot-mancing").push() // Menggunakan push untuk membuat key unik

                val spotData = HashMap<String, Any>()
                spotData["user"] = userEmail
                spotData["alamat"] = alamat
                spotData["namaspot"] = namaspot
                spotData["deskripsispot"] = deskripsispot
                spotData["linkspot"] = linkspot

                spotRef.setValue(spotData)
                    .addOnSuccessListener {
                        Toast.makeText(activity, "Spot Mancing Berhasil diTambahkan", Toast.LENGTH_SHORT).show()
                        binding.slotSpot.visibility = View.GONE
                    }
                    .addOnFailureListener {
                        Toast.makeText(activity, "Gagal menambahkan spot mancing", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
