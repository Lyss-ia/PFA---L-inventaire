package com.example.linventaire

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.linventaire.DB.DB
import com.example.linventaire.DB.Products.Products
import com.example.linventaire.databinding.ActivityScannerBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

class ScannerActivity : AppCompatActivity() {
    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var binding: ActivityScannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Permissions
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            setupControls()
        }
        //Animation barre bleue
        val aniSlide: Animation =
            AnimationUtils.loadAnimation(this, R.anim.scanner_animation)
        binding.barcodeLine.startAnimation(aniSlide)
    }


    private fun setupControls() {
        barcodeDetector = BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true) //you should add this feature
            .build()

        binding.cameraSurfaceView.getHolder().addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            @SuppressLint("MissingPermission")
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })


        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(applicationContext, "Scanner has been closed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val qrCode = detections.detectedItems
                if (qrCode.size() == 1) {
                    val scannedUniqueNumber = qrCode.valueAt(0).rawValue

                    runOnUiThread { //lorsque le scan est passé
                        cameraSource.stop()
                        //On récupère le numéro unique du qr code pour l'identifier
                        val db = DB.getInstance(this@ScannerActivity)
                        val dao = db!!.productsDao()
                        var selectedProduct = dao.get(scannedUniqueNumber)
                        //On pose une condition
                        if(selectedProduct.state == true){
                            selectedProduct.state = false
                        }
                        else{
                            selectedProduct.state = true
                        }
                        //On met à jour les données
                        selectedProduct = Products(selectedProduct.id,
                            selectedProduct.idUser,
                            selectedProduct.type,
                            selectedProduct.model,
                            selectedProduct.unique_number,
                            selectedProduct.link,
                            selectedProduct.state
                        )
                        dao.updateProduct(selectedProduct)

                        Toast.makeText(this@ScannerActivity, "Etat de location de l'appareil " + selectedProduct.unique_number + " modifié avec succès !", Toast.LENGTH_SHORT).show()

                        finish()
                    }

                }
                else
                {
                    Toast.makeText(this@ScannerActivity, "Il y a eu une erreur, veuillez recommencer ", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupControls()
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource.stop()
    }
}
