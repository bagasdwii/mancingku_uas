//package com.example.mancingku.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.navigation.findNavController
//import androidx.recyclerview.widget.RecyclerView
//import com.example.mancingku.R
//import com.example.mancingku.fragment.tokoFragmentDirections
//import com.example.mancingku.model.modelEvent
//import com.example.mancingku.model.modelToko
//
//class Slide(private val slideList: List<modelEvent>) :
//    RecyclerView.Adapter<Slide.EventViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.fragment_maps, parent, false)
//        return EventViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: TokoViewHolder, position: Int) {
//        val toko = tokoList[position]
//        holder.bind(toko)
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
//    }
//
//    override fun getItemCount(): Int {
//        return tokoList.size
//    }
//
//    inner class TokoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val txtNamatoko: TextView = itemView.findViewById(R.id.tvNamaLokasi)
//        private val txtAlamat: TextView = itemView.findViewById(R.id.tvAlamat)
//
//        fun bind(toko: modelToko) {
//            txtNamatoko.text = toko.namatoko
//            txtAlamat.text = toko.alamat
//        }
//    }
//}