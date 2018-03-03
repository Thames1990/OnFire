package xyz.thomasmohr.onfire.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.helper.ItemTouchHelper.*
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.architecture.ext.viewModel
import xyz.thomasmohr.onfire.R
import xyz.thomasmohr.onfire.data.Counter
import xyz.thomasmohr.onfire.data.CounterChange
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val changeRequestRelay = PublishRelay.create<CounterChange>()
    private val counterAdapter = CounterAdapter()
    private val linearLayoutManager = LinearLayoutManager(this)
    private var startDisposables = CompositeDisposable()
    private val mediaPlayer = MediaPlayer()

    private val viewModel by viewModel<CounterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(recycler_view) {
            layoutManager = linearLayoutManager
            adapter = counterAdapter
            itemAnimator = DefaultItemAnimator().apply { supportsChangeAnimations = false }

            val callback: ItemTouchHelper.Callback = DragManageAdapter(
                dragDirs = UP or DOWN,
                swipeDirs = LEFT or RIGHT
            )
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(this)
        }

        // Always guarantee at least one counter, so the app doesn't open blank on a fresh run
        if (savedInstanceState == null) {
            viewModel.counters()
                .take(1)
                .subscribe { if (it.isEmpty()) viewModel.createCounter() }
        }
    }

    override fun onStart() {
        super.onStart()

        with(startDisposables) {
            // Ensure all database changes happen off the main thread
            add(changeRequestRelay.observeOn(Schedulers.io()).subscribe(::onCounterChangeRequested))
            add(create_button.clicks().map { CounterChange.Create() }.subscribe(changeRequestRelay))
            add(counterAdapter.changes().subscribe(changeRequestRelay))
            // Detect when new items are added so we can scroll to them
            // This is admittedly a bit of a hack
            data class ItemIdChanges(val itemIds: Set<Long>, val addedItemIds: Set<Long>)
            add(counterAdapter.bind(viewModel.counters()))
            add(viewModel.counters()
                .map { counters -> counters.map { it.id }.toSet() }
                .distinctUntilChanged()
                .scan(ItemIdChanges(
                    itemIds = emptySet(),
                    addedItemIds = emptySet()
                ), { (itemIds), newItemIds ->
                    // Skip the initial add; we only care about *new* items
                    if (itemIds.isEmpty()) ItemIdChanges(newItemIds, emptySet())
                    else {
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
                .subscribe {
                    linearLayoutManager.smoothScrollToPosition(
                        recycler_view,
                        null,
                        it
                    )
                })
        }
    }

    override fun onStop() {
        super.onStop()
        startDisposables.clear()
    }

    private fun onCounterChangeRequested(change: CounterChange) {
        when (change) {
            is CounterChange.Create -> viewModel.createCounter(name = change.name)
            is CounterChange.Count -> {
                viewModel.modifyCount(change.counterId, change.difference)

                val counter: Counter? = viewModel.counter(change.counterId)
                if (counter != null) {
                    val soundName = "russ_bray_${counter.count}"
                    val soundId = resources.getIdentifier(soundName, "raw", packageName)

                    if (soundId == 0) return // Sound file is not available
                    val sound = resources.openRawResourceFd(soundId)

                    with(mediaPlayer) {
                        reset()
                        setDataSource(sound.fileDescriptor, sound.startOffset, sound.length)
                        prepare()
                        start()
                    }

                    sound.close()
                }
            }
            is CounterChange.Name -> viewModel.modifyName(change.counterId, change.name)
            is CounterChange.Move -> viewModel.move(change.fromCounterId, change.toCounterId)
            is CounterChange.Delete -> {
                val counter = viewModel.delete(counterId = change.counterId)

                if (counter != null) {
                    val text: CharSequence = if (counter.name.isNotBlank()) {
                        getString(R.string.snackbar_named_counter_removed, counter.name)
                    } else {
                        getString(R.string.snackbar_counter_removed)
                    }

                    Snackbar
                        .make(coordinator_layout, text, Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_undo, {
                            changeRequestRelay.accept(CounterChange.UndoDelete(counter))
                        })
                        .show()
                }
            }
            is CounterChange.UndoDelete -> viewModel.undoDelete(change.counter)
        }
    }

    private inner class DragManageAdapter(
        dragDirs: Int,
        swipeDirs: Int
    ) : SimpleCallback(dragDirs, swipeDirs) {

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

    }

}
