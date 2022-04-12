package com.yangchen.fuckqrcode

import android.Manifest
import android.graphics.PointF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import android.os.Build
import android.widget.TextView
import okhttp3.*
import java.io.IOException
import android.util.Base64
import java.io.StringReader
import java.nio.charset.Charset

class MainActivity : AppCompatActivity(), QRCodeReaderView.OnQRCodeReadListener {
    private lateinit var qrCodeReaderView: QRCodeReaderView
    private var codeStr: String = "NotSet"
    private lateinit var code: TextView
    private val client: OkHttpClient = OkHttpClient()

    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        if (text == null || codeStr == text) {
            return
        }
        codeStr = text
        code.text = text

        val bs64 = Base64.encodeToString(text.toByteArray(Charsets.UTF_8), Base64.URL_SAFE)

        val url = "http://81.68.126.42:39123/update/$bs64"
        val request: Request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, url + e.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "SUCCESS", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    override fun onResume() {
        super.onResume()
        qrCodeReaderView.startCamera();
    }

    override fun onPause() {
        super.onPause()
        qrCodeReaderView.stopCamera();
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        requestPermissions(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 1
        )

        code = findViewById(R.id.code)
        qrCodeReaderView = findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
    }
}