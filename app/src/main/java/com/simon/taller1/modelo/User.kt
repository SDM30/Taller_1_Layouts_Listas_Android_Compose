package com.simon.taller1.modelo

import android.os.Parcelable
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize

@Parcelize
@Serializable
data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val gender: String,
    val email: String,
    val phone: String,
    val company: Company,
    val height: Double,
    val weight: Double,
    val university: String,
    val image: String
) : Parcelable
