package com.example.ucp2.data.Dependenciesinjection

import com.example.ucp2.data.database.RsDatabase
import android.content.Context
import com.example.ucp2.repository.RepositoryJadwal
import com.example.ucp2.repository.localRepositoryJadwal

interface InterfaceJadwal{
    val repositoryJadwal: RepositoryJadwal
}

class ContainerAppJadwal(private val context: Context) : InterfaceJadwal{
    override val repositoryJadwal: RepositoryJadwal by lazy {
        localRepositoryJadwal(RsDatabase.getDatabase(context).jadwalDao())
    }
}
