package com.example.padicare.edukasi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.padicare.R
import com.example.padicare.databinding.ItemRowCafeBinding

class ListCafeAdapter(var listArtikel: ArrayList<Artikel>) : RecyclerView.Adapter<ListCafeAdapter.ListViewHolder>() {

    var filteredList: List<Artikel> = listArtikel.toList()

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: ItemRowCafeBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowCafeBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (judul, sumber, cover) = filteredList[position]

        Glide.with(holder.itemView.context)
            .load(cover)
            .error(R.drawable.padicare)
            .into(holder.binding.imgItemCover)

        holder.apply {
            binding.tvItemJudul.text = judul
            binding.tvItemSumber.text = sumber
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(filteredList[holder.adapterPosition])
            }
        }
    }

    fun filterList(query: String) {
        filteredList = if (query.isEmpty()) {
            listArtikel.toList()
        } else {
            listArtikel.filter { item ->
                item.judul.contains(query, ignoreCase = true)
            }.toList()
        }
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = filteredList.size

    interface OnItemClickCallback {
        fun onItemClicked(data: Artikel)
    }
}