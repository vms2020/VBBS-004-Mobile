package io.bbs.seva.vbbs004mobile.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.bbs.seva.vbbs004mobile.data.datastore.authDataStore
import io.bbs.seva.vbbs004mobile.data.datastore.model.UserProfile
import io.bbs.seva.vbbs004mobile.data.datastore.profileDataStore
import io.bbs.seva.vbbs004mobile.data.security.AuthTokens
import io.bbs.seva.vbbs004mobile.data.security.AuthTokensSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

// 1. Define custom qualifiers
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokensDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProfileDataStore

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    @TokensDataStore
    fun provideTokensDataStore(
        @ApplicationContext context: Context
    ): DataStore<AuthTokens> {
        return context.authDataStore
//        return DataStoreFactory.create(
//            serializer = AuthTokensSerializer,
//            produceFile = { context.dataStoreFile("secure_auth_tokens.json") },
//            // Run DataStore operations on a background thread pool
//            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
//        )
    }

    @Provides
    @Singleton
    @ProfileDataStore
    fun provideProfileDataStore(@ApplicationContext context: Context): DataStore<UserProfile> {
        return context.profileDataStore
    }

}
