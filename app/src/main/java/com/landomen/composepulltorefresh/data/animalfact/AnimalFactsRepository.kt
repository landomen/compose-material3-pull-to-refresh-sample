package com.landomen.composepulltorefresh.data.animalfact

import com.landomen.composepulltorefresh.data.animalfact.model.AnimalFact
import kotlinx.coroutines.flow.Flow

interface AnimalFactsRepository {

    fun observeAnimalFacts(): Flow<List<AnimalFact>>

    suspend fun fetchAnimalFacts()
}
