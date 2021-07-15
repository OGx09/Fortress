package com.example.myapplication.repository.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GroInvestmentResponse {
    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("icons")
    @Expose
    var icons: List<Icon>? = null
}

class Icon {
    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("width")
    @Expose
    var width: Int? = null

    @SerializedName("height")
    @Expose
    var height: Int? = null

    @SerializedName("format")
    @Expose
    var format: String? = null

    @SerializedName("bytes")
    @Expose
    var bytes: Int? = null

    @SerializedName("error")
    @Expose
    var error: Any? = null

    @SerializedName("sha1sum")
    @Expose
    var sha1sum: String? = null
}
