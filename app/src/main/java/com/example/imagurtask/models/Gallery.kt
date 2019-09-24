package com.example.imagurtask.models


import com.google.gson.annotations.SerializedName

data class Gallery(
    @SerializedName("data") val `data`: ArrayList<GalleryData>,
    @SerializedName("success") val success: Boolean,
    @SerializedName("status") val status: Int
)