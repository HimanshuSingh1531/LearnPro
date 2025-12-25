package com.himanshu.learnpro.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.himanshu.learnpro.ui.theme.*

@Composable
fun RoleSelectScreen(
    onUserClick: () -> Unit,
    onAdminClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {

            Text(
                text = "LearnPro",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = SoftBlack
            )

            Text(
                text = "Choose how you want to continue",
                fontSize = 15.sp,
                color = SoftGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            RoleButton(
                text = "Continue as User",
                onClick = onUserClick
            )

            RoleButton(
                text = "Continue as Admin",
                onClick = onAdminClick,
                outlined = true
            )
        }
    }
}

@Composable
private fun RoleButton(
    text: String,
    onClick: () -> Unit,
    outlined: Boolean = false
) {
    if (outlined) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .width(280.dp)
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = SoftBlack,
                fontWeight = FontWeight.Medium
            )
        }
    } else {
        Button(
            onClick = onClick,
            modifier = Modifier
                .width(280.dp)
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SoftBlack,
                contentColor = SurfaceWhite
            )
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
