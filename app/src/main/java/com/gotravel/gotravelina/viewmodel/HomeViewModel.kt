package com.gotravel.gotravelina.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gotravel.gotravelina.data.Destination
import com.gotravel.gotravelina.data.DestinationRepository
import com.gotravel.gotravelina.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: DestinationRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<Destination>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<Destination>>> get() = _uiState

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> get() = _query

    fun search(newQuery: String) = viewModelScope.launch {
        _query.value = newQuery
        repository.searchDestination(_query.value)
            .catch {
                _uiState.value = UiState.Error(it.message.toString())
            }
            .collect {
                _uiState.value = UiState.Success(it)
            }
    }

    fun updateDestination(id: Int, newState: Boolean) = viewModelScope.launch {
        repository.updateDestination(id, newState)
            .collect { isUpdated ->
                if (isUpdated) search(_query.value)
            }
    }
}