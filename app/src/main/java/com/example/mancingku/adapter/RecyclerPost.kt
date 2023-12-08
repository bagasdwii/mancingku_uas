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
import com.example.mancingku.fragment.tokoFragmentDirections
import com.example.mancingku.model.modelPost

import com.example.mancingku.model.modelToko

class RecyclerPost(private val postList: List<modelPost>) :
    RecyclerView.Adapter<RecyclerPost.PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_home, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.bind(post)
//        holder.itemView.setOnClickListener {
//            // Mengakses NavController dan melakukan navigasi ke DetailFragment dengan argumen yang diperlukan
//            val action = tokoFragmentDirections.actionTokoFragmentToDetailFragment(
//                toko.namatoko,
//                toko.alamat,
//                toko.deskripsitoko,
//                toko.linktoko,
//                toko.imgURL
//            )
//            it.findNavController().navigate(action)
//        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtusername: TextView = itemView.findViewById(R.id.edt_emailpost)
        private val profil: ImageView = itemView.findViewById(R.id.cvi_userpost)
        private val gambar: ImageView = itemView.findViewById(R.id.img_status)
        private val txtStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val txtLink: TextView = itemView.findViewById(R.id.tvlokasi)


        fun bind(post: modelPost) {
            txtusername.text = post.username
            Glide.with(itemView)
                .load(post.profil) // post.profil berisi URL gambar
                .into(profil) // profil adalah ImageView
            Glide.with(itemView)
                .load(post.gambar) // post.profil berisi URL gambar
                .into(gambar) // profil adalah ImageView
            txtStatus.text = post.status
            txtLink.text = post.linkpost

        }
    }
}

