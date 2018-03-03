package xyz.thomasmohr.onfire.ui

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.counter.*
import xyz.thomasmohr.onfire.data.Counter
import xyz.thomasmohr.onfire.data.CounterChange

class CounterViewHolder(
    override val containerView: View?
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    companion object {
        private val DUMMY_COUNTER = Counter(-1)
    }

    private var counter: Counter = DUMMY_COUNTER

    private lateinit var listener: Listener

    init {
        plus_button.setOnClickListener {
            listener.onCounterChange(CounterChange.Count(counterId = counter.id, difference = 1))
        }

        minus_button.setOnClickListener {
            listener.onCounterChange(CounterChange.Count(counterId = counter.id, difference = -1))
        }

        with(counter_name) {
            setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // If you try to clear focus immediately it doesn't work
                    v.postDelayed({ v.clearFocus() }, 0)
                }
                false
            }
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) = listener.onCounterChange(
                    CounterChange.Name(counterId = counter.id, name = s.toString())
                )

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) = Unit
            })
        }
    }

    fun bind(counter: Counter, listener: Listener) {
        if (this.counter == counter) return
        this.counter = counter

        this.listener = listener

        if (counter_name.text.toString() != counter.name) {
            counter_name.setText(counter.name)
        }
        count.text = counter.count.toString()
    }

    fun detach() {
        if (counter_name.hasFocus()) counter_name.clearFocus()
    }

    interface Listener {
        fun onCounterChange(counterChange: CounterChange)
    }
}