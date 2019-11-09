package com.alokomkar.mediaplay.ui.main.details.di

import com.alokomkar.mediaplay.ui.main.details.DetailsFragment
import com.alokomkar.mediaplay.ui.main.list.di.ListComponent
import dagger.Component
import dagger.Module

@DetailsScope
@Component(dependencies = [ListComponent::class], modules = [DetailsModule::class])
interface DetailsComponent {
    fun inject(detailsFragment: DetailsFragment)
}

@Module
class DetailsModule {

}