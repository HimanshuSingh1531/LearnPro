package com.himanshu.learnpro


import android.app.Activity
import com.razorpay.Checkout
import org.json.JSONObject

object RazorpayManager {

    fun startPayment(
        activity: Activity,
        courseName: String,
        amount: Int, // in rupees
        email: String?,
        onPaymentSuccess: (paymentId: String, orderId: String?) -> Unit,
        onPaymentError: (String) -> Unit
    ) {
        val checkout = Checkout()

        // 🔒 KEY WILL BE ADDED LATER
        checkout.setKeyID("RAZORPAY_KEY_ID_HERE")

        val options = JSONObject().apply {
            put("name", "LearnPro")
            put("description", courseName)
            put("currency", "INR")
            put("amount", amount * 100) // Razorpay uses paise
            put("prefill", JSONObject().apply {
                put("email", email ?: "")
            })
        }

        try {
            checkout.open(activity, options)
        } catch (e: Exception) {
            onPaymentError(e.message ?: "Payment failed to start")
        }
    }
}
