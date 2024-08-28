package com.gotravel.gotravelina.di

import com.gotravel.gotravelina.data.DestinationRepository

object Injection {
    fun provideRepository(): DestinationRepository {
        return DestinationRepository.getInstance()
    }
}