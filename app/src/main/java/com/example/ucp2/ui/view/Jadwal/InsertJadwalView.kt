package com.example.ucp2.ui.view.Jadwal


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2.ui.customwidget.DynamicSelectedTextField
import com.example.ucp2.ui.customwidget.TopAppBar
import com.example.ucp2.ui.navigation.RouteNavigation
import com.example.ucp2.ui.viewmodel.FormErrorJadwalState
import com.example.ucp2.ui.viewmodel.JadUiState
import com.example.ucp2.ui.viewmodel.JadwalEvent
import com.example.ucp2.ui.viewmodel.JadwalViewModel
import com.example.ucp2.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormJadwal(
    jadwalEvent: JadwalEvent = JadwalEvent(),
    onValueChange: (JadwalEvent) -> Unit = { },
    errorJadwalState: FormErrorJadwalState,
    viewModel: JadwalViewModel = viewModel(factory = PenyediaViewModel.Factory),
    modifier: Modifier = Modifier
) {
    var chosenDropdown by remember { mutableStateOf("") }

    var PilihDokter by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = jadwalEvent.id,
            onValueChange = { onValueChange(jadwalEvent.copy(id = it)) },
            label = { Text(text = "ID") },
            isError = errorJadwalState.id != null
        )

        // Dropdown for Spesialis
        LaunchedEffect (Unit) {
            viewModel.listDokter.collect { dokterList ->
                PilihDokter = dokterList.map { it.nama }
            }
        }

        DynamicSelectedTextField(
            selectedValue = chosenDropdown,
            options = PilihDokter,
            label = "Pilih Nama Dokter",
            onValueChangedEvent = {
                onValueChange(jadwalEvent.copy(NmDokter = it))
                chosenDropdown = it
            }
        )
        Text(
            text = errorJadwalState.NmDokter ?: "",
            color = Color.Red
        )

        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = jadwalEvent.NmPasien,
            onValueChange = { onValueChange(jadwalEvent.copy(NmPasien = it)) },
            label = { Text(text = "Nama Pasien") },
            isError = errorJadwalState.NmPasien != null
        )
        Text(text = errorJadwalState.NmPasien ?: "", color = Color.Red)

        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = jadwalEvent.noHp,
            onValueChange = { onValueChange(jadwalEvent.copy(noHp = it)) },
            label = { Text(text = "Nomor Hp") },
            isError = errorJadwalState.noHp != null
        )
        Text(text = errorJadwalState.noHp ?: "", color = Color.Red)

        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = jadwalEvent.tanggal,
            onValueChange = { onValueChange(jadwalEvent.copy(tanggal = it)) },
            label = { Text(text = "Tanggal Konsultasi") },
            isError = errorJadwalState.tanggal != null
        )
        Text(text = errorJadwalState.tanggal?: "", color = Color.Red)

        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = jadwalEvent.status,
            onValueChange = { onValueChange(jadwalEvent.copy(status = it)) },
            label = { Text(text = "Status") },
            isError = errorJadwalState.status != null
        )
        Text(text = errorJadwalState.status?: "", color = Color.Red)
    }
}


@Composable
fun InsertBodyJadwal(
    modifier: Modifier = Modifier,
    onValueChange: (JadwalEvent) -> Unit,
    JUiState: JadUiState,
    onClick: () -> Unit
){
    Column  (
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        FormJadwal(
            jadwalEvent = JUiState.JadwalEvent,
            onValueChange = onValueChange,
            errorJadwalState = JUiState.isEntryValid,
            modifier = modifier.fillMaxWidth()
        )
        Button(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0XFF000080)
            )
        ){
            Text(text = "Simpan")
        }
    }
}

object DestinasiInsertJad: RouteNavigation {
    override val route : String = "insert_Jad"
}

@Composable
fun InsertJadView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JadwalViewModel = viewModel(factory = PenyediaViewModel.Factory)
){
    val JUiState = viewModel.JUiState
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(JUiState.snackbarMessage) {
        JUiState.snackbarMessage?.let { message ->
            coroutineScope.launch {
                snackBarHostState.showSnackbar(message)
                viewModel.resetSnackbarMessage()
            }
        }
    }
    Scaffold (
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }

    ){padding ->
        Column (
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ){
            TopAppBar(
                onBack = onBack,
                showBackButton = true,
                judul = "Tambah Jadwal"
            )
            InsertBodyJadwal(
                JUiState = JUiState,
                onValueChange = {updateEvent ->
                    viewModel.updateJadState(updateEvent)
                },onClick = {
                    coroutineScope.launch {
                        viewModel.saveData()
                    }
                    onNavigate()
                }
            )
        }
    }
}