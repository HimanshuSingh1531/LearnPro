package com.himanshu.learnpro.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.himanshu.learnpro.data.model.UserProfile
import com.himanshu.learnpro.data.source.AvatarDataSource
import com.himanshu.learnpro.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    uid: String,
    viewModel: ProfileViewModel
) {
    LaunchedEffect(uid) {
        viewModel.loadProfile(uid)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Your Profile",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        }
    ) { padding ->

        when {
            viewModel.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            viewModel.profile == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Profile not found")
                }
            }

            else -> {
                ProfileContent(
                    profile = viewModel.profile!!,
                    onEditClick = { viewModel.toggleEdit() },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    profile: UserProfile,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val avatarRes = remember(profile.avatarId) {
        AvatarDataSource.avatars
            .find { it.id == profile.avatarId }
            ?.drawableRes
            ?: AvatarDataSource.avatars.first().drawableRes
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        // Avatar
        Image(
            painter = painterResource(id = avatarRes),
            contentDescription = "Profile Avatar",
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Name
        Text(
            text = profile.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Phone
        Text(
            text = profile.phone,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Divider()

        Spacer(modifier = Modifier.height(16.dp))

        // About Section
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "ABOUT",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = profile.bio,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Small Edit Button
        OutlinedButton(
            onClick = onEditClick,
            modifier = Modifier
                .height(40.dp)
                .width(160.dp)
        ) {
            Text("Edit Profile")
        }
    }
}

/* ---------------- PREVIEW ---------------- */

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileContent(
            profile = UserProfile(
                name = "Alex Johnson",
                phone = "+1 (555) 123-4567",
                bio = "Aspiring Android Developer preparing for the Associate Android Developer Certification. Focused on Kotlin and Jetpack Compose.",
                avatarId = "avatar_3"
            ),
            onEditClick = {}
        )
    }
}
