package xyz.thomasmohr.onfire

import android.app.Application
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.applicationContext
import xyz.thomasmohr.onfire.data.CounterDatabase
import xyz.thomasmohr.onfire.ui.CounterViewModel

class CounterApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }

}

private val appModule = applicationContext {
    provide { CounterDatabase.createPersistentDatabase(get()) }
    viewModel { CounterViewModel(get()) }
}