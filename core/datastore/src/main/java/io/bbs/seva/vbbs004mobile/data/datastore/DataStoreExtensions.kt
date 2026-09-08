package io.bbs.seva.vbbs004mobile.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import io.bbs.seva.vbbs004mobile.data.datastore.model.GeoLocationData
import io.bbs.seva.vbbs004mobile.data.datastore.model.UserProfile
import io.bbs.seva.vbbs004mobile.data.datastore.serializer.GeoLocationSerializer
import io.bbs.seva.vbbs004mobile.data.datastore.serializer.UserProfileSerializer
import io.bbs.seva.vbbs004mobile.data.security.AuthTokensSerializer

val Context.profileDataStore: DataStore<UserProfile> by dataStore(
    fileName = "user_profile.json",
    serializer = UserProfileSerializer
)

// Extension property to fetch the DataStore instance
val Context.authDataStore by dataStore(
    //fileName = "secure_auth_tokens.json",
    fileName = "secure_auth_tokens.enc",
    serializer = AuthTokensSerializer
)

val Context.locationDataStore: DataStore<GeoLocationData> by dataStore(
    fileName = "Geolocation.json",
    serializer = GeoLocationSerializer
)