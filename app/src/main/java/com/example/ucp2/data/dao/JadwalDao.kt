package com.example.ucp2.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ucp2.data.entity.Dokter
import com.example.ucp2.data.entity.Jadwal
import kotlinx.coroutines.flow.Flow

@Dao
interface JadwalDao {

    @Insert
    suspend fun insertJadwal(jadwal: Jadwal)

    @Query("SELECT * FROM Jadwal ORDER BY NmPasien ASC")
    fun getAllJadwal(): Flow<List<Jadwal>>

    @Query("SELECT * FROM jadwal WHERE id = :id")
    fun getJadwal(id: String): Flow<Jadwal>

    @Query("SELECT * FROM dokter ORDER BY nama DESC")
    fun getAllNmDokter(): Flow<List<Dokter>>

    @Delete
    suspend fun deleteJadwal(jadwal: Jadwal)

    @Update
    suspend fun updateJadwal(jadwal: Jadwal)
}