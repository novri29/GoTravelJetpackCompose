package com.gotravel.gotravelina.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gotravel.gotravelina.data.Destination
import com.gotravel.gotravelina.data.DestinationRepository
import com.gotravel.gotravelina.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: DestinationRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<Destination>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<Destination>>
        get() = _uiState

    fun getDestinationById(destinationId: Int) = viewModelScope.launch {
        _uiState.value = UiState.Loading
        _uiState.value = UiState.Success(repository.getDestionationById(destinationId))
    }

    fun updateDestination(id: Int, newState: Boolean) = viewModelScope.launch {
        repository.updateDestination(id, !newState)
            .collect { isUpdated ->
                if (isUpdated) getDestinationById(id)
            }
    }
}