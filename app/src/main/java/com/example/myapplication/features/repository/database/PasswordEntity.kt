package com.example.myapplication.features.repository.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * TODO: User can either use unique password or set a single password ffor all
 */

@Entity
data class PasswordEntity(@PrimaryKey(autoGenerate = true) var id: Int = 0,
                          @ColumnInfo(name = "platform_website") val platformWebsite: String?,
                          @ColumnInfo(name = "platform_logo") val platformLogo: String?,
                          @ColumnInfo(name = "platform_password") val platformPassword: String?,
                          @ColumnInfo(name = "platform_name") val platformName: String?,
                          @ColumnInfo(name = "buzz_word") val buzzWord: String?
)