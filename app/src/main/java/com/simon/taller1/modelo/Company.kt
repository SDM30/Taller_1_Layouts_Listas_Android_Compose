package com.simon.taller1.modelo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Company(
    val name: String
) : Parcelable
