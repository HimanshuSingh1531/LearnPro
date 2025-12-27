package com.himanshu.learnpro.data.repository



import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp

object PaymentRepository {

    fun savePurchase(
        userId: String,
        courseId: String,
        paymentId: String,
        orderId: String?,
        amount: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val data = hashMapOf(
            "paymentId" to paymentId,
            "orderId" to orderId,
            "amount" to amount,
            "status" to "SUCCESS",
            "purchasedAt" to Timestamp.now()
        )

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .collection("purchases")
            .document(courseId)
            .set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                onError(it.message ?: "Failed to save purchase")
            }
    }
}
