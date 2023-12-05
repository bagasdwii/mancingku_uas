package com.example.mancingku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mancingku.R
import com.example.mancingku.model.modelSpotMancing
import com.example.mancingku.fragment.SpotPancingFragmentDirections

class RecyclerSpotManvcingAdapter(private val spotList: List<modelSpotMancing>) :
    RecyclerView.Adapter<RecyclerSpotManvcingAdapter.SpotMancingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpotMancingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_main, parent, false)
        return SpotMancingViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpotMancingViewHolder, position: Int) {
        val spot = spotList[position]
        holder.bind(spot)
        holder.itemView.setOnClickListener {
            // Mengakses NavController dan melakukan navigasi ke DetailFragment dengan argumen yang diperlukan
            val action = SpotPancingFragmentDirections.actionSpotPancingFragmentToDetailFragment(
                spot.namaspot,
                spot.alamat,
                spot.deskripsispot,
                spot.linkspot,
                spot.imgURL
            )
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return spotList.size
    }

    inner class SpotMancingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtNamaSpot: TextView = itemView.findViewById(R.id.tvNamaLokasi)
        private val txtAlamat: TextView = itemView.findViewById(R.id.tvAlamat)

        fun bind(spot: modelSpotMancing) {
            txtNamaSpot.text = spot.namaspot
            txtAlamat.text = spot.alamat

        }
    }
}

