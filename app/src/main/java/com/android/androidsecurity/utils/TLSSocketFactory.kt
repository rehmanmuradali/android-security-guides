package com.android.androidsecurity.utils

import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.security.GeneralSecurityException
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.net.ssl.*

class TLSSocketFactory @Throws(KeyManagementException::class, NoSuchAlgorithmException::class)
constructor() : SSLSocketFactory() {

    private val internalSSLSocketFactory: SSLSocketFactory

    init {

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf<TrustManager>(systemDefaultTrustManager()), null)
        internalSSLSocketFactory = sslContext.socketFactory
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return internalSSLSocketFactory.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return internalSSLSocketFactory.supportedCipherSuites
    }

    @Throws(IOException::class)
    override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket? {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(s, host, port, autoClose))
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int): Socket? {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int, localHost: InetAddress, localPort: Int): Socket? {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port, localHost, localPort))
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int): Socket? {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(address: InetAddress, port: Int, localAddress: InetAddress, localPort: Int): Socket? {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(address, port, localAddress, localPort))
    }

    private fun enableTLSOnSocket(socket: Socket?): Socket? {
        if (socket != null && socket is SSLSocket) {
            socket.enabledProtocols = arrayOf("TLSv1.1", "TLSv1.2")
        }
        return socket
    }

    fun systemDefaultTrustManager(): X509TrustManager {
        try {
            val trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm()
            )
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
            }
            return trustManagers[0] as X509TrustManager
        } catch (e: GeneralSecurityException) {
            throw AssertionError() // The system has no TLS. Just give up.
        }

    }
}