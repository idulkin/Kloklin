package com.idulkin.kloklin.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by igor.dulkin on 9/28/17.
 *
 * Main module for dependency injection
 * Provides app context where necessary
 */
@Module
class AppModule(val app: Application) {
    @Provides @Singleton fun provideApp() = app
}