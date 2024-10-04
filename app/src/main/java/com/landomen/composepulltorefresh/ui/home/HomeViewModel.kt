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

    private val _isLoading = MutableStateFlow(false)

    private val _screenState = MutableStateFlow(HomeScreenState())
    val screenState: StateFlow<HomeScreenState> = animalFactsRepository.observeAnimalFacts()
        .combine(_isLoading) { items, isLoading ->
            HomeScreenState(items = items, isLoading = isLoading)
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
        _screenState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            animalFactsRepository.fetchAnimalFacts()
            _screenState.update { it.copy(isLoading = false) }
        }
    }
}

data class HomeScreenState(
    val items: List<AnimalFact> = listOf(),
    val isLoading: Boolean = false
)