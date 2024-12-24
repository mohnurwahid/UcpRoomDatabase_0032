package com.example.ucp2.ui.viewmodel

package com.example.ucp2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Dokter
import com.example.ucp2.data.entity.Jadwal
import com.example.ucp2.repository.RepositoryJadwal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class JadwalViewModel(
    private val repositoryJadwal: RepositoryJadwal,
) : ViewModel(){

    val listDokter: Flow<List<Dokter>> = repositoryJadwal.getAllNmDokter()
    var JUiState by mutableStateOf(JadUiState())


    fun updateJadState(jadwalEvent: JadwalEvent){
        JUiState = JUiState.copy(
            JadwalEvent = jadwalEvent,
        )

    }
    private  fun validateFields() : Boolean{
        val event = JUiState.JadwalEvent
        val errorJState = FormErrorJadwalState(
            id = if (event.id.isEmpty()) "Id tidak boleh kosong" else null,
            NmDokter = if (event.NmDokter.isEmpty()) "Nama Dokter tidak boleh kosong" else null,
            NmPasien = if (event.NmPasien.isEmpty()) "Nama Pasien tidak boleh kosong" else null,
            noHp = if (event.noHp.isEmpty()) "Nomor Hp tidak boleh kosong" else null,
            tanggal = if (event.tanggal.isEmpty()) "Tanggal tidak boleh kosong" else null,
            status = if (event.status.isEmpty()) "Status tidak boleh kosong" else null
        )
        JUiState = JUiState.copy(isEntryValid = errorJState)
        return errorJState.isValid()
    }

    fun saveData() {

        val currentEvent = JUiState.JadwalEvent

        if (validateFields()) {
            viewModelScope.launch {
                try {
                    repositoryJadwal.insertJadwal(currentEvent.toJadwalEntity())
                    JUiState = JUiState.copy(
                        snackbarMessage = "Data berhasil disimpan",
                        JadwalEvent = JadwalEvent(),
                        isEntryValid = FormErrorJadwalState()
                    )
                } catch (e: Exception) {
                    JUiState = JUiState.copy(
                        snackbarMessage = "Data gagal disimpan"
                    )
                }
            }
        }else {
            JUiState = JUiState.copy(
                snackbarMessage = "Data tidak valid. Periksa kembali data Anda."
            )
        }
    }
    //reset pesan snackbar setelah ditampilkan
    fun resetSnackbarMessage() {
        JUiState = JUiState.copy(
            snackbarMessage = null
        )
    }
}

data class JadwalEvent(
    val id: String = "",
    val NmDokter: String = "",
    val NmPasien: String = "",
    val noHp: String = "",
    val tanggal: String = "",
    val status: String = ""
)

fun JadwalEvent.toJadwalEntity(): Jadwal = Jadwal(
    id = id,
    NmDokter = NmDokter,
    NmPasien = NmPasien,
    noHp = noHp,
    tanggal = tanggal,
    status = status
)

data class FormErrorJadwalState(
    val id: String? = null,
    val NmDokter: String? = null,
    val NmPasien: String? = null,
    val noHp: String? = null,
    val tanggal: String? = null,
    val status: String? = null
){
    fun isValid(): Boolean {
        return id == null &&
                NmDokter == null &&
                NmPasien == null &&
                noHp == null &&
                tanggal == null &&
                status == null
    }
}

data class JadUiState(
    val JadwalEvent: JadwalEvent = JadwalEvent(),
    val isEntryValid: FormErrorJadwalState = FormErrorJadwalState(),
    val snackbarMessage: String? = null
)