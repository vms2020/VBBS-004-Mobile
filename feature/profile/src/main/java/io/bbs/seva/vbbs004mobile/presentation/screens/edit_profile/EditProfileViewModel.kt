package io.bbs.seva.vbbs004mobile.presentation.screens.edit_profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.bbs.seva.vbbs004mobile.domain.model.AvaPic
import io.bbs.seva.vbbs004mobile.domain.model.User
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject
import androidx.core.net.toUri
import kotlinx.coroutines.flow.MutableStateFlow


sealed interface ProfileUiState {
    object Idle : ProfileUiState
    object Loading : ProfileUiState
    object Success : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

private const val TAG = "EditProfileViewModel"

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val ctx: Context
) : ViewModel() {

    val userProfile: StateFlow<User?> = authRepository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    var picList = mutableStateListOf<AvaPic>()

    var uiState by mutableStateOf<ProfileUiState>(ProfileUiState.Idle)
        private set

    init {
        fetchAvatarPictures()
        refreshProfile()
    }

    fun refreshProfile() {
        viewModelScope.launch {

            authRepository.me()
                .onFailure { exception ->
//                    if (exception is java.net.ConnectException) {
//                        _error.value="Connection error"
//                    } else {
//                        _error.value = exception.message ?: "An unknown error occurred"
//                    }
                    Log.e(TAG, "refreshProfile: ", exception)
                }



//            _isRefreshing.value = false
        }
    }

    fun fetchAvatarPictures() {
        viewModelScope.launch {
            authRepository.fetchAvaPics()
                .onSuccess { pictures ->
                    picList.clear()
                    picList.addAll(pictures)
                }
                .onFailure { error ->
                    Log.i(TAG, "fetchAvatarPictures: !!!!!!!!!!!!!")
                    Log.i(TAG, "fetchAvatarPictures: $error")
                    Log.i(TAG, "fetchAvatarPictures: !!!!!!!!!!!!!")
                    // Optionally handle background asset fetch failure here
                }
            Log.i(TAG, "fetchAvatarPictures: $picList")
        }
    }

    fun deletePicture(storagePath: String) {
        viewModelScope.launch {
            val r = authRepository.deletePicture(storagePath = storagePath)
            Log.i(TAG, "deletePicture: $r")
            authRepository.fetchAvaPics()
                .onSuccess { pictures ->
                    picList.clear()
                    picList.addAll(pictures)
                }
                .onFailure { error ->
                    Log.i(TAG, "fetchAvatarPictures: !!!!!!!!!!!!!")
                    Log.i(TAG, "fetchAvatarPictures: $error")
                    Log.i(TAG, "fetchAvatarPictures: !!!!!!!!!!!!!")
                    // Optionally handle background asset fetch failure here
                }
            Log.i(TAG, "fetchAvatarPictures: $picList")
        }
    }

    fun updateProfile(
        name: String,
        ageStr: String,
        avatarUrl: String? = userProfile.value?.avatarUrl
    ) {
        viewModelScope.launch {
            uiState = ProfileUiState.Loading
            val ageInt = ageStr.toIntOrNull()

            val result = authRepository.updateProfile(
                fullName = name,
                age = ageInt,
                avatarUrl = avatarUrl,
            )

            uiState = if (result.isSuccess) {
                ProfileUiState.Success
            } else {
                ProfileUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    val compressStateString = MutableStateFlow("")

    fun uploadPic(
        uriString: String
    ) {
        viewModelScope.launch {
            compressStateString.value = "Compressing..."
            val byteArray = compressImageUri(ctx, uriString.toUri(), 48 * 1024)
            Log.i(TAG, "uploadPic: byteArray.size = ${byteArray?.size} *********************")
            if (byteArray != null && byteArray.size > 4096) {
                authRepository.uploadPic(byteArray)
                    .onSuccess {
                        Log.i(TAG, "uploadPic: $it")
                    }
                    .onFailure {
                        Log.e(TAG, "uploadPic: error", it)
                    }
                compressStateString.value = "Optimized: ${(byteArray.size / 1024)} KB"
                authRepository.fetchAvaPics()
                    .onSuccess { pictures ->
                        picList.clear()
                        picList.addAll(pictures)
                    }
                    .onFailure { error ->
                        Log.i(TAG, "fetchAvatarPictures: !!!!!!!!!!!!!")
                        Log.i(TAG, "fetchAvatarPictures: $error")
                        Log.i(TAG, "fetchAvatarPictures: !!!!!!!!!!!!!")
                        // Optionally handle background asset fetch failure here
                    }
                Log.i(TAG, "fetchAvatarPictures: $picList")
            } else {
                compressStateString.value = "Failed to stay beneath 48KB limit"
            }
        }
    }
}
