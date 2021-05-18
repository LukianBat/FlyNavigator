package com.lukianbat.tickets.common.di

interface TicketsFlowComponentController {
    fun provideTicketsFlowComponent(): TicketsFlowComponent

    fun clearTicketsFlowComponent()
}
