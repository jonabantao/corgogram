package com.abantaoj.corgogram.ui.main

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.abantaoj.corgogram.R
import com.abantaoj.corgogram.models.Post
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File

/**
 * A simple [Fragment] subclass.
 * Use the [ComposeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComposeFragment : Fragment() {
    companion object {
        const val TAG = "ComposeFragment"
        const val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 20
        const val photoFileName = "photo.jpg"

        @JvmStatic
        fun newInstance() = ComposeFragment()
    }

    private lateinit var descriptionEditText: EditText
    private lateinit var captureButton: Button
    private lateinit var previewImageView: ImageView
    private lateinit var postButton: Button
    private lateinit var photoFile: File
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        descriptionEditText = view.findViewById(R.id.composeDescriptionEditText)
        captureButton = view.findViewById(R.id.composeCameraButton)
        previewImageView = view.findViewById(R.id.composePreviewImageView)
        postButton = view.findViewById(R.id.composePostButton)
        progressBar = view.findViewById(R.id.composeProgressBar)

        captureButton.setOnClickListener { launchCamera() }

        postButton.setOnClickListener {
            val description = descriptionEditText.text.toString()
            val currentUser = ParseUser.getCurrentUser()

            if (description.isEmpty()) {
                Toast.makeText(context, "Description cannot be blank", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (previewImageView.drawable == null) {
                Toast.makeText(context, "No picture found!", Toast.LENGTH_SHORT).show()
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

        progressBar.visibility = ProgressBar.VISIBLE

        post.saveInBackground { e ->
            if (e != null) {
                Toast.makeText(context, "New Post Failed", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Failure", e)
                progressBar.visibility = ProgressBar.INVISIBLE
                return@saveInBackground
            }

            Toast.makeText(context, "Post Successful!", Toast.LENGTH_SHORT).show()
            progressBar.visibility = ProgressBar.INVISIBLE

            parentFragmentManager.beginTransaction()
                .replace(R.id.mainActivityFrameLayout, FeedFragment.newInstance())
                .commit()
        }
    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFileUri()
        val fileProvider = FileProvider.getUriForFile(requireContext(), "com.abantaoj.fileprovider", photoFile)

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        }
    }

    private fun getPhotoFileUri(): File {
        val mediaStorageDir = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            TAG
        )

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        return File(mediaStorageDir.path + File.separator + photoFileName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                previewImageView.setImageBitmap(takenImage)
            } else {
                Toast.makeText(context, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}