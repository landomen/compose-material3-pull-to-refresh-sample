package com.landomen.composepulltorefresh.data.animalfact.remote

import com.landomen.composepulltorefresh.data.animalfact.model.AnimalFact

interface AnimalFactsRemote {

    suspend fun getAnimalFacts(): List<AnimalFact>
}