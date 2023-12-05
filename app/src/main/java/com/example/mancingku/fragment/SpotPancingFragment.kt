package com.example.mancingku.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.example.mancingku.fragment.UserFragment.Companion.REQ_IMAGE
import com.example.mancingku.model.modelSpotMancing
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.UUID
import kotlin.random.Random


class SpotPancingFragment : Fragment() {
    private var _binding : FragmentSpotPancingBinding? = null
    fun generateUniqueKey(): String {
        return UUID.randomUUID().toString()
    }
    val uniqueKey = generateUniqueKey()
    lateinit var auth : FirebaseAuth
    private lateinit var imgUri : Uri
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var adapter: RecyclerSpotManvcingAdapter
//    private lateinit var spotList: MutableList<modelSpotMancing>

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
//        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
//        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
//
//        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
//        actionBar?.apply {
//            setDisplayHomeAsUpEnabled(true) // Menampilkan tombol back
//            title = "" // Mengatur judul toolbar
//        }

        val databaseReference = FirebaseDatabase.getInstance().getReference("spot-mancing")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val spotList = mutableListOf<modelSpotMancing>()

                for (snapshot in dataSnapshot.children) { val key = snapshot.ref.key
                    val alamat = snapshot.child("alamat").getValue(String::class.java) ?: ""
                    val namaspot = snapshot.child("namaspot").getValue(String::class.java) ?: ""
                    val deskripsi = snapshot.child("deskripsispot").getValue(String::class.java) ?: ""
                    val linkMaps = snapshot.child("linkspot").getValue(String::class.java) ?: ""

                    val spot = modelSpotMancing(alamat, namaspot, deskripsi, linkMaps, "")
                    spotList.add(spot)

                    // Ambil URL gambar sesuai dengan kunci yang ada di Firebase Realtime Database
                    val storageRef = FirebaseStorage.getInstance().reference.child("img_spot/${snapshot.key}/image.jpg")

                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Simpan URL gambar ke properti imgURL pada objek spot yang sesuai
                        spot.imgURL = uri.toString()
                    }.addOnFailureListener {
                        // Handle error jika gagal mengambil URL gambar dari Cloud Storage
                    }
                }

                // Gunakan spotList dalam RecyclerViewAdapter
                val adapter = RecyclerSpotManvcingAdapter(spotList)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error jika terjadi pembatalan pengambilan data dari Realtime Database
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
        binding.imgSpot.setOnClickListener {
            goToImg()
        }
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
                val spotRef = database.child("spot-mancing").child(uniqueKey) // Menggunakan push untuk membuat key unik

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

    private fun goToImg() {
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        val chooser = Intent.createChooser(intentGallery, "Select Image")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intentCamera))

        activity?.packageManager?.let {
            chooser.resolveActivity(it)?.also {
                startActivityForResult(chooser, REQ_IMAGE)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            UserFragment.REQ_CAM -> {
                // Penanganan hasil pengambilan foto dari kamera
                if (resultCode == Activity.RESULT_OK) {
                    val imgBitmap = data?.extras?.get("data") as? Bitmap
                    imgBitmap?.let {
                        uploadImgToFirebase(imgBitmap)
                        binding.imgSpot.setImageBitmap(imgBitmap) // Tampilkan gambar dari kamera ke ImageView
                        // Lakukan sesuatu dengan imgBitmap yang tidak null
                    } ?: run {
                        // Handle kasus ketika imgBitmap null
                    }

                }
            }
            REQ_IMAGE -> {
                // Penanganan hasil pemilihan gambar dari galeri
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data.data)
                    bitmap?.let {
                        uploadImgToFirebase(bitmap)
                        binding.imgSpot.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
    private fun uploadImgToFirebase(imgBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref = FirebaseStorage.getInstance().reference.child("img_spot/$uniqueKey/image.jpg")

        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val img = baos.toByteArray()
        ref.putBytes(img)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    ref.downloadUrl.addOnCompleteListener { Task->
                        Task.result.let{ Uri ->
                            imgUri = Uri
                            binding.imgSpot.setImageBitmap(imgBitmap)
                        }
                    }
                }
            }
    }
}
