package kr.genti.generate.billing

import com.android.billingclient.api.Purchase

interface BillingCallback {
    fun onBillingSuccess(purchase: Purchase)
    fun onBillingFailure(responseCode: Int)
}