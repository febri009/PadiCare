package com.example.padicare.edukasi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artikel(
    var judul: String,
    var sumber: String,
    var cover: String,
    var konten: String,
) : Parcelable
