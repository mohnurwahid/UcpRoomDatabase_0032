package com.example.ucp2.ui

import android.app.Application
import com.example.ucp2.data.Dependenciesinjection.ContainerAppDr
import com.example.ucp2.data.Dependenciesinjection.ContainerAppJadwal

class RumahSakitApp : Application() {
    lateinit var containerAppDokter: ContainerAppDr
    lateinit var containerAppJadwal: ContainerAppJadwal

    override fun onCreate() {
        super.onCreate()
        containerAppDokter = ContainerAppDr(this)
        containerAppJadwal = ContainerAppJadwal(this)
    }

}