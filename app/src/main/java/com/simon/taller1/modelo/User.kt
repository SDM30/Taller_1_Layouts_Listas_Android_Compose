package com.simon.taller1.modelo

import kotlinx.serialization.Serializable

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
)
