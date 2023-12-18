package com.example.mancingku.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.mancingku.R
import com.example.mancingku.databinding.FragmentDetail2Binding
import com.example.mancingku.databinding.FragmentDetailEventBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class DetailEventFragment : Fragment() {
    private var _binding: FragmentDetailEventBinding? = null
    private val binding get() = _binding!!
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailEventBinding.inflate(inflater, container, false)
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
            binding.bEditevent.visibility = View.VISIBLE
            binding.bHapusevent.visibility = View.VISIBLE


        } else {
            binding.bEditevent.visibility = View.GONE
            binding.bHapusevent.visibility = View.GONE

        }

        // Mengambil argumen yang dikirim dari navigasi
        val event = DetailEventFragmentArgs.fromBundle(requireArguments()).event
//        val namaTempat = arguments?.getString("namaTempat")
//        val alamat = arguments?.getString("alamat")
//        val deskripsi = arguments?.getString("deskripsi")
//        val linkMaps = arguments?.getString("linkMaps")
//        val imgDetail = arguments?.getString("imgDetail")

        // Menemukan TextView berdasarkan ID dari layout
        val tvNamaevent = binding.tvjudul
        val tvAlamatevent = binding.tvAlamatevent
        val tvDeskripsievent = binding.tvDeskripsievent
        val tvLinkevent = binding.tvLinkevent
        val img = binding.imgDetailEvent

        // Set nilai dari argumen ke TextView yang sesuai
        tvNamaevent.text = event.title
        tvAlamatevent.text = event.alamat
        tvDeskripsievent.text = event.deskripsievent
        tvLinkevent.text = event.linkevent
//        imgDetail?.let {
//            Glide.with(requireContext())
//                .load(it) // it adalah URL gambar
//                .into(img)
//        }

        // Ambil URL gambar sesuai dengan kunci yang ada di Firebase Realtime Database
        val storageRef =
            FirebaseStorage.getInstance().reference.child("img_event/${event.id}/image.jpg")

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            // Simpan URL gambar ke properti imgURL pada objek spot yang sesuai
            event.imgURL = uri.toString()
            Glide.with(requireContext())
                .load(event.imgURL) // it adalah URL gambar
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
