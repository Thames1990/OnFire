package xyz.thomasmohr.onfire.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import xyz.thomasmohr.onfire.R
import xyz.thomasmohr.onfire.data.Counter
import xyz.thomasmohr.onfire.data.CounterChange
import xyz.thomasmohr.onfire.util.bindView

class CounterViewHolder(context: Context, parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(context).inflate(R.layout.counter, parent, false)
) {

    companion object {
        private val DUMMY_COUNTER = Counter(-1)
    }

    private val count: TextView by bindView(R.id.count)
    private val counterName: EditText by bindView(R.id.counter_name)
    private val minusButton: Button by bindView(R.id.minus_button)
    private val plusButton: Button by bindView(R.id.plus_button)

    private var counter: Counter = DUMMY_COUNTER
    private lateinit var listener: Listener

    init {
        plusButton.setOnClickListener {
            listener.onCounterChange(CounterChange.Count(counterId = counter.id, difference = 1))
        }

        minusButton.setOnClickListener {
            listener.onCounterChange(CounterChange.Count(counterId = counter.id, difference = -1))
        }

        with(counterName) {
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

        this.listener = listener
        this.counter = counter

        if (counterName.text.toString() != counter.name) {
            counterName.setText(counter.name)
        }
        count.text = counter.count.toString()
    }

    fun detach() {
        if (counterName.hasFocus()) counterName.clearFocus()
    }

    interface Listener {
        fun onCounterChange(counterChange: CounterChange)
    }
}