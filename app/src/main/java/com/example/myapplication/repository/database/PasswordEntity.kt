package com.example.myapplication.repository.database

import android.util.SparseArray
import androidx.annotation.VisibleForTesting
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.myapplication.data.FortressModel
import com.google.gson.Gson


/**
 * TODO: User can either use unique password or set a single password ffor all
 */

@Entity
data class PasswordEntity(@PrimaryKey(autoGenerate = true) val id : Int?,
                          @ColumnInfo var websiteName: String,
                          @ColumnInfo var website: String,
                          @ColumnInfo(name ="encryptedData") var encryptedData: String?=null,
                          @ColumnInfo var iconBytes: String){


    @Ignore var fortressModel: FortressModel? = null

    override fun toString(): String {
        return Gson().toJson(this)
    }

    companion object{

//        fun fakeQueryDb(): SparseArray<PasswordEntity>{
//
//            val sparseArray = SparseArray<PasswordEntity>().apply {
//                for (id in 0..4 ){
//                    put(id, PasswordEntity(i, "Google.com", "Google Search",
//                        "someEncryptedData", otherInfo = "Hello world!!", "randomstring"))
//                }
//            }
//        }
//
//        @VisibleForTesting
//        fun mockGetAll(){
//
//            mutableListOf<PasswordEntity>().apply{
//
//            }
//        }

        @VisibleForTesting
        fun getMock() = PasswordEntity(0, "Google.com", "Google Search",
            "someEncryptedData",  "randomstring")
    }

}