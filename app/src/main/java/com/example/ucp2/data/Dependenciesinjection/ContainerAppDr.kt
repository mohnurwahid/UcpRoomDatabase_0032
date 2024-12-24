package com.example.ucp2.data.Dependenciesinjection


import android.content.Context
import com.example.ucp2.data.database.RsDatabase
import com.example.ucp2.repository.RepositoryDokter
import com.example.ucp2.repository.localRepositoryDokter


interface InterfaceDokter {
    val repositoryDr : RepositoryDokter
}

class ContainerAppDr(private val context: Context) : InterfaceDokter {
    override val repositoryDr: RepositoryDokter by lazy {
        localRepositoryDokter(RsDatabase.getDatabase(context).dokterDao())
    }
}
