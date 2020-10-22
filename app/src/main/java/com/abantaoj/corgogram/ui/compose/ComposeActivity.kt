package com.abantaoj.corgogram.ui.compose

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.abantaoj.corgogram.R
import com.abantaoj.corgogram.models.Post
import com.abantaoj.corgogram.ui.feed.FeedActivity
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File


class ComposeActivity : AppCompatActivity() {
    companion object {
        const val TAG = "ComposeActivity"
        const val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 20
        const val photoFileName = "photo.jpg"
    }

    private lateinit var descriptionEditText: EditText
    private lateinit var captureButton: Button
    private lateinit var previewImageView: ImageView
    private lateinit var postButton: Button
    private lateinit var photoFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        descriptionEditText = findViewById(R.id.composeDescriptionEditText)
        captureButton = findViewById(R.id.composeCameraButton)
        previewImageView = findViewById(R.id.composePreviewImageView)
        postButton = findViewById(R.id.composePostButton)

        captureButton.setOnClickListener { launchCamera() }

        postButton.setOnClickListener {
            val description = descriptionEditText.text.toString()
            val currentUser = ParseUser.getCurrentUser()

            if (description.isEmpty()) {
                Toast.makeText(this, "Description cannot be blank", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (previewImageView.drawable == null) {
                Toast.makeText(this, "No picture found!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            savePost(description, currentUser)
        }
    }

    private fun savePost(description: String, currentUser: ParseUser?) {
        val post = Post()

        post.description = description
        post.user = currentUser!!
        post.image = ParseFile(photoFile)

        post.saveInBackground { e ->
            if (e != null) {
                Toast.makeText(this, "New Post Failed", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Failure", e)
                return@saveInBackground
            }

            Toast.makeText(this, "Post Successful!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, FeedActivity::class.java))
        }
    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri();
        val fileProvider = FileProvider.getUriForFile(this, "com.abantaoj.fileprovider", photoFile)

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        }
    }

    private fun getPhotoFileUri(): File {
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        return File(mediaStorageDir.path + File.separator + photoFileName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                previewImageView = findViewById(R.id.composePreviewImageView)
                previewImageView.setImageBitmap(takenImage)
            } else {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}