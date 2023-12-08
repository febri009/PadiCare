package com.example.padicare.deteksi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.padicare.HomeActivity
import com.example.padicare.deteksi.network.responses.AddHistoryDiseaseResponse
import com.example.padicare.deteksi.network.responses.DiseaseDetectResponse
import com.example.padicare.databinding.ActivityDetectionBinding
import com.example.padicare.deteksi.network.ApiConfig
import com.example.padicare.util.reduceFileImage
import com.example.padicare.util.uriToFile
import com.example.padicare.util.Preferences
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetectionBinding
    private lateinit var backButton: ImageView
    private lateinit var myPreferences: Preferences

    // counter untuk mengaktifkan tombol deteksi
    private var counter = 0
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    // fungsi untuk menangani hasil dari request permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Tidak mendapatkan izin!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    // fungsi untuk memeriksa apakah semua permission sudah diberikan
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPreferences = Preferences(this)
        // Memeriksa apakah semua permission sudah diberikan
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        val galleryButton = binding.galleryButton
        val camera = binding.cameraButton

        // Tombol galeri di klik
        galleryButton.setOnClickListener {
            openGallery()
            counter = 1
            setMyButtonEnable()
        }

        // Tombol kamera di klik
        camera.setOnClickListener {
            openCamera()
            counter = 1
            setMyButtonEnable()
        }

        // Tombol deteksi di klik
        val detectButton = binding.detectionButton
        detectButton.setOnClickListener {
            detectDisease()
        }

        setMyButtonEnable()
    }

    // fungsi untuk mengatur tombol deteksi
    private fun setMyButtonEnable() {
        val isEnable = counter == 1
        binding.detectionButton.isEnabled = isEnable
    }

    // fungsi untuk menangani hasil dari pemilihan gambar dari galeri
    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImg: Uri = result.data?.data as Uri
                val myFile = uriToFile(selectedImg, this)
                getFile = myFile

                binding.ivResultImage.setImageURI(selectedImg)
            }
        }

    // fungsi untuk membuka galeri dan memilih gambar
    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih gambar")
        launcherIntentGallery.launch(chooser)
    }

    // fungsi untuk menangani hasil dari pemanggilan kamera
    private val launcherIntentCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CAMERA_X_RESULT) {
                val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getSerializableExtra("picture", File::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    it.data?.getSerializableExtra("picture")
                } as? File

                myFile?.let { file ->
                    getFile = file
                    binding.ivResultImage.setImageBitmap(BitmapFactory.decodeFile(file.path))
                }
            }
        }

    // fungsi untuk membuka kamera
    private fun openCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }

    // fungsi untuk mendeteksi gambar
    private fun detectDisease() {
        if (getFile != null) {
            val image = reduceFileImage(getFile as File)
            val requestImageFile = image.asRequestBody("image/*".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                image.name,
                requestImageFile
            )
            addImageToDetect(imageMultipart)
        } else {
            Toast.makeText(this, "Gambar tidak ditemukan!", Toast.LENGTH_SHORT).show()
        }
    }

    // fungsi untuk mendeteksi gambar
    private fun addImageToDetect(image: MultipartBody.Part) {
        val token = "Bearer " + myPreferences.getUserToken()
        // request untuk fungsi deteksi
        val client = ApiConfig.getApiServiceRecomendationDisease().getDiseaseDetect(image)

        showLoading(true)
        client.enqueue(object : Callback<DiseaseDetectResponse> {
            // Jika berhasil
            override fun onResponse(
                call: Call<DiseaseDetectResponse>,
                response: Response<DiseaseDetectResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@DetectionActivity,
                        "Berhasil Upload Gambar",
                        Toast.LENGTH_LONG
                    ).show()
                    val result = response.body()

                    val intent = Intent(this@DetectionActivity, DetectionResultActivity::class.java)
                    if (result != null) {
                        intent.putExtra("url_image", result.imageUrl)
                        intent.putExtra("disease_name", result.predictions[0].disease.name)
                        intent.putExtra("disease_solution", result.predictions[0].disease.solutions)
                        intent.putExtra(
                            "disease_percentage",
                            result.predictions[0].percentage.toString()
                        )
                    }
                    startActivity(intent)
                    showLoading(false)
                }
            }

            // Jika gagal
            override fun onFailure(call: Call<DiseaseDetectResponse>, t: Throwable) {
                Toast.makeText(this@DetectionActivity, "Gagal Upload Gambar", Toast.LENGTH_SHORT)
                    .show()
            }

        })
    }


    // fungsi untuk menampilkan loading
    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }

    // companion object untuk menyimpan properti dan konstanta yang digunakan di activity ini
    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}