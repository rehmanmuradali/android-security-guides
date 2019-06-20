package com.android.androidsecurity.implementation

import android.util.Log
import com.android.androidsecurity.utils.Constant
import com.android.androidsecurity.contracts.Point
import com.android.androidsecurity.utils.TLSSocketFactory
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException

class SslPinning : Point {

    override fun explain() {
        createRetrofitInstance()
    }

    private fun createRetrofitInstance() {


        //Add Certificate Pinning
        val okHttpClientBuilder = OkHttpClient.Builder()
            .certificatePinner(getCertificatesPin())


        // Use TLS for security
        try {
            val tlsSocketFactory = TLSSocketFactory()
            okHttpClientBuilder.sslSocketFactory(tlsSocketFactory, tlsSocketFactory.systemDefaultTrustManager())
        } catch (e: KeyManagementException) {
            Log.d(Constant.TAG, "Failed to create Socket connection ")
        } catch (e: NoSuchAlgorithmException) {
            Log.e(Constant.TAG, "Failed to create Socket connection ")
        }


        //Finalize retrofit object
        val client = okHttpClientBuilder.build()
        var retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(client)
            .build()
    }

    private fun getCertificatesPin(): CertificatePinner {
        return CertificatePinner.Builder()
            .add(Constant.BASE_URL.replace("https://", ""), Constant.DEMO_SHA256_KEY).build()
    }
}