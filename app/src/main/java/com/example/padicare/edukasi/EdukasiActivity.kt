package com.example.padicare.edukasi

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.padicare.R
import com.example.padicare.databinding.ActivityEdukasiBinding

class EdukasiActivity : AppCompatActivity() {

    private var list = ArrayList<Artikel>()

    private lateinit var binding: ActivityEdukasiBinding
    private lateinit var adapter: ListCafeAdapter
    private lateinit var searchViewListener: SearchView.OnQueryTextListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEdukasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvCafe.setHasFixedSize(true)

        list.addAll(getListCafe())
        showRecyclerList()

        searchViewListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    adapter.filterList(it)
                }
                return true
            }
        }
        binding.searchView.setOnQueryTextListener(searchViewListener)
    }

    @SuppressLint("Recycle")
    private fun getListCafe(): ArrayList<Artikel>{
        val datajudul = resources.getStringArray(R.array.data_judul)
        val dataCover = resources.getStringArray(R.array.data_cover)
        val dataSumber = resources.getStringArray(R.array.data_sumber)
        val dataKonten = resources.getStringArray(R.array.data_konten)

        val listArtikel = ArrayList<Artikel>()

        for (i in datajudul.indices) {
            val artikel = Artikel(datajudul[i], dataSumber[i], dataCover[i], dataKonten[i])

            listArtikel.add(artikel)
        }

        return listArtikel
    }

    private fun showRecyclerList() {
        binding.rvCafe.layoutManager = LinearLayoutManager(this)
        adapter = ListCafeAdapter(list)
        binding.rvCafe.adapter = adapter

        adapter.setOnItemClickCallback((object : ListCafeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Artikel) {
                showSelectedCafe(data)
            }
        }))
    }

    private fun showSelectedCafe(artikel: Artikel) {
        val moveWithDataIntent = Intent(this@EdukasiActivity, DetailActivity::class.java)
        moveWithDataIntent.apply {
            putExtra(DetailActivity.EXTRA_JUDUL, artikel.judul)
            putExtra(DetailActivity.EXTRA_KONTEN, artikel.konten)
            putExtra(DetailActivity.EXTRA_COVER, artikel.cover)
            putExtra(DetailActivity.EXTRA_SUMBER, artikel.sumber)
        }
        startActivity(moveWithDataIntent)
    }

}