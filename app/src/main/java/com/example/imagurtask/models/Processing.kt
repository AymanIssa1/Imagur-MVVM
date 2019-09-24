package com.example.imagurtask.models

import com.google.gson.annotations.SerializedName

data class Processing(
    @SerializedName("status") val status: String
)