package com.oscar.estatehubcompose.perfil.data.network

import android.util.Log
import com.oscar.estatehubcompose.helpers.DataStoreManager
import com.oscar.estatehubcompose.perfil.data.network.response.PropiedadesResponse
import com.oscar.estatehubcompose.perfil.data.network.response.UsuarioResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PerfilService @Inject constructor(private var perfilClient: PerfilClient,
                                        private val dataStoreManager: DataStoreManager) {



    suspend fun getUsuario(): UsuarioResponse?{

        try{

            return withContext(Dispatchers.IO){
                val token = dataStoreManager.getToken().first();
                val idUsuario = dataStoreManager.getIdUsuario().first();
                val parsedToken = "Bearer ${token}"
                val response = perfilClient.getUsuario(parsedToken, idUsuario)
                response.body();
            }

        }catch (e: Exception){
            Log.e("OSCAR", "Error: ${e}")
            return null;
        }


    }


    suspend fun getPropiedades(): PropiedadesResponse?{
        try{

            return withContext(Dispatchers.IO){
                val token = dataStoreManager.getToken().first();
                val idUsuario = dataStoreManager.getIdUsuario().first();
                val parsedToken = "Bearer ${token}";
                val response = perfilClient.getPropiedadesByUsuario(parsedToken, idUsuario);
                response.body();
            }

        }catch (e: Exception){
            Log.e("OSCAR", "Error: ${e}")
            return null;
        }
    }
}