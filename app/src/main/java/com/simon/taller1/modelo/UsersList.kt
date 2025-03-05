package com.simon.taller1.modelo

import kotlinx.serialization.Serializable

@Serializable
data class UsersList(
    val users: List<User>
)
