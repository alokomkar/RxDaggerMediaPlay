package com.alokomkar.mediaplay.commons

import com.alokomkar.core.application.CoreApp
import com.alokomkar.mediaplay.ui.main.details.di.DaggerDetailsComponent
import com.alokomkar.mediaplay.ui.main.details.di.DetailsComponent
import com.alokomkar.mediaplay.ui.main.list.di.DaggerListComponent
import com.alokomkar.mediaplay.ui.main.list.di.ListComponent
import javax.inject.Singleton

@Singleton
object ComponentHolder {
    private var listComponent: ListComponent? = null
    private var detailsComponent: DetailsComponent? = null

    fun listComponent(): ListComponent {
        if (listComponent == null)
            listComponent = DaggerListComponent.builder().coreComponent(CoreApp.coreComponent).build()
        return listComponent as ListComponent
    }

    fun destroyListComponent() {
        listComponent = null
    }

    fun detailsComponent(): DetailsComponent {
        if (detailsComponent == null)
            detailsComponent = DaggerDetailsComponent.builder().listComponent(listComponent()).build()
        return detailsComponent as DetailsComponent
    }

    fun destroyDetailsComponent() {
        detailsComponent = null
    }
}