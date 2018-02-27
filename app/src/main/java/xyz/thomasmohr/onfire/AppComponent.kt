package xyz.thomasmohr.onfire

import dagger.Component
import xyz.thomasmohr.onfire.data.DataModule
import xyz.thomasmohr.onfire.ui.CounterViewModel
import javax.inject.Singleton

@Component(modules = [AndroidModule::class, DataModule::class])
@Singleton
interface AppComponent {
    fun inject(into: CounterViewModel)
}