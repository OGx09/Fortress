package com.example.myapplication.repository.models

import com.google.gson.Gson

data class FortressModel(val platformPassword: String?, val userName: String?,
                          val otherInfo: String?
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}