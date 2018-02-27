package xyz.thomasmohr.onfire.data

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context) = CounterDatabase.createPersistentDatabase(context)

}