package com.tanucode.practica2.data.remote.model

import com.google.gson.annotations.SerializedName

data class WeaponDto(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("image")
    var image: String? = null,
)
