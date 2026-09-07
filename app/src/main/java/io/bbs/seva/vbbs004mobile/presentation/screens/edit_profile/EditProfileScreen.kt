package io.bbs.seva.vbbs004mobile.presentation.screens.edit_profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import android.content.Context
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.core.net.toUri
import androidx.core.graphics.scale


sealed class DialogType(val title: String, val message: String, val confirmText: String) {
    object SaveName :
        DialogType("Change Full Name?", "Do you want to update your name configuration?", "Change")

    object SaveAge : DialogType(
        "Change Age?",
        "Do you want to overwrite your registered age parameter?",
        "Change"
    )

    data class SaveAvatar(val url: String) : DialogType(
        "Set Avatar?",
        "Do you want to switch your profile look to this image?",
        "Change"
    )

    data class UploadPic(val uriString: String) : DialogType(
        "Upload Picture?",
        "Do you want to upload image to server?",
        "Upload"
    )

    data class DeletePicture(val url: String) : DialogType(
        "Delete Picture?",
        "This deletes the item permanently from storage. Proceed?",
        "Delete"
    )
}


suspend fun compressImageUri(context: Context, uri: Uri, maxBytes: Int): ByteArray? = withContext(
    Dispatchers.IO
) {
    val TAG = "compressImageUri"
    try {
        //val inputStream: InputStream? = context.contentResolver.openInputStream(uri) //????
        //val originalBitmap = BitmapFactory.decodeStream(inputStream) ?: return@withContext null
        var streamSize = 0L

        val projection = arrayOf(OpenableColumns.SIZE)
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (sizeIndex != -1) {
                    streamSize = cursor.getLong(sizeIndex)
                }
            }
        }
        if (streamSize <= maxBytes) {
            Log.i(TAG, "compressImageUri: streamSize <= maxBytes ($streamSize<$maxBytes)")
            return@withContext context.contentResolver.openInputStream(uri).use { it?.readBytes() }
        }
        Log.i(TAG, "compressImageUri: streamSize=$streamSize ***********************************************")



        val originalBitmap = context.contentResolver.openInputStream(uri).use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        } ?: return@withContext null
        var quality = 90
        var scale = 1.0f
        var outputBytes: ByteArray? = null


        while (streamSize > maxBytes && quality > 5) {
            val bos = ByteArrayOutputStream()
            val targetBitmap = if (scale < 1.0f) {
                originalBitmap.scale(
                    (originalBitmap.width * scale).toInt(),
                    (originalBitmap.height * scale).toInt()
                )
            } else {
                originalBitmap
            }

            targetBitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos)
            outputBytes = bos.toByteArray()
            streamSize = outputBytes.size.toLong()
            if (targetBitmap != originalBitmap) {
                targetBitmap.recycle()
            }
            quality -= 10
            scale -= 0.1f
        }
        originalBitmap.recycle()
        Log.i(TAG, "compressImageUri: outputBytes = $outputBytes   *****************")
        return@withContext outputBytes
    } catch (e: Exception) {
        Log.e("compressImageUri", "compressImageUri:$uri", e)
        return@withContext null
    }
}

