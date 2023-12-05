package com.example.mancingku.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.mancingku.R
import com.example.mancingku.databinding.FragmentDetailBinding
import com.google.firebase.auth.FirebaseAuth


class DetailFragment : Fragment() {
    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!
    lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
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
            binding.bEdit.visibility = View.VISIBLE
            binding.bHapus.visibility = View.VISIBLE


        } else {
            binding.bEdit.visibility = View.GONE
            binding.bHapus.visibility = View.GONE

        }

        // Mengambil argumen yang dikirim dari navigasi
        val namaTempat = arguments?.getString("namaTempat")
        val alamat = arguments?.getString("alamat")
        val deskripsi = arguments?.getString("deskripsi")
        val linkMaps = arguments?.getString("linkMaps")
        val imgDetail = arguments?.getString("imgDetail")

        // Menemukan TextView berdasarkan ID dari layout
        val tvNamaTempat = binding.tvNamaTempat
        val tvAlamatLokasi = binding.tvAlamatlokasi
        val tvDeskripsiTempat = binding.tvDeskripsiTempat
        val tvLinkTempat = binding.tvLinkTempat
        val img = binding.imgDetail

        // Set nilai dari argumen ke TextView yang sesuai
        tvNamaTempat.text = namaTempat
        tvAlamatLokasi.text = alamat
        tvDeskripsiTempat.text = deskripsi
        tvLinkTempat.text = linkMaps
        imgDetail?.let {
            Glide.with(requireContext())
                .load(it) // it adalah URL gambar
                .into(img)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
