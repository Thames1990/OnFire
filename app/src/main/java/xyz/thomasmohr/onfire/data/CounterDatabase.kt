package xyz.thomasmohr.onfire.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [Counter::class], version = 1)
abstract class CounterDatabase : RoomDatabase() {

    abstract fun counterModel(): CounterDao

    companion object {
        private const val DB_NAME = "counter.db"

        fun createInMemoryDatabase(
            context: Context
        ): CounterDatabase = Room.inMemoryDatabaseBuilder(
            context.applicationContext,
            CounterDatabase::class.java
        ).build()

        fun createPersistentDatabase(context: Context): CounterDatabase = Room.databaseBuilder(
            context.applicationContext,
            CounterDatabase::class.java,
            DB_NAME
        ).build()
    }

}