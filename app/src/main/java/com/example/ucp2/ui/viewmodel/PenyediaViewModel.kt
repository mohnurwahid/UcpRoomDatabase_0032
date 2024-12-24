package com.example.ucp2.ui.viewmodel

package com.example.ucp2.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ucp2.IRMApp

object PenyediaViewModel{
    val Factory = viewModelFactory {

        initializer {
            DokterViewModel(
                IRMApp().containerAppDr.repositoryDr
            )
        }

        initializer {
            HomeDrViewModel(
                IRMApp().containerAppDr.repositoryDr
            )
        }

        initializer {
            JadwalViewModel(
                IRMApp().containerAppJadwal.repositoryJadwal
            )
        }

        initializer {
            HomeJadViewModel(
                IRMApp().containerAppJadwal.repositoryJadwal
            )
        }

        initializer {
            DetailJadViewModel(
                createSavedStateHandle(),
                IRMApp().containerAppJadwal.repositoryJadwal
            )
        }

        initializer {
            UpdateJadViewModel(
                createSavedStateHandle(),
                IRMApp().containerAppJadwal.repositoryJadwal
            )
        }
    }
}
fun CreationExtras.IRMApp(): IRMApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as IRMApp)