package com.example.mancingku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mancingku.R
import com.example.mancingku.fragment.MainFragmentDirections
import com.example.mancingku.fragment.tokoFragmentDirections
import com.example.mancingku.model.modelEvent
import com.example.mancingku.model.modelToko
import com.google.firebase.storage.FirebaseStorage

class Slide(private val slideList: List<modelEvent>) :
    RecyclerView.Adapter<Slide.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.slide_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = slideList[position]
        holder.bind(event)
        holder.itemView.setOnClickListener {
            // Mengakses NavController dan melakukan navigasi ke DetailFragment dengan argumen yang diperlukan
            val action = MainFragmentDirections.actionMainFragmentToDetailEventFragment(
                event
            )
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return slideList.size
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gambarevent: ImageView = itemView.findViewById(R.id.imgevent)
        private val txtAlamat: TextView = itemView.findViewById(R.id.tvNamaevent)

        fun bind(event: modelEvent) {
            val storageRefEvent = FirebaseStorage.getInstance().reference.child("img_event/${event.id}/image.jpg")
            storageRefEvent.downloadUrl.addOnSuccessListener { uri ->
                // Simpan URL gambar ke properti imgURL pada objek spot yang sesuai
                Glide.with(itemView)
                    .load(uri.toString()) // post.profil berisi URL gambar
                    .into(gambarevent) // profil adalah ImageView

            }.addOnFailureListener {
                gambarevent.setImageDrawable(null)
                // Handle error jika gagal mengambil URL gambar dari Cloud Storage
            }
            txtAlamat.text = event.title
        }
    }
}