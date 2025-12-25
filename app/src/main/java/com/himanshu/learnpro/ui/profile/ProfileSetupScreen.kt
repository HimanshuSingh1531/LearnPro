package com.himanshu.learnpro.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.himanshu.learnpro.data.model.Avatar
import com.himanshu.learnpro.data.source.AvatarDataSource

@Composable
fun ProfileSetupScreen(
    uid: String,
    onProfileSaved: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf<Avatar?>(null) }
    var isSaving by remember { mutableStateOf(false) }

    val firestore = remember { FirebaseFirestore.getInstance() }
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()                 // ✅ pushes UI above keyboard
            .verticalScroll(scrollState) // ✅ allows scrolling with keyboard
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Complete Your Profile",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Fill in the details below so we can get to know you better",
            style = MaterialTheme.typography.bodyMedium
        )

        // Avatar preview
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            selectedAvatar?.let {
                Image(
                    painter = painterResource(it.drawableRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                )
            }
        }

        Text(
            text = "Choose your avatar",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.height(220.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(AvatarDataSource.avatars) { avatar ->
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .border(
                            width = if (selectedAvatar?.id == avatar.id) 2.dp else 1.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .clickable { selectedAvatar = avatar },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(avatar.drawableRes),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // ✅ PHONE KEYBOARD FIX
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            )
        )

        OutlinedTextField(
            value = bio,
            onValueChange = { bio = it },
            label = { Text("Bio") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 4
        )

        Button(
            onClick = {
                focusManager.clearFocus() // ✅ hides keyboard on save
                val avatar = selectedAvatar ?: return@Button
                isSaving = true

                firestore.collection("users")
                    .document(uid)
                    .set(
                        mapOf(
                            "name" to name,
                            "phone" to phone,
                            "bio" to bio,
                            "avatarId" to avatar.id,
                            "profileCompleted" to true
                        )
                    )
                    .addOnSuccessListener {
                        isSaving = false
                        onProfileSaved()
                    }
                    .addOnFailureListener {
                        isSaving = false
                    }
            },
            enabled = selectedAvatar != null && !isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSaving) "Saving..." else "Save Profile")
        }

        // extra bottom space so Save button is never hidden
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileSetupScreenPreview() {
    MaterialTheme {
        ProfileSetupScreen(
            uid = "test_uid",
            onProfileSaved = {}
        )
    }
}
