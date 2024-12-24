package com.example.ucp2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Jadwal
import com.example.ucp2.repository.RepositoryJadwal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class HomeJadViewModel(
    private val repositoryJadwal: RepositoryJadwal
) : ViewModel() {

    val homeJadUiState: StateFlow<HomeJadUiState> = repositoryJadwal.getAllJadwal()
        .filterNotNull()
        .map {
            HomeJadUiState(
                listJad = it.toList(),
                isLoading = false,
            )
        }
        .onStart {
            emit(HomeJadUiState(isLoading = true))
            delay(900)
        }
        .catch {
            emit(
                HomeJadUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = it.message ?: "Terjadi Kesalahan"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeJadUiState(
                isLoading = true
            )
        )

}

data class HomeJadUiState(
    val listJad: List<Jadwal> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)