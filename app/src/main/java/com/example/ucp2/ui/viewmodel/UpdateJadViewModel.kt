package com.example.ucp2.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Jadwal
import com.example.ucp2.repository.RepositoryJadwal
import com.example.ucp2.ui.navigation.RouteNavigation
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UpdateJadViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryJadwal: RepositoryJadwal
) : ViewModel() {

    var updateJadUIState by mutableStateOf(JadUiState())
        private set

    private val _id: String = checkNotNull(savedStateHandle[RouteNavigation.DestinasiEditJad.id])

    init {
        viewModelScope.launch {
            updateJadUIState = repositoryJadwal.getJadwal(_id)
                .filterNotNull()
                .first()
                .toUIStateMhs()
        }
    }

    fun updateJadState(jadwalEvent: JadwalEvent) {
        updateJadUIState = updateJadUIState.copy(
            JadwalEvent = jadwalEvent,
        )
    }

    fun validateFields(): Boolean {
        val event = updateJadUIState.JadwalEvent
        val errorJadState = FormErrorJadwalState(
            id = if (event.id.isNotEmpty()) null else "Id Tidak Boleh Kosong",
            NmDokter = if (event.NmDokter.isNotEmpty()) null else "Nama Dokter Tidak Boleh Kosong",
            NmPasien = if (event.NmPasien.isNotEmpty()) null else "Nama Pasien Tidak Boleh Kosong",
            noHp = if (event.noHp.isNotEmpty()) null else "Nomor Hp Tidak Boleh Kosong",
            tanggal = if (event.tanggal.isNotEmpty()) null else "Tanggal Tidak Boleh Kosong",
            status = if (event.status.isNotEmpty()) null else "Status Tidak Boleh Kosong",
        )

        updateJadUIState = updateJadUIState.copy(isEntryValid = errorJadState)
        return errorJadState.isValid()
    }

    fun updateData() {
        val currentEvent = updateJadUIState.JadwalEvent

        if (validateFields()) {
            viewModelScope.launch {
                try {
                    repositoryJadwal.updateJadwal(currentEvent.toJadwalEntity())
                    updateJadUIState = updateJadUIState.copy(
                        snackbarMessage = "Data Berhasil Diupdate",
                        JadwalEvent = JadwalEvent(),
                        isEntryValid = FormErrorJadwalState(),
                    )
                    println("snackBarMessage Diatur: ${updateJadUIState.snackbarMessage}")
                } catch (e: Exception) {
                    updateJadUIState = updateJadUIState.copy(
                        snackbarMessage = "Data Gagal Diupdate"
                    )
                }
            }
        } else  {
            updateJadUIState = updateJadUIState.copy(
                snackbarMessage = "Data Gagal Diupdate"
            )
        }
    }

    fun resetSnackBarMessage() {
        updateJadUIState = updateJadUIState.copy(snackbarMessage = null)
    }
}

fun Jadwal.toUIStateMhs(): JadUiState = JadUiState(
    JadwalEvent = this.toDetailUiEvent(),
)