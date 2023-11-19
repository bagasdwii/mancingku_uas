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
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.example.mancingku.Login
import com.example.mancingku.R
import com.example.mancingku.databinding.FragmentUserBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class UserFragment : Fragment() {
    private var _binding : FragmentUserBinding? = null
    lateinit var auth : FirebaseAuth
    private lateinit var imgUri : Uri
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
            binding.edtName.setText(getUsernameFromEmail(user.email ?: ""))
            binding.edtEmail.setText(user.email)
            loadProfileImage()
            //kondisi email sudah verifikasi atau belum
            if (user.isEmailVerified){
                binding.iconVerify.visibility = View.VISIBLE
                binding.iconNotVerify.visibility = View.GONE
            } else {
                binding.iconVerify.visibility = View.GONE
                binding.iconNotVerify.visibility = View.VISIBLE
            }
        }
        //ke kamera buat ambil gambar
        binding.cviUser.setOnClickListener {
            goToCamera()
        }
        binding.btnLogout.setOnClickListener {
            btnLogout()
        }
        binding.btnVerify.setOnClickListener{
            emailVerification()
        }
        binding.btnChangePass.setOnClickListener {
            changePass()
        }
    }
    private fun changePass() {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        binding.cvCurrentPass.visibility = View.VISIBLE

        binding.btnCancel.setOnClickListener {
            binding.cvCurrentPass.visibility = View.GONE
        }

        binding.btnConfirm.setOnClickListener btnConfirm@{

            val pass = binding.edtCurrentPassword.text.toString()

            if (pass.isEmpty()) {
                binding.edtCurrentPassword.error = "Password Tidak Boleh Kosong"
                binding.edtCurrentPassword.requestFocus()
                return@btnConfirm
            }

            user.let {
                val userCredential = EmailAuthProvider.getCredential(it?.email!!,pass)
                it.reauthenticate(userCredential).addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> {
                            binding.cvCurrentPass.visibility = View.GONE
                            binding.cvUpdatePass.visibility = View.VISIBLE
                        }
                        task.exception is FirebaseAuthInvalidCredentialsException -> {
                            binding.edtCurrentPassword.error = "Password Salah"
                            binding.edtCurrentPassword.requestFocus()
                        }
                        else -> {
                            Toast.makeText(activity, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            binding.btnNewCancel.setOnClickListener {
                binding.cvCurrentPass.visibility = View.GONE
                binding.cvUpdatePass.visibility = View.GONE
            }

            binding.btnNewChange.setOnClickListener newChangePassword@{

                val newPass = binding.edtNewPass.text.toString()
                val passConfirm = binding.edtConfirmPass.text.toString()

                if (newPass.isEmpty()) {
                    binding.edtCurrentPassword.error = "Password Tidak Boleh Kosong"
                    binding.edtCurrentPassword.requestFocus()
                    return@newChangePassword
                }

                if(passConfirm.isEmpty()){
                    binding.edtCurrentPassword.error = "Ulangi Password Baru"
                    binding.edtCurrentPassword.requestFocus()
                    return@newChangePassword
                }

                if (newPass.length < 6) {
                    binding.edtCurrentPassword.error = "Password Harus Lebih dari 6 Karakter"
                    binding.edtCurrentPassword.requestFocus()
                    return@newChangePassword
                }

                if (passConfirm.length < 6) {
                    binding.edtCurrentPassword.error = "Password Tidak Sama"
                    binding.edtCurrentPassword.requestFocus()
                    return@newChangePassword
                }

                if (newPass != passConfirm) {
                    binding.edtCurrentPassword.error = "Password Tidak Sama"
                    binding.edtCurrentPassword.requestFocus()
                    return@newChangePassword
                }

                user?.let {
                    user.updatePassword(newPass).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(activity, "Password Berhasil diUpdate", Toast.LENGTH_SHORT).show()
                            successLogout()
                        } else {
                            Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }
    }
    private fun successLogout() {
        auth = FirebaseAuth.getInstance()
        auth.signOut()

        val intent = Intent(context, Login::class.java)
        startActivity(intent)
        activity?.finish()

        Toast.makeText(activity, "Silahkan Login Kembali", Toast.LENGTH_SHORT).show()
    }
    private fun getUsernameFromEmail(email: String): String {
        return email.substringBefore('@')
    }
    private fun emailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(activity, "Email Verifikasi Telah Dikirim", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun btnLogout() {
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        val intent = Intent(context,Login::class.java)
        startActivity(intent)
        activity?.finish()
    }
    private fun goToCamera() {
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
            REQ_CAM -> {
                // Penanganan hasil pengambilan foto dari kamera
                if (resultCode == Activity.RESULT_OK) {
                    val imgBitmap = data?.extras?.get("data") as? Bitmap
                    imgBitmap?.let {
                        uploadImgToFirebase(imgBitmap)
                        binding.cviUser.setImageBitmap(imgBitmap) // Tampilkan gambar dari kamera ke ImageView
                        // Lakukan sesuatu dengan imgBitmap yang tidak null
                    } ?: run {
                        // Handle kasus ketika imgBitmap null
                    }

                }
            }
            REQ_IMAGE -> {
                // Penanganan hasil pemilihan gambar dari galeri
                if (resultCode == Activity.RESULT_OK && data != null) {
                    imgUri?.let { uri ->
                        val bitmap: Bitmap? = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                        bitmap?.let {
                            uploadImgToFirebase(bitmap)
                            binding.cviUser.setImageBitmap(bitmap)
                        }
                    }
                }
            }
        }
    }

    private fun uploadImgToFirebase(imgBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref = FirebaseStorage.getInstance().reference.child("img_user/${FirebaseAuth.getInstance().currentUser?.email}")
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val img = baos.toByteArray()
        ref.putBytes(img)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    ref.downloadUrl.addOnCompleteListener { Task->
                        Task.result.let{ Uri ->
                            imgUri = Uri
                            binding.cviUser.setImageBitmap(imgBitmap)
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
                .into(binding.cviUser)
        }.addOnFailureListener {
            // Handle kegagalan memuat gambar
        }
    }
    companion object{
        const val REQ_CAM = 100
        const val REQ_IMAGE = 100
    }
}