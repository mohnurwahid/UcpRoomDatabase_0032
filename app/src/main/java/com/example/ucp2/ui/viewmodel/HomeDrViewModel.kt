package com.example.ucp2.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Dokter
import com.example.ucp2.repository.RepositoryDokter

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class HomeDrViewModel(
    private val repositoryDr: RepositoryDokter
) : ViewModel() {

    val homeDrUiState: StateFlow<HomeDrUiState> = repositoryDr.getAllDokter()
        .filterNotNull()
        .map {
            HomeDrUiState(
                listDr = it.toList(),
                isLoading = false,
            )
        }
        .onStart {
            emit(HomeDrUiState(isLoading = true))
            delay(900)
        }
        .catch {
            emit(
                HomeDrUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = it.message ?: "Terjadi Kesalahan"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeDrUiState(
                isLoading = true
            )
        )

}

data class HomeDrUiState(
    val listDr: List<Dokter> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)