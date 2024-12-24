package com.example.ucp2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jadwal")
data class Jadwal(
    @PrimaryKey
    val id: String,
    val NmDokter: String,
    val NmPasien: String,
    val noHp: String,
    val tanggal: String,
    val status: String
)