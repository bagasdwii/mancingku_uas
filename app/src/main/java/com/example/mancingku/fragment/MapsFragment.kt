package com.example.mancingku.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import com.example.mancingku.R
import com.example.mancingku.databinding.FragmentMapsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.UUID


class MapsFragment : Fragment() {
    private var _binding : FragmentMapsBinding? = null
    private val binding get() = _binding!!
    lateinit var auth : FirebaseAuth
    fun generateUniqueKey(): String {
        return UUID.randomUUID().toString()
    }
    private lateinit var database: DatabaseReference
    val uniqueKey = generateUniqueKey()
    private lateinit var imgUri : Uri
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spotMancing.setOnClickListener {
            val navController = findNavController(view)
            navController.navigate(MainFragmentDirections.actionMainFragmentToSpotPancingFragment())
        }
        binding.tokoPancing.setOnClickListener {
            val navController = findNavController(view)
            navController.navigate(MainFragmentDirections.actionMainFragmentToTokoFragment())
        }
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val admin = "untukrandom130@gmail.com"
        //kondisi user sedang login atau tidak
        if (user != null) {
            binding.welcomeMessageTextView.setText("Selamat Datang " + getUsernameFromEmail(user.email ?: ""))
        }
        if (user != null && user.email == admin) {
            binding.btnAdd.visibility = View.VISIBLE


        } else {
            binding.btnAdd.visibility = View.GONE

        }
        binding.btnAdd.setOnClickListener {
            TambahEvent()
        }

    }
    private fun getUsernameFromEmail(email: String): String {
        return email.substringBefore('@')
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun TambahEvent() {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        database = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://mancingku-fa98f-default-rtdb.firebaseio.com/")

        binding.slotEvent.visibility = View.VISIBLE
        binding.imgEvent.setOnClickListener {
            goToImg()
        }
        binding.btnCancelevent.setOnClickListener {
            binding.slotEvent.visibility = View.GONE
        }

        binding.btnConfirmevent.setOnClickListener {
            val userEmail = user?.email // Mengambil email dari currentUser
            val title = binding.edtTitleevent.text.toString()
            val alamatevent = binding.edtAlamatevent.text.toString()
            val namaevent = binding.edtNamaevent.text.toString()
            val deskripsievent = binding.edtDeskripsievent.text.toString()
            val linkevent = binding.edtLinkevent.text.toString()


            if (userEmail.isNullOrEmpty() || title.isEmpty()|| alamatevent.isEmpty() || namaevent.isEmpty() || deskripsievent.isEmpty() || linkevent.isEmpty()) {
                Toast.makeText(
                    activity,
                    "Ada Data Yang Masih Kosong!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val eventRef = database.child("event-mancing").child(uniqueKey) // Menggunakan push untuk membuat key unik

                val eventData = HashMap<String, Any>()
                eventData["user"] = userEmail
                eventData["title"] = title
                eventData["alamat"] = alamatevent
                eventData["namaevent"] = namaevent
                eventData["deskripsievent"] = deskripsievent
                eventData["linkevent"] = linkevent

                eventRef.setValue(eventData)
                    .addOnSuccessListener {
                        Toast.makeText(activity, "Event Berhasil diTambahkan", Toast.LENGTH_SHORT).show()
                        binding.slotEvent.visibility = View.GONE
                    }
                    .addOnFailureListener {
                        Toast.makeText(activity, "Gagal menambahkan Event mancing", Toast.LENGTH_SHORT).show()
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
                startActivityForResult(chooser, UserFragment.REQ_IMAGE)
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
                        binding.imgEvent.setImageBitmap(imgBitmap) // Tampilkan gambar dari kamera ke ImageView
                        // Lakukan sesuatu dengan imgBitmap yang tidak null
                    } ?: run {
                        // Handle kasus ketika imgBitmap null
                    }

                }
            }
            UserFragment.REQ_IMAGE -> {
                // Penanganan hasil pemilihan gambar dari galeri
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data.data)
                    bitmap?.let {
                        uploadImgToFirebase(bitmap)
                        binding.imgEvent.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
    private fun uploadImgToFirebase(imgBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref = FirebaseStorage.getInstance().reference.child("img_event/$uniqueKey/image.jpg")

        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val img = baos.toByteArray()
        ref.putBytes(img)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    ref.downloadUrl.addOnCompleteListener { Task->
                        Task.result.let{ Uri ->
                            imgUri = Uri
                            binding.imgEvent.setImageBitmap(imgBitmap)
                        }
                    }
                }
            }
    }
}
