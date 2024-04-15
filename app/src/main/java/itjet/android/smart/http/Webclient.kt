package itjet.android.smart.http

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Base64
import itjet.android.smart.R
import okhttp3.OkHttpClient
import okhttp3.tls.HandshakeCertificates
import okhttp3.tls.HeldCertificate
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyFactory
import java.security.KeyPair
import java.security.PrivateKey
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.security.spec.PKCS8EncodedKeySpec


class Webclient {

    private val BASE_URL = "https://192.168.100.19:25160"
    private val BASE_ONLINE_URL = "https://193.32.95.135:25160"

    private lateinit var retrofitInstance :IGreenhouse

    companion object {
        //        lateinit var application: Application
        @Volatile
        private var instance: Webclient? = null

        fun getInstance(): Webclient {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Webclient()
                    }
                }
            }
            return instance!!
        }

    }

    fun getRetrofitInstance(context: Context): IGreenhouse {

        val cert = context.resources.openRawResource(R.raw.client)
        val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
        val x509Certificate: X509Certificate =
            cf.generateCertificate(cert) as X509Certificate
        val server = context.resources.openRawResource(R.raw.root)
        val servercert: X509Certificate = cf.generateCertificate(server) as X509Certificate
        val keyPair = KeyPair(x509Certificate.publicKey, loadPrivateKey(context))

        val handshakeCertificates: HandshakeCertificates = HandshakeCertificates.Builder()
            .addTrustedCertificate(servercert)
            .heldCertificate(HeldCertificate(keyPair, x509Certificate))
            .build()

        val client = OkHttpClient
            .Builder()
            .sslSocketFactory(
                handshakeCertificates.sslSocketFactory(),
                handshakeCertificates.trustManager
            )
            .build()


        if (!::retrofitInstance.isInitialized) {
            val wifiName: String = checkWiFi(context)
            if (wifiName.equals("\"siecsasiedzka2516\"") || wifiName.equals("\"siecsasiedzka2516-ext\"")) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                retrofitInstance = retrofit.create(IGreenhouse::class.java)
            } else {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_ONLINE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                retrofitInstance = retrofit.create(IGreenhouse::class.java)
            }
        }
        return retrofitInstance
    }

    private fun checkWiFi(context: Context): String {
        val wifiManager =
            context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.connectionInfo.ssid
    }

    private fun loadPrivateKey(context: Context): PrivateKey? {
        val private = context.resources.openRawResource(R.raw.pkey)
        val encoded: ByteArray = Base64.decode(private.readBytes(), Base64.DEFAULT)
        val keySpec = PKCS8EncodedKeySpec(encoded)
        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePrivate(keySpec)
    }

}