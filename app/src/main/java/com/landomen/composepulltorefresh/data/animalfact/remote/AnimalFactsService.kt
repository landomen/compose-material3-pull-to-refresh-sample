package com.landomen.composepulltorefresh.data.animalfact.remote

import com.landomen.composepulltorefresh.data.animalfact.model.AnimalFact
import com.landomen.composepulltorefresh.data.animalfact.remote.model.AnimalFactsApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.request

class AnimalFactsService(private val httpClient: HttpClient) : AnimalFactsRemote {

    override suspend fun getAnimalFacts(): List<AnimalFact> {
        val facts: AnimalFactsApiResponse = httpClient.get("https://dog-api.kinduff.com/api/facts?number=10").body()
        return facts.facts.mapIndexed { id, fact ->
            AnimalFact(id + 1, fact)
        }
    }
}