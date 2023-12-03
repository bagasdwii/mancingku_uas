package com.example.mancingku.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mancingku.R
import com.example.mancingku.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {
    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengambil argumen yang dikirim dari navigasi
        val namaTempat = arguments?.getString("namaTempat")
        val alamat = arguments?.getString("alamat")
        val deskripsi = arguments?.getString("deskripsi")
        val linkMaps = arguments?.getString("linkMaps")

        // Menemukan TextView berdasarkan ID dari layout
        val tvNamaTempat = binding.tvNamaTempat
        val tvAlamatLokasi = binding.tvAlamatlokasi
        val tvDeskripsiTempat = binding.tvDeskripsiTempat
        val tvLinkTempat = binding.tvLinkTempat

        // Set nilai dari argumen ke TextView yang sesuai
        tvNamaTempat.text = namaTempat
        tvAlamatLokasi.text = alamat
        tvDeskripsiTempat.text = deskripsi
        tvLinkTempat.text = linkMaps
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
