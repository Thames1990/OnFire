package xyz.thomasmohr.onfire.ui

import android.arch.lifecycle.ViewModel
import io.reactivex.Flowable
import xyz.thomasmohr.onfire.data.Counter
import xyz.thomasmohr.onfire.data.CounterDatabase

class CounterViewModel(private val counterDatabase: CounterDatabase) : ViewModel() {

    fun counters(): Flowable<List<Counter>> = counterDatabase.counterModel().counters()

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