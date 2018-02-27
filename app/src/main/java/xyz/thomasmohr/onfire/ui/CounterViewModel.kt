package xyz.thomasmohr.onfire.ui

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import xyz.thomasmohr.onfire.CounterApplication
import xyz.thomasmohr.onfire.data.Counter
import xyz.thomasmohr.onfire.data.CounterDatabase
import javax.inject.Inject

class CounterViewModel constructor(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var counterDatabase: CounterDatabase

    init {
        (application as CounterApplication).appComponent.inject(this)
    }

    fun hasCounters() = counterDatabase.counterModel().count() != 0

    fun counters() = counterDatabase.counterModel().counters()

    fun createCounter(name: String = "") = counterDatabase.counterModel().createCounter(name)

    fun undoDelete(counter: Counter) = counterDatabase.counterModel().insertOrUpdate(counter)

    fun modifyCount(
        counterId: Long,
        difference: Long
    ) = counterDatabase.counterModel().modifyCount(counterId, difference)

    fun modifyName(
        counterId: Long,
        name: String
    ) = counterDatabase.counterModel().modifyName(counterId, name)

    fun move(
        fromCounterId: Long,
        toCounterId: Long
    ) = counterDatabase.counterModel().move(fromCounterId, toCounterId)

    fun delete(counterId: Long): Counter? {
        counterDatabase.counterModel().counter(counterId)?.let { counter ->
            counterDatabase.counterModel().delete(counter)
            return counter
        }

        return null
    }
}