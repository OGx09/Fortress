package com.example.myapplication.features.repository.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson


/**
 * TODO: User can either use unique password or set a single password ffor all
 */

@Entity
data class PasswordEntity(@ColumnInfo(name ="encryptedData") val encryptedData: String?){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}