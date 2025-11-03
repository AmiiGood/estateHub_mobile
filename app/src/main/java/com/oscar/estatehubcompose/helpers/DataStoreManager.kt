package com.oscar.estatehubcompose.helpers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    companion object{
        private const val DNAME = "tokenDataStore"
        private val TOKEN = stringPreferencesKey("token")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DNAME);

    suspend fun guardarToken(token:String){
        context.dataStore.edit { preferences -> preferences[TOKEN] = token }
    };

    fun getToken(): Flow<String?>{
        return context.dataStore.data.map { preferences -> preferences[TOKEN] }
    }

    suspend fun clearToken(){
        context.dataStore.edit { preferences ->  preferences.remove(TOKEN) }
    }

}