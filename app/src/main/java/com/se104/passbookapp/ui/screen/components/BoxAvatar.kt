package com.se104.passbookapp.ui.screen.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.se104.passbookapp.R

@Composable
fun BoxAvatar(
    modifier: Modifier = Modifier,
    avatar: String?= null
){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        AsyncImage(
            model = avatar,
            contentDescription = "Avatar",
            modifier = modifier
                .shadow(8.dp, shape = CircleShape)
                .clip(CircleShape)
                .border(4.dp, MaterialTheme.colorScheme.background, CircleShape),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.avatar_placeholder),
            error = painterResource(id = R.drawable.avatar_placeholder)
        )


    }
}