package com.example.mancingku.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.mancingku.R
import com.example.mancingku.databinding.FragmentDetail2Binding
import com.example.mancingku.databinding.FragmentDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class DetailFragment2 : Fragment() {
    private var _binding : FragmentDetail2Binding? = null
    private val binding get() = _binding!!
    lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetail2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val admin = "untukrandom130@gmail.com"
        //kondisi user sedang login atau tidak

        //kondisi email sudah verifikasi atau belum
        if (user != null && user.email == admin) {
            binding.bEdit2.visibility = View.VISIBLE
            binding.bHapus2.visibility = View.VISIBLE


        } else {
            binding.bEdit2.visibility = View.GONE
            binding.bHapus2.visibility = View.GONE

        }

        // Mengambil argumen yang dikirim dari navigasi
        val spot = DetailFragment2Args.fromBundle(requireArguments()).spot
//        val namaTempat = arguments?.getString("namaTempat")
//        val alamat = arguments?.getString("alamat")
//        val deskripsi = arguments?.getString("deskripsi")
//        val linkMaps = arguments?.getString("linkMaps")
//        val imgDetail = arguments?.getString("imgDetail")

        // Menemukan TextView berdasarkan ID dari layout
        val tvNamaTempat = binding.tvNamaTempat2
        val tvAlamatLokasi = binding.tvAlamatlokasi2
        val tvDeskripsiTempat = binding.tvDeskripsiTempat2
        val tvLinkTempat = binding.tvLinkTempat2
        val img = binding.imgDetail2

        // Set nilai dari argumen ke TextView yang sesuai
        tvNamaTempat.text = spot.namaspot
        tvAlamatLokasi.text = spot.alamat
        tvDeskripsiTempat.text = spot.deskripsispot
        tvLinkTempat.text = spot.linkspot
//        imgDetail?.let {
//            Glide.with(requireContext())
//                .load(it) // it adalah URL gambar
//                .into(img)
//        }

        // Ambil URL gambar sesuai dengan kunci yang ada di Firebase Realtime Database
        val storageRef = FirebaseStorage.getInstance().reference.child("img_spot/${spot.id}/image.jpg")

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            // Simpan URL gambar ke properti imgURL pada objek spot yang sesuai
            spot.imgURL = uri.toString()
            Glide.with(requireContext())
                .load(spot.imgURL) // it adalah URL gambar
                .into(img)
        }.addOnFailureListener {
            // Handle error jika gagal mengambil URL gambar dari Cloud Storage
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}