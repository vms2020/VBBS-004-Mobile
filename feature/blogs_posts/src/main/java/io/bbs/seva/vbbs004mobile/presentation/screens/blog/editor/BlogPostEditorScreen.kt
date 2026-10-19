package io.bbs.seva.vbbs004mobile.presentation.screens.blog.editor
// feature/blogs_posts/src/main/java/io/bbs/seva/vbbs004mobile/presentation/screens/blog/editor/BlogPostEditorScreen.kt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BlogPostEditorScreen(
    slug: String?,                         // null = create, non-null = edit
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onSaved: (slug: String) -> Unit = {},
    viewModel: BlogPostEditorViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(slug) {
        viewModel.setMode(slug)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is BlogPostEditorEvent.Saved -> onSaved(event.slug)
                BlogPostEditorEvent.Cancelled -> onNavigateBack()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

            else ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    OutlinedTextField(
                        value = state.title,
                        onValueChange = viewModel::onTitleChange,
                        label = { Text("Title") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isSaving,
                    )

                    OutlinedTextField(
                        value = state.slug,
                        onValueChange = viewModel::onSlugChange,
                        label = { Text("Slug") },
                        supportingText = { Text("lowercase, hyphens, a-z0-9") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isSaving,
                    )

                    OutlinedTextField(
                        value = state.content,
                        onValueChange = viewModel::onContentChange,
                        label = { Text("Content") },
                        minLines = 8,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isSaving,
                    )

                    OutlinedTextField(
                        value = state.excerpt,
                        onValueChange = viewModel::onExcerptChange,
                        label = { Text("Excerpt (optional)") },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isSaving,
                    )

                    OutlinedTextField(
                        value = state.coverImageUrl,
                        onValueChange = viewModel::onCoverImageUrlChange,
                        label = { Text("Cover image URL (optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isSaving,
                    )

                    SwitchRow(
                        label = "Comments enabled",
                        checked = state.commentsEnabled,
                        onCheckedChange = viewModel::onCommentsEnabledChange,
                        enabled = !state.isSaving,
                    )

                    SwitchRow(
                        label = "Published",
                        checked = state.published,
                        onCheckedChange = viewModel::onPublishedChange,
                        enabled = !state.isSaving,
                    )

                    state.error?.let { err ->
                        Text(
                            text = err.toString(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        TextButton(
                            onClick = viewModel::cancel,
                            enabled = !state.isSaving,
                        ) { Text("Cancel") }

                        TextButton(
                            onClick = viewModel::save,
                            enabled = state.canSave,
                        ) {
                            if (state.isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.height(18.dp),
                                    strokeWidth = 2.dp,
                                )
                            } else {
                                Text(if (state.mode == EditorMode.Create) "Create" else "Save")
                            }
                        }
                    }
                }
        }
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
    }
}

