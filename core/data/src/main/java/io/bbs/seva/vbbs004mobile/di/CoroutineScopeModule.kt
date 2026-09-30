package io.bbs.seva.vbbs004mobile.di
// core/data/src/main/java/io/bbs/seva/vbbs004mobile/di/CoroutineScopeModule.kt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers
//import javax.inject.Qualifier
import javax.inject.Singleton

///////////////////////////
// moved to :core:common
//@Qualifier
//@Retention(AnnotationRetention.BINARY)
//annotation class ApplicationScope    // ← must exist somewhere importable

@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopeModule {

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
}
