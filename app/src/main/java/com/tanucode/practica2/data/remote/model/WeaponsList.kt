package com.tanucode.practica2.data.remote.model

import com.google.gson.annotations.SerializedName

data class WeaponsList(
    @SerializedName("data")
    var weapons: List<WeaponDto>
)
