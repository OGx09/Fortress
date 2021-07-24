package com.example.myapplication.data

import com.google.gson.Gson

data class FortressModel(val platformPassword: String?, val userName: String?,
                          val otherInfo: String?, val icon: String?){

    override fun toString(): String {
        return Gson().toJson(this)
    }
}