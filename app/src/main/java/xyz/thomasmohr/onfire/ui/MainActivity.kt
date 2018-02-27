package xyz.thomasmohr.onfire.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.*
import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import xyz.thomasmohr.onfire.R
import xyz.thomasmohr.onfire.data.CounterChange
import xyz.thomasmohr.onfire.util.bindView
import xyz.thomasmohr.onfire.util.plus
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val coordinatorLayout: CoordinatorLayout by bindView(R.id.coordinator_layout)
    private val createButton: View by bindView(R.id.create_button)
    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)

    private val changeRequestRelay = PublishRelay.create<CounterChange>()
    private val counterAdapter = CounterAdapter(this)
    private val linearLayoutManager = LinearLayoutManager(this)
    private var startDisposables = CompositeDisposable()

    private lateinit var viewModel: CounterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(CounterViewModel::class.java)

        with(recyclerView) {
            layoutManager = linearLayoutManager
            adapter = counterAdapter
            itemAnimator = DefaultItemAnimator().apply { supportsChangeAnimations = false }
        }

        itemTouchHelper.attachToRecyclerView(recyclerView)

        // Always guarantee at least one counter, so the app doesn't open blank on a fresh run
        if (savedInstanceState == null) {
            viewModel.counters()
                .take(1)
                .subscribe { if (it.isEmpty()) viewModel.createCounter() }
        }
    }

    override fun onStart() {
        super.onStart()

        // Ensure all database changes happen off the main thread
        startDisposables += changeRequestRelay
            .observeOn(Schedulers.io())
            .subscribe(::onCounterChangeRequested)

        startDisposables += createButton.clicks()
            .map { CounterChange.Create() }
            .subscribe(changeRequestRelay)
        startDisposables += counterAdapter.changes().subscribe(changeRequestRelay)
        startDisposables += counterAdapter.bind(viewModel.counters())

        // Detect when new items are added so we can scroll to them
        // This is admittedly a bit of a hack
        data class ItemIdChanges(val itemIds: Set<Long>, val addedItemIds: Set<Long>)
        startDisposables += viewModel.counters()
            .map { counters -> counters.map { it.id }.toSet() }
            .distinctUntilChanged()
            .scan(ItemIdChanges(
                itemIds = emptySet(),
                addedItemIds = emptySet()
            ), { (itemIds), newItemIds ->
                // Skip the initial add; we only care about *new* items
                if (itemIds.isEmpty()) {
                    ItemIdChanges(newItemIds, emptySet())
                } else {
                    val addedItemIds = newItemIds.toMutableSet()
                    addedItemIds.removeAll(itemIds)
                    ItemIdChanges(newItemIds, addedItemIds)
                }
            })
            .filter { (_, addedItemIds) -> addedItemIds.isNotEmpty() }
            .map { (_, addedItemIds) -> addedItemIds.first() }
            .delay(100, TimeUnit.MILLISECONDS) // Give the adapter time to settle
            .map { counterAdapter.getPosition(it) }
            .filter { it != -1 }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { linearLayoutManager.smoothScrollToPosition(recyclerView, null, it) }
    }

    override fun onStop() {
        super.onStop()
        startDisposables.clear()
    }

    private val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
        UP or DOWN,
        LEFT or RIGHT
    ) {
        private var lastFrom: Int = -1
        private var lastTo: Int = -1

        override fun onMove(
            recyclerView: RecyclerView,
            source: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val from = counterAdapter.getPosition(source.itemId)
            val to = counterAdapter.getPosition(target.itemId)

            // Check if we aren't already in the process of making this swap, since this could be
            // fired multiple times while the animation is running
            if (lastFrom == from && lastTo == to) return false

            lastFrom = from
            lastTo = to
            changeRequestRelay.accept(CounterChange.Move(source.itemId, target.itemId))
            return true
        }

        override fun onSwiped(
            viewHolder: RecyclerView.ViewHolder,
            direction: Int
        ) = changeRequestRelay.accept(CounterChange.Delete(viewHolder.itemId))
    })

    private fun onCounterChangeRequested(change: CounterChange) {
        when (change) {
            is CounterChange.Create -> viewModel.createCounter(name = change.name)
            is CounterChange.Count -> viewModel.modifyCount(change.counterId, change.difference)
            is CounterChange.Name -> viewModel.modifyName(change.counterId, change.name)
            is CounterChange.Move -> viewModel.move(change.fromCounterId, change.toCounterId)
            is CounterChange.Delete -> {
                val counter = viewModel.delete(counterId = change.counterId)

                if (counter != null) {
                    val text: CharSequence = if (counter.name.isNotBlank()) {
                        getString(R.string.named_counter_removed, counter.name)
                    } else {
                        getString(R.string.counter_removed)
                    }

                    Snackbar
                        .make(coordinatorLayout, text, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, {
                            changeRequestRelay.accept(CounterChange.UndoDelete(counter))
                        })
                        .show()
                }
            }
            is CounterChange.UndoDelete -> viewModel.undoDelete(change.counter)
        }
    }
}