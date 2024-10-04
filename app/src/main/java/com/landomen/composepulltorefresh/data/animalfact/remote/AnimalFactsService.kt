package com.landomen.composepulltorefresh.data.animalfact.remote

import com.landomen.composepulltorefresh.data.animalfact.model.AnimalFact
import com.landomen.composepulltorefresh.data.animalfact.remote.model.AnimalFactsApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class AnimalFactsService(private val httpClient: HttpClient) : AnimalFactsRemote {

    override suspend fun getAnimalFacts(): List<AnimalFact> {
        val facts = httpClient.get<AnimalFactsApiResponse>("https://dog-api.kinduff.com/api/facts?number=10")
        return facts.facts.mapIndexed { id, fact ->
            AnimalFact(id, fact)
        }
    }
}