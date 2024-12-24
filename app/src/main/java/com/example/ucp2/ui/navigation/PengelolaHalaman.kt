package com.example.ucp2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ucp2.ui.view.Dokter.DestinasiInsertDokter
import com.example.ucp2.ui.view.Dokter.HomeDokterView
import com.example.ucp2.ui.view.Dokter.InsertDokterView
import com.example.ucp2.ui.view.Jadwal.DestinasiInsertJadwal

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = AlamatNavigation.DestinasiHomeDokter.route) {
        composable(AlamatNavigation.DestinasiHomeDokter.route) {
            HomeDokterView(
                onAddDokter = {
                    navController.navigate(DestinasiInsertDokter.route)
                },
                onAddJad = {
                    navController.navigate(AlamatNavigation.DestinasiHomeJadwal.route) // Mengarahkan ke halaman Jadwal
                },
                modifier = modifier
            )
        }
        composable(route = DestinasiInsertDokter.route) {
            InsertDokterView(
                onBack = { navController.popBackStack() },
                onNavigate = { navController.popBackStack() },
                modifier = modifier
            )
        }
        composable(route = AlamatNavigation.DestinasiHomeJadwal.route) {
            HomeJadwalView(
                onBack = { navController.popBackStack() },
                onDetailClick = { id ->
                    navController.navigate("${AlamatNavigation.DestinasiDetail.route}/$id")
                },
                onAddJad = {
                    navController.navigate(DestinasiInsertJadwal.route)
                },
                modifier = modifier
            )
        }

        composable(
            route = DestinasiInsertJadwal.route
        ) {
            InsertJadwalView(
                onBack = {
                    navController.popBackStack()
                },
                onNavigate = {
                    navController.popBackStack()
                },
                modifier = modifier,
            )
        }

        composable(
            AlamatNavigation.DestinasiDetail.routeWithArgs,
            arguments = listOf(
                navArgument(AlamatNavigation.DestinasiDetail.id) {
                    type = NavType.StringType
                }
            )
        ) {
            val id = it.arguments?.getString(RouteNavigation.DestinasiDetailJad.id)
            id?.let { id ->
                DetailJadView(
                    onBack = {
                        navController.popBackStack()
                    },
                    onEditClick = {
                        navController.navigate("${RouteNavigation.DestinasiEditJad.route}/$it")
                    },
                    modifier = modifier,
                    onDeleteClick = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(
            AlamatNavigation.DestinasiEdit.routeWithArgs,
            arguments = listOf(
                navArgument(AlamatNavigation.DestinasiEdit.id) {
                    type = NavType.StringType
                }
            )
        ) {
            UpdateJadView(
                onBack = {
                    navController.popBackStack()
                },
                onNavigate = {
                    navController.popBackStack()
                },
                modifier = modifier,
            )
        }
    }
}