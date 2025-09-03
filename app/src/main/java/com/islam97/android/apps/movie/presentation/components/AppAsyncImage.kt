package com.islam97.android.apps.movie.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.islam97.android.apps.movie.R

@Composable
fun AppAsyncImage(modifier: Modifier = Modifier, model: Any?) {
    AsyncImage(
        modifier = modifier,
        model = model,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.ic_image_placeholder)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAppAsyncImage() {
    AppAsyncImage(model = "")
}