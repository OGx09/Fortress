package com.example.myapplication.utils
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.BuildConfig
import com.example.myapplication.features.main.MainActivity
import kotlin.reflect.jvm.kotlinFunction


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "username_store")

object DragonLog{
    fun Any.spit(tag: String = this.javaClass.simpleName, string: String){
        if(BuildConfig.DEBUG){
            //val clazz = MainActivity::class.java
            Log.d(tag, string)
        }
    }
}