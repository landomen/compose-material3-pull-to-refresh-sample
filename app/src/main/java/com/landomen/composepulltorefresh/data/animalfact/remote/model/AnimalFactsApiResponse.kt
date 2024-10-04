package com.landomen.composepulltorefresh.data.animalfact.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AnimalFactsApiResponse(
    val facts: List<String>
)
