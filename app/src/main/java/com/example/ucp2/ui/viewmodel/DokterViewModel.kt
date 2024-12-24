package com.example.ucp2.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Dokter
import com.example.ucp2.repository.RepositoryDokter

import kotlinx.coroutines.launch

class DokterViewModel(
    private val repositoryDr: RepositoryDokter
) : ViewModel() {
    var DrUiState by mutableStateOf(DrUiState())

    fun updateUiState(dokterEvent: DokterEvent){
        DrUiState = DrUiState.copy(
            DokterEvent = dokterEvent,
        )
    }
    private  fun validateFields() : Boolean{
        val event = DrUiState.DokterEvent
        val errorState = FormErrorState(
            id = if (event.id.isEmpty()) "Id tidak boleh kosong" else null,
            nama = if (event.nama.isEmpty()) "Nama tidak boleh kosong" else null,
            spesialis = if (event.spesialis.isEmpty()) "Spesialis tidak boleh kosong" else null,
            klinik = if (event.klinik.isEmpty()) "Klinik tidak boleh kosong" else null,
            noHp = if (event.noHp.isEmpty()) "Nomor Hp tidak boleh kosong" else null,
            jamPraktik = if (event.jamPraktik.isEmpty()) "Jam Praktik tidak boleh kosong" else null
        )
        DrUiState = DrUiState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }
    fun saveData() {

        val currentEvent = DrUiState.DokterEvent

        if (validateFields()) {
            viewModelScope.launch {
                try {
                    repositoryDr.insertDokter(currentEvent.toDokterEntity())
                    DrUiState = DrUiState.copy(
                        snackbarMessage = "Data berhasil disimpan",
                        DokterEvent = DokterEvent(),
                        isEntryValid = FormErrorState()
                    )
                } catch (e: Exception) {
                    DrUiState = DrUiState.copy(
                        snackbarMessage = "Data gagal disimpan"
                    )
                }
            }
        }else {
            DrUiState = DrUiState.copy(
                snackbarMessage = "Data tidak valid. Periksa kembali data Anda."
            )
        }
    }
    //reset pesan snackbar setelah ditampilkan
    fun resetSnackBarMessage() {
        DrUiState = DrUiState.copy(
            snackbarMessage = null
        )
    }
}
//data input form
data class DokterEvent(
    var id: String = "",
    var nama: String = "",
    var spesialis: String = "",
    var klinik: String = "",
    var noHp: String = "",
    var jamPraktik: String = ""
)

//menyimpan input ke dalam entity
fun DokterEvent.toDokterEntity(): Dokter = Dokter(
    id = id,
    nama = nama,
    spesialis = spesialis,
    klinik = klinik,
    noHp = noHp,
    jamPraktik = jamPraktik
)

data class FormErrorState(
    val id : String? = null,
    val nama : String? = null,
    val spesialis : String? = null,
    val klinik : String? = null,
    val noHp : String? = null,
    val jamPraktik : String? = null
){
    fun isValid(): Boolean {
        return id == null &&
                nama == null &&
                spesialis == null &&
                klinik == null &&
                noHp == null &&
                jamPraktik == null
    }
}

data class DrUiState(
    val DokterEvent: DokterEvent = DokterEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
    val snackbarMessage: String? = null
)