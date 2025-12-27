package com.himanshu.learnpro.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.himanshu.learnpro.data.repository.PaymentRepository
import com.himanshu.learnpro.payment.RazorpayManager
import com.himanshu.learnpro.viewmodel.CourseDetailViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun CourseDetailScreen(
    courseId: String,
    userId: String,
    onBack: () -> Unit
) {
    val viewModel = remember {
        CourseDetailViewModel(courseId, userId)
    }

    val course = viewModel.course
    val lectures = viewModel.lectures
    val hasPurchased = viewModel.hasPurchased
    val isLoading = viewModel.isLoading

    val activity = LocalContext.current as Activity
    val userEmail = FirebaseAuth.getInstance().currentUser?.email

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = course?.title ?: "",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = course?.description ?: "",
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Price: ₹${course?.price}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 🔒 PAYMENT / ACCESS LOGIC
        if (!hasPurchased) {

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    RazorpayManager.startPayment(
                        activity = activity,
                        courseName = course?.title ?: "Course",
                        amount = course?.price ?: 0,
                        email = userEmail,
                        onPaymentSuccess = { paymentId, orderId ->
                            PaymentRepository.savePurchase(
                                userId = userId,
                                courseId = courseId,
                                paymentId = paymentId,
                                orderId = orderId,
                                amount = course?.price ?: 0,
                                onSuccess = {
                                    // 🔓 refresh purchase state
                                    viewModel.refreshPurchase()
                                },
                                onError = {
                                    // TODO: show snackbar / toast
                                }
                            )
                        },
                        onPaymentError = {
                            // TODO: show payment failed message
                        }
                    )
                }
            ) {
                Text("Buy Now")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Lectures will unlock after purchase",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.error
            )

        } else {

            Text(
                text = "Lectures",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            lectures.forEach { lecture ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${lecture.order}. ${lecture.title}",
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = lecture.type.uppercase(),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = onBack) {
            Text("Back")
        }
    }
}
