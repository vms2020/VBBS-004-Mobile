package io.bbs.seva.vbbs004mobile.presentation.screens.blog.detail
// feature/blogs_posts/src/main/java/io/bbs/seva/vbbs004mobile/presentation/screens/blog/detail/BlogPostDetailScreen.kt

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import io.bbs.seva.vbbs004mobile.domain.model.BlogPostComment
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun BlogPostDetailScreen(
    slug: String,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onNavigateToEdit: (String) -> Unit = {},
    viewModel: BlogPostDetailViewModel = hiltViewModel(),
) {
    val debugImageLoader = ImageLoader.Builder(LocalContext.current)
        .logger(DebugLogger()) // <-- Put logger here
        .build()
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(slug) {
        viewModel.setSlug(slug)        // ← the only new line
    }
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                BlogDetailEvent.NavigateBack -> onNavigateBack()
                is BlogDetailEvent.NavigateToEdit -> onNavigateToEdit(event.slug)
                is BlogDetailEvent.ShowToast -> { /* TODO: Snackbar via your snackbar controller */
                }
            }
        }
    }

    when {
        state.isLoadingPost && state.post == null ->
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

        state.error != null && state.post == null ->
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(state.error.toString(), style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = viewModel::retry) { Text("Retry") }
                }
            }

        state.post != null -> {
            val post = state.post!!
            LazyColumn(
                contentPadding = PaddingValues(
                    top = 0.dp,
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = modifier.fillMaxSize(), // .border(width = 1.dp, color = Color.Cyan,),
            ) {
                item(key = "cover") {
                    if (!post.coverImageUrl.isNullOrBlank()) {
                        AsyncImage(
                            imageLoader = debugImageLoader,
                            model = //post.coverImageUrl
                                ImageRequest.Builder(LocalContext.current)
                                    .data(post.coverImageUrl)
                                    .crossfade(true)
                                    .httpHeaders(
                                        NetworkHeaders.Builder()
                                            .set(
                                                "User-Agent",
                                                "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Mobile Safari/537.36"
                                            )
                                            .build()
                                    )
                                    .build(),
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                //.size(72.dp)
                                .clip(RoundedCornerShape(8.dp)),
                        )
                        Spacer(Modifier.width(12.dp))
                    }
                }
                item(key = "header") {
                    val post = state.post ?: return@item
                    Column(
//                        modifier = Modifier.border(
//                            width = 1.dp,
//                            color = Color.Cyan,
//                        ),
                    ) {
                        if (!post.published) {
                            Surface(
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                shape = RoundedCornerShape(4.dp),
                            ) {
                                Text(
                                    "Draft",
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                )
                            }
                            Spacer(Modifier.height(6.dp))
                        }
//                        Text(
//                            post.title,
//                            style = MaterialTheme.typography.headlineSmall,
//                        )
                        Text(
                            "Posted by: <${post.authorEmail}> ${post.authorFullName}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                        )
                        //Spacer(Modifier.height(8.dp))
                        Text(
                            "Created ${post.createdAt.formatDisplay()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = FontFamily.Monospace,
                        )
                        Text(
                            "Updated ${post.updatedAt.formatDisplay()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = FontFamily.Monospace,
                        )
                    }
                }
                item {
                    Text(
                        post.content,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = FontFamily.Monospace,
                    )
                }
                item { HorizontalDivider() }
                item {
                    Text(
                        "Comments (${state.comments.size})",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                items(state.comments, key = { it.id }) { c ->
                    CommentRow(
                        comment = c,
                        canDelete = state.currentUserId != null && c.authorId == state.currentUserId,
                        onDelete = { viewModel.deleteComment(c.id) },
                    )
                }
                if (state.canComment) {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = state.draftComment,
                                onValueChange = viewModel::onDraftCommentChange,
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("Write a comment…") },
                                enabled = !state.isSubmittingComment,
                                maxLines = 4,
                            )
                            Spacer(Modifier.width(8.dp))
                            TextButton(
                                onClick = viewModel::submitComment,
                                enabled = state.draftComment.isNotBlank() && !state.isSubmittingComment,
                            ) { Text("Send") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentRow(
    comment: BlogPostComment,
    canDelete: Boolean,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Column(Modifier.weight(1f)) {
            Text(
                comment.createdAt.formatDisplay(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
            )
            Text(
                "Posted by: <${comment.authorEmail}> ${comment.authorFullName}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                comment.content,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = FontFamily.Monospace,
            )
        }
        if (canDelete) {
            TextButton(onClick = onDelete) { Text("×") }
        }
    }
}

private fun java.time.Instant.formatDisplay(): String =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        .withZone(ZoneId.systemDefault())
        .format(this)


