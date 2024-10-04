package com.landomen.composepulltorefresh.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.landomen.composepulltorefresh.data.animalfact.AnimalFactsRepository
import com.landomen.composepulltorefresh.data.animalfact.model.AnimalFact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val animalFactsRepository: AnimalFactsRepository) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val screenState: StateFlow<HomeScreenState> = animalFactsRepository.observeAnimalFacts()
        .combine(_isRefreshing) { items, isRefreshing ->
            HomeScreenState(items = items, isRefreshing = isRefreshing)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeScreenState()
        )

    init {
        viewModelScope.launch {
            animalFactsRepository.fetchAnimalFacts()
        }
    }

    fun onPullToRefreshTrigger() {
        _isRefreshing.update { true }
        viewModelScope.launch {
            animalFactsRepository.fetchAnimalFacts()
            _isRefreshing.update { false }
        }
    }
}

data class HomeScreenState(
    val items: List<AnimalFact> = listOf(),
    val isRefreshing: Boolean = false
)