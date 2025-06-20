package com.tanucode.practica2.data.remote.model

import com.google.gson.annotations.SerializedName

data class WeaponDetailDto(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("class")
    var weaponClass: String? = null,
    @SerializedName("sub")
    var sub: String? = null,
    @SerializedName("special")
    var special: String? = null,
    @SerializedName("range")
    var range: Int? = null,
    @SerializedName("damage")
    var damage: Int? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("video")
    var video: String? = null,
    @SerializedName("lat")
    var lat: Double? = null,
    @SerializedName("lon")
    var lon: Double? = null
)
/*
"id": "shooter_quickmiddle_01",
"name": "N-ZAP '89",
"class": "Shooter",
"sub": "Auto bomb",
"special": "Super Chump",
"range": 50,
"damage": 29,
"image": ""
"video" " ""
 */