package com.landomen.composepulltorefresh.data.animalfact

import com.landomen.composepulltorefresh.data.animalfact.model.AnimalFact
import com.landomen.composepulltorefresh.data.animalfact.remote.AnimalFactsRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class AnimalFactsRemoteRepository(private val animalFactsRemote: AnimalFactsRemote) :
    AnimalFactsRepository {

    private val _animalFacts = MutableStateFlow<List<AnimalFact>>(emptyList())

    override fun observeAnimalFacts(): Flow<List<AnimalFact>> = _animalFacts

    override suspend fun fetchAnimalFacts() {
        withContext(Dispatchers.IO) {
            _animalFacts.value = animalFactsRemote.getAnimalFacts()
        }
    }
}