@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val user by viewModel.userProfile.collectAsState()

    // Form inputs states
    var fullName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var displayAvatarUri by remember { mutableStateOf("") }
    var selectedImageBytes by remember { mutableStateOf<ByteArray?>(null) }
    //var compressionStatus by remember { mutableStateOf("") }
    val compressionStatus by viewModel.compressStateString.collectAsStateWithLifecycle()

    // Dialog state controllers
    var activeDialog by remember { mutableStateOf<DialogType?>(null) }
    var pendingAvatarUrl by remember { mutableStateOf("") }

    // Dynamic initial value snapshots to watch for unsaved field variations
    val initialName = user?.fullName ?: ""
    val initialAge = user?.age?.toString() ?: ""
    val initialAvatar = user?.avatarUrl ?: ""

    LaunchedEffect(user) {
        user?.let {
            if (fullName.isEmpty()) fullName = initialName
            if (age.isEmpty()) age = initialAge
            if (displayAvatarUri.isEmpty()) displayAvatarUri = initialAvatar
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { fileUri ->
            scope.launch {

                //compressionStatus = "Compressing..."
                //val compressedBytes = compressImageUri(context, fileUri, maxBytes = 48 * 1024)
                //if (compressedBytes != null) {
                //    selectedImageBytes = compressedBytes
                //    displayAvatarUri = fileUri.toString()
                //    compressionStatus = "Optimized: ${(compressedBytes.size / 1024)} KB"

                // Immediately trigger dialog validation to confirm the uploaded file
                activeDialog = DialogType.UploadPic(fileUri.toString())
                //} else {
                //    compressionStatus = "Failed to stay beneath 48KB limit"
                //}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        // --- 1. HORIZONTAL AVATAR LIST ---
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Select or Upload Avatar", style = MaterialTheme.typography.titleMedium)

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Manual Image Picker Slot
                item(key = "upload_slot") {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            //.then(this@item.Modifier.align(Alignment.Top))
                            .clip(CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                            .clickable {
                                photoPickerLauncher.launch("image/*")
                            },
                        contentAlignment = Alignment.TopCenter,
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("+ Photo", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }

                // Dynamic Picture Collection Items
                items(
                    items = viewModel.picList,
                    key = { pic -> pic.url }
                ) { pic ->
                    val isActiveAvatar = pic.url == initialAvatar

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                //.clip(CircleShape)
                                .border(
                                    width = if (isActiveAvatar) 3.dp else 0.dp,
                                    color = if (isActiveAvatar) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                    //shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center,
                        ) {
                            SubcomposeAsyncImage(
                                model = pic.url,
                                contentDescription = "Avatar item",
                                //contentScale = ContentScale.Crop,
                                //modifier = Modifier.fillMaxSize(),
                                loading = {
                                    Box(
                                        Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(strokeWidth = 2.dp)
                                    }
                                }
                            )
                        }

                        // Individual utility control icon row beneath each image
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            IconButton(
                                onClick = { activeDialog = DialogType.SaveAvatar(pic.url) },
                                modifier = Modifier.size(32.dp),
                                enabled = !isActiveAvatar
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Set Active",
                                    tint = if (isActiveAvatar) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            IconButton(
                                onClick = { activeDialog = DialogType.DeletePicture(pic.url) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Delete Picture",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

        if (compressionStatus.isNotEmpty()) {
            Text(text = compressionStatus, style = MaterialTheme.typography.bodySmall)
        }

        // --- 2. FULL NAME SECTOR ---
        Row(
            modifier = Modifier.fillMaxWidth(),

            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            if (fullName != initialName) {
                Row(modifier = Modifier.padding(start = 4.dp)) {
                    IconButton(onClick = {
                        activeDialog = DialogType.SaveName
                    }) {
                        Icon(
                            Icons.Default.Check,
                            "Save Name",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { fullName = initialName }) {
                        Icon(
                            Icons.Default.Clear,
                            "Reset Name",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }// --- 3. AGE SECTOR ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            if (age != initialAge) {
                Row(modifier = Modifier.padding(start = 4.dp)) {
                    IconButton(onClick = {
                        activeDialog = DialogType.SaveAge
                    }) {
                        Icon(
                            Icons.Default.Check,
                            "Save Age",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { age = initialAge }) {
                        Icon(
                            Icons.Default.Clear,
                            "Reset Age",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
// Global UI Status tracking notification line
        when (val state = viewModel.uiState) {
            is ProfileUiState.Loading -> CircularProgressIndicator()
            is ProfileUiState.Success -> Text(
                "Update applied successfully!",
                color = MaterialTheme.colorScheme.primary
            )

            is ProfileUiState.Error -> Text(
                "Error: ${state.message}",
                color = MaterialTheme.colorScheme.error
            )

            else -> {}
        }
    }

    // --- INTERACTIVE CONFIRMATION POPUP DIALOGS ---
    activeDialog?.let { dialog ->
        AlertDialog(
            onDismissRequest = { activeDialog = null },
            title = { Text(dialog.title) },
            text = { Text(dialog.message) },
            icon = {
                if (dialog is DialogType.UploadPic) {
                    AsyncImage(
                        model = dialog.uriString.toUri(),
                        contentDescription = "Selected Picture Preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp) // Standard M3 alert dialog icon size is 24dp, but pictures look better slightly larger
                            .clip(CircleShape) // Standard visual pattern for avatars/profiles
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    // Routing structural mutation tasks down to the repository interface hooks
                    when (dialog) {
                        is DialogType.SaveName -> viewModel.updateProfile(
                            name = fullName,
                            ageStr = age
                        )

                        is DialogType.SaveAge -> viewModel.updateProfile(
                            name = fullName,
                            ageStr = age
                        )

                        is DialogType.SaveAvatar -> {
                            displayAvatarUri = dialog.url
                            viewModel.updateProfile(
                                name = fullName,
                                ageStr = age,
                                avatarUrl = dialog.url
                            )
                        }

                        is DialogType.DeletePicture -> {
                            // Clear selection locally if active matching reference resource is purged
                            if (displayAvatarUri == dialog.url) displayAvatarUri = ""
                            viewModel.deletePicture(dialog.url)
                        }

                        is DialogType.UploadPic -> {
                            viewModel.uploadPic(dialog.uriString)
                        }
                    }
                    activeDialog = null
                }) {
                    Text(
                        dialog.confirmText,
                        color = if (dialog is DialogType.DeletePicture) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    // Revert visual selection local reference modifications if cancelled
                    if (dialog is DialogType.SaveAvatar) {
                        displayAvatarUri = initialAvatar
                        selectedImageBytes = null
                    }
                    activeDialog = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}


//region ////////////////////////////////////////////////////////////
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Button
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
//import coil.compose.SubcomposeAsyncImage
//import coil.compose.rememberAsyncImagePainter
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.io.ByteArrayOutputStream
//import java.io.InputStream
//
//
//@Composable
//fun EditProfileScreen(
//    viewModel: EditProfileViewModel = hiltViewModel()
//) {
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//    val user by viewModel.userProfile.collectAsState()
//
//    var fullName by remember { mutableStateOf("") }
//    var age by remember { mutableStateOf("") }
//
//    // Display URL/Uri string path references
//    var displayAvatarUri by remember { mutableStateOf("") }
//    var selectedImageBytes by remember { mutableStateOf<ByteArray?>(null) }
//    var compressionStatus by remember { mutableStateOf("") }
//
//    LaunchedEffect(user) {
//        user?.let {
//            if (fullName.isEmpty()) fullName = it.fullName ?: ""
//            if (age.isEmpty()) age = it.age?.toString() ?: ""
//            if (displayAvatarUri.isEmpty()) displayAvatarUri = it.avatarUrl ?: ""
//        }
//    }
//
//    val photoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let { fileUri ->
//            scope.launch {
//                compressionStatus = "Compressing..."
//                // Hard capped 48 Kilobyte structural configuration threshold validation rules
//                val compressedBytes = compressImageUri(context, fileUri, maxBytes = 48 * 1024)
//
//                if (compressedBytes != null) {
//                    selectedImageBytes = compressedBytes
//                    displayAvatarUri = fileUri.toString() // Use local Uri for display preview
//                    compressionStatus =
//                        "Size optimization complete: ${(compressedBytes.size / 1024)} KB"
//                } else {
//                    compressionStatus = "Failed to scale photo beneath 48KB limit"
//                }
//            }
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
////        Box(
////            modifier = Modifier
////                .size(120.dp)
////                .clip(CircleShape)
////                .clickable { photoPickerLauncher.launch("image/*") },
////            contentAlignment = Alignment.Center
////        ) {
////            if (displayAvatarUri.isNotEmpty()) {
////                Image(
////                    painter = rememberAsyncImagePainter(displayAvatarUri),
////                    contentDescription = "Avatar Picture Preview",
////                    modifier = Modifier.fillMaxSize(),
////                    contentScale = ContentScale.Crop
////                )
////            } else {
////                Surface(
////                    color = MaterialTheme.colorScheme.primaryContainer,
////                    modifier = Modifier.fillMaxSize()
////                ) {
////                    Box(contentAlignment = Alignment.Center) {
////                        Text("Add Photo", style = MaterialTheme.typography.labelLarge)
////                    }
////                }
////            }
////        }
//
//        // --- AVATAR SELECTION LAZYROW ---
//        LazyRow(
//            modifier = Modifier.fillMaxWidth(),
//            contentPadding = PaddingValues(horizontal = 16.dp),
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Item 1: The dynamic custom upload button
//            item(key = "upload_button") {
//                val isCustomImageSelected = selectedImageBytes != null
//                Box(
//                    modifier = Modifier
//                        .size(80.dp)
//                        .clip(CircleShape)
//                        .border(
//                            width = if (isCustomImageSelected) 3.dp else 1.dp,
//                            color = if (isCustomImageSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
//                            shape = CircleShape
//                        )
//                        .clickable { photoPickerLauncher.launch("image/*") },
//                    contentAlignment = Alignment.Center
//                ) {
//                    if (isCustomImageSelected) {
//                        Image(
//                            painter = rememberAsyncImagePainter(displayAvatarUri),
//                            contentDescription = "Custom Upload Preview",
//                            modifier = Modifier.fillMaxSize(),
//                            contentScale = ContentScale.Crop
//                        )
//                    } else {
//                        Surface(
//                            color = MaterialTheme.colorScheme.primaryContainer,
//                            modifier = Modifier.fillMaxSize()
//                        ) {
//                            Box(contentAlignment = Alignment.Center) {
//                                Text(
//                                    text = "+ Photo",
//                                    style = MaterialTheme.typography.labelMedium,
//                                    textAlign = TextAlign.Center
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//
//            // Item 2: Existing pictures returned from your Next.js/Supabase backend
//            items(
//                items = viewModel.picList,
//                key = { pic -> pic.url }
//            ) { pic ->
//                // Fixed: Map directly to your declared tracking variable
//                val isSelected = pic.url == displayAvatarUri && selectedImageBytes == null
//
//                Box(
//                    modifier = Modifier
//                        .size(80.dp)
//                        .clip(CircleShape)
//                        .border(
//                            width = if (isSelected) 3.dp else 1.dp,
//                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
//                            shape = CircleShape
//                        )
//                        .clickable {
//                            selectedImageBytes = null // Clear manual file payload if picking preset url
//                            displayAvatarUri = pic.url
//                        }
//                ) {
//                    SubcomposeAsyncImage(
//                        model = pic.url,
//                        contentDescription = "Avatar Choice",
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier.fillMaxSize(),
//                        loading = {
//                            Box(
//                                modifier = Modifier.fillMaxSize(),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                CircularProgressIndicator(strokeWidth = 2.dp)
//                            }
//                        }
//                    )
//                }
//            }
//        }
//
//        if (compressionStatus.isNotEmpty()) {
//            Text(
//                text = compressionStatus,
//                style = MaterialTheme.typography.bodySmall,
//                color = if (selectedImageBytes != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
//            )
//        }
//
//        OutlinedTextField(
//            value = fullName,
//            onValueChange = { fullName = it },
//            label = { Text("Full Name") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        OutlinedTextField(
//            value = age,
//            onValueChange = { age = it },
//            label = { Text("Age") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Button(
//            onClick = {
//                viewModel.updateProfile(
//                    fullName,
//                    age,
//                    //selectedImageBytes
//                )
//            },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = viewModel.uiState !is ProfileUiState.Loading
//        ) {
//            if (viewModel.uiState is ProfileUiState.Loading) {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(24.dp),
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//            } else {
//                Text("Save Changes")
//            }
//        }
//
//        when (val state = viewModel.uiState) {
//            is ProfileUiState.Success -> {
//                Text("Profile saved successfully!", color = MaterialTheme.colorScheme.primary)
//            }
//
//            is ProfileUiState.Error -> {
//                Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
//            }
//
//            else -> {}
//        }
//    }
//}
//
//
///**
// * Iteratively drops compression quality metrics until the byte footprint maps safely below 48KB.
// */
//suspend fun compressImageUri(context: Context, uri: Uri, maxBytes: Int): ByteArray? = withContext(
//    Dispatchers.IO
//) {
//    try {
//        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
//        val originalBitmap = BitmapFactory.decodeStream(inputStream) ?: return@withContext null
//
//        var streamSize = Int.MAX_VALUE
//        var quality = 90
//        var scale = 1.0f
//        var outputBytes: ByteArray? = null
//
//        while (streamSize > maxBytes && quality > 5) {
//            val bos = ByteArrayOutputStream()
//            val targetBitmap = if (scale < 1.0f) {
//                Bitmap.createScaledBitmap(
//                    originalBitmap,
//                    (originalBitmap.width * scale).toInt(),
//                    (originalBitmap.height * scale).toInt(),
//                    true
//                )
//            } else {
//                originalBitmap
//            }
//
//            targetBitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos)
//            outputBytes = bos.toByteArray()
//            streamSize = outputBytes.size
//
//            quality -= 10
//            scale -= 0.1f
//        }
//        return@withContext outputBytes
//    } catch (e: Exception) {
//        e.printStackTrace()
//        return@withContext null
//    }
//}
//endregion

