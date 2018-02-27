package xyz.thomasmohr.onfire

import android.app.Application

class CounterApplication : Application() {

    val appComponent: AppComponent = DaggerAppComponent.builder()
        .androidModule(AndroidModule(this))
        .build()

}