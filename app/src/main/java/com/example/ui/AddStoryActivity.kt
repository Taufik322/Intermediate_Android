package com.example.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.helper.Session
import com.example.helper.rotateBitmap
import com.example.helper.uriToFile
import com.example.intermediateandroid.ui.databinding.ActivityAddStoryBinding
import com.example.network.ApiConfig
import com.example.network.UploadStoryResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class AddStoryActivity : AppCompatActivity(), LocationListener {
    private lateinit var binding: ActivityAddStoryBinding
    private var getFile: File? = null
    private lateinit var session: Session
    private lateinit var client: Call<UploadStoryResponse>

    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 1000

    private var latitude: Float = 0.0F
    private var longitude: Float = 0.0F

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = Session(this)
        binding.progressBar.visibility = View.INVISIBLE

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS,
            )
        }

        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener {
            if (binding.cbLocation.isChecked) {
                uploadImage(true)
            } else {
                uploadImage(false)
            }
        }
        supportActionBar?.title = "Add New Story"
        getLocation()
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
    }

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude.toFloat()
        longitude = location.longitude.toFloat()
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun uploadImage(location: Boolean) {
        if (getFile != null && binding.etAdd.text.isNotEmpty()) {

            val file = reduceFileImage(getFile as File)

            val description =
                binding.etAdd.text?.toString()?.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val lat = latitude.toString().toRequestBody("text/plain".toMediaType())
            val lon = longitude.toString().toRequestBody("text/plain".toMediaType())

            binding.progressBar.visibility = View.VISIBLE

            client = if (location) {
                ApiConfig.getApiService().uploadStoryWithLocation(
                    "Bearer ${session.getToken()}",
                    imageMultipart,
                    description!!,
                    lat,
                    lon
                )
            } else {
                ApiConfig.getApiService()
                    .uploadStory("Bearer ${session.getToken()}", imageMultipart, description!!)
            }
            client.enqueue(object : Callback<UploadStoryResponse> {
                override fun onResponse(
                    call: Call<UploadStoryResponse>,
                    response: Response<UploadStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        binding.progressBar.visibility = View.INVISIBLE
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            Toast.makeText(this@AddStoryActivity, "Success", Toast.LENGTH_SHORT)
                                .show()
                        }
                        Intent(this@AddStoryActivity, HomeActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    } else {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(
                            this@AddStoryActivity,
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<UploadStoryResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(
                        this@AddStoryActivity,
                        "Gagal instance Retrofit",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else if (binding.etAdd.text.isEmpty()) {
            Toast.makeText(this, "Please add description first", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please add image first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private val launcherIntentCameraX =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CAMERA_X_RESULT) {

                val myFile = it.data?.getSerializableExtra("picture") as File
                val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

                val result = rotateBitmap(
                    BitmapFactory.decodeFile(myFile.path),
                    isBackCamera
                )

                val bitmap: Bitmap = result

                val os: OutputStream = BufferedOutputStream(FileOutputStream(myFile))
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                os.close()

                getFile = myFile
                binding.previewImageView.setImageBitmap(result)
            }
        }
}