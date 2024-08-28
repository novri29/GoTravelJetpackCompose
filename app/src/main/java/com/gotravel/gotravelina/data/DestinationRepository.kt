package com.gotravel.gotravelina.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf


class DestinationRepository {
    private val dummyDestination = mutableListOf<Destination>()

    init {
        if (dummyDestination.isEmpty()) {
            DestinationData.dummyDestination.forEach {
                dummyDestination.add(it)
            }
        }
    }

    fun getDestionationById(destinationId: Int): Destination{
        return dummyDestination.first() {
            it.id == destinationId
        }
    }

    fun getDestinationFavorite(): Flow<List<Destination>> {
        return flowOf(dummyDestination.filter { it.isFavorite })
    }

    fun searchDestination(query: String) = flow {
        val data = dummyDestination.filter {
            it.name.contains(query, ignoreCase = true)
        }
        emit(data)
    }

    fun updateDestination(destinationId: Int, newState: Boolean): Flow<Boolean> {
        val index = dummyDestination.indexOfFirst { it.id == destinationId }
        val result = if (index >= 0) {
            val destination = dummyDestination[index]
            dummyDestination[index] = destination.copy(isFavorite = newState)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    companion object {
        @Volatile
        private var instance: DestinationRepository? = null

        fun getInstance(): DestinationRepository = instance ?: synchronized(this) {
            DestinationRepository().apply {
                instance = this
            }
        }
    }
}