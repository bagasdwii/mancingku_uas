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
import com.bumptech.glide.Glide
import com.example.mancingku.R
import com.example.mancingku.databinding.FragmentPostBinding
import com.example.mancingku.fragment.UserFragment.Companion.REQ_IMAGE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.UUID


class PostFragment : Fragment() {
    private var _binding : FragmentPostBinding? = null
    fun generateUniqueKey(): String {
        return UUID.randomUUID().toString()
    }
    var uniqueKey = generateUniqueKey()
    lateinit var auth : FirebaseAuth
    private lateinit var imgUri : Uri
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        //kondisi user sedang login atau tidak
        if (user != null) {
            binding.usernamePost.setText(getUsernameFromEmail(user.email ?: ""))
            binding.emailPost.setText(user.email)
            loadProfileImage()
        }
        binding.btnConfirm.setOnClickListener {
            Posting()
        }

        binding.img.setOnClickListener {
            Gambar()
        }
    }

    private fun Posting() {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        database = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://mancingku-fa98f-default-rtdb.firebaseio.com/")

        val userEmail = user?.email // Mengambil email dari currentUser
        val status = binding.edtStatus.text.toString()
        val linkpost = binding.edtLink.text.toString()


        if (userEmail.isNullOrEmpty() || linkpost.isEmpty()) {
            Toast.makeText(
                activity,
                "Ada Data Yang Masih Kosong!!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val postRef = database.child("post-mancing").child(uniqueKey) // Menggunakan push untuk membuat key unik

            val postData = HashMap<String, Any>()
            postData["user"] = userEmail
            postData["status"] = status
            postData["linkpost"] = linkpost

            postRef.setValue(postData)
                .addOnSuccessListener {
                    Toast.makeText(activity, "Spot Mancing Berhasil diTambahkan", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener {
                    Toast.makeText(activity, "Gagal menambahkan spot mancing", Toast.LENGTH_SHORT).show()
                }
            uniqueKey = generateUniqueKey()
            binding.edtStatus.setText("")
            binding.edtLink.setText("")
            binding.img.setImageResource(R.drawable.img_white)

        }
    }
    private fun getUsernameFromEmail(email: String): String {
        return email.substringBefore('@')
    }
    private fun Gambar() {
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
                        binding.img.setImageBitmap(imgBitmap) // Tampilkan gambar dari kamera ke ImageView
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
                        binding.img.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
    private fun uploadImgToFirebase(imgBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref = FirebaseStorage.getInstance().reference.child("img_post/$uniqueKey/image.jpg")

        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val img = baos.toByteArray()
        ref.putBytes(img)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    ref.downloadUrl.addOnCompleteListener { Task->
                        Task.result.let{ Uri ->
                            imgUri = Uri
                            binding.img.setImageBitmap(imgBitmap)
                        }
                    }
                }
            }
    }
    private fun loadProfileImage() {
        val ref = FirebaseStorage.getInstance().reference.child("img_user/${FirebaseAuth.getInstance().currentUser?.email}")
        ref.downloadUrl.addOnSuccessListener { uri ->
            // Menggunakan library seperti Picasso atau Glide untuk memuat gambar ke ImageView
            // Contoh menggunakan Glide
            Glide.with(this)
                .load(uri)
                .into(binding.cviUserpost)
        }.addOnFailureListener {
            // Handle kegagalan memuat gambar
        }
    }
}