package com.example.ucp2.ui.navigation

interface RouteNavigation {
    val route: String

    object DestinasiHomeDr : RouteNavigation {
        override val route: String = "home"
    }

    object DestinasiHomeJad : RouteNavigation {
        override val route = "jadwal"
    }
    object DestinasiDetailJad : RouteNavigation {
        override val route = "detail"
        const val id = "id"
        val routeWithArgs = "$route/{$id}"
    }

    object DestinasiUpdateJad : RouteNavigation {
        override val route = "update"
        const val id = "id"
        val routeWithArgs = "$route/{$id}"
    }

    object DestinasiEditJad : RouteNavigation {
        override val route = "edit"
        const val id = "id"
        val routeWithArgs = "$route/{$id}"
    }
}