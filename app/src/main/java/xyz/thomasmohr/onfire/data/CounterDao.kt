package xyz.thomasmohr.onfire.data

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Dao
abstract class CounterDao {

    companion object {
        private const val POSITION_OFFSET = 262144
    }

    @Query("SELECT COUNT(*) FROM counters")
    abstract fun count(): Int

    @Query("SELECT * FROM counters ORDER BY position")
    abstract fun counters(): Flowable<List<Counter>>

    @Query("SELECT * FROM counters WHERE id = :id")
    abstract fun counter(id: Long): Counter?

    @Query("SELECT position FROM counters WHERE id = :id")
    abstract fun position(id: Long): Long

    @Query("SELECT position FROM counters WHERE position < :position ORDER BY position DESC LIMIT 1")
    abstract fun previousPosition(position: Long): Long

    @Query("SELECT position FROM counters WHERE position > :position ORDER BY position ASC LIMIT 1")
    abstract fun nextPosition(position: Long): Long

    @Query("SELECT position FROM counters ORDER BY position DESC LIMIT 1")
    abstract fun lastPosition(): Long

    @Query("UPDATE counters SET count = (count + :difference) WHERE id = :counterId")
    abstract fun modifyCount(counterId: Long, difference: Long)

    @Query("UPDATE counters SET name = :name WHERE id = :counterId")
    abstract fun modifyName(counterId: Long, name: String)

    @Query("UPDATE counters SET position = :position WHERE id = :counterId")
    abstract fun modifyPosition(counterId: Long, position: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertOrUpdate(vararg counters: Counter)

    @Delete
    abstract fun delete(counter: Counter)

    fun createCounter(name: String) = insertOrUpdate(
        Counter(id = 0, name = name, position = lastPosition() + POSITION_OFFSET)
    )

    /**
     * Move a counter
     *
     * Positioning is done in such a way that the database doesn't have to be rewritten each time
     * a change occurs, just the single item that is being moved.
     */
    fun move(fromCounterId: Long, toCounterId: Long) {
        val fromPosition = position(fromCounterId)
        val toPosition = position(toCounterId)

        val newPosition: Long = if (fromPosition < toPosition) {
            val nextPosition = nextPosition(toPosition)
            if (nextPosition == 0L) toPosition + POSITION_OFFSET
            else (nextPosition + toPosition) / 2L
        } else {
            val previousPosition = previousPosition(toPosition)
            if (previousPosition == 0L) toPosition / 2L
            else (previousPosition + toPosition) / 2L
        }

        modifyPosition(fromCounterId, newPosition)
    }

}