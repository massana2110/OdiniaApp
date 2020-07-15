package com.womannotfound.odinia.views.ui.fragments.controls

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentSettingsProfileBinding
import com.womannotfound.odinia.views.ui.activities.AuthenticationActivity
import kotlinx.android.synthetic.main.fragment_settings_profile.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class SettingsProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    val PICK_IMAGE_REQUEST = 71
    lateinit var filePath: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSettingsProfileBinding>(
            inflater,
            R.layout.fragment_settings_profile, container, false
        )

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        val userID = auth.currentUser?.uid.toString()

        binding.chooseImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else{
                    //permission already granted
                    chooseImage();
                }
            }
            else{
                //system OS is < Marshmallow
                chooseImage();
            }
        }


        binding.btnAdd.setOnClickListener {
            val newUsername = binding.editText.text.toString()


            if (newUsername == "") {
                Toast.makeText(context, "Proporcione datos validos", Toast.LENGTH_SHORT)
                    .show()
            } else {
                db.collection("users")
                    .document(userID)
                    .update("username", newUsername)
                it.findNavController()
                    .navigate(SettingsProfileFragmentDirections.actionNavSettingsProfileFragmentToNavSettings())
                Toast.makeText(
                    context,
                    "Cuenta editada exitosamente",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }

        binding.logOutButton.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            LoginManager.getInstance().logOut()
            val intent = Intent(requireContext(), AuthenticationActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        return binding.root
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Selecciona una foto"),
            PICK_IMAGE_REQUEST
        )
    }

    companion object {
        private val PERMISSION_CODE = 1001
    }

    private fun uploadImageToFirebase(){
        if (filePath != null){
            val ref = storageReference.child("user_profile_image/" + UUID.randomUUID().toString())
            ref.putFile(filePath).addOnSuccessListener {taskSnapshot ->  
                Toast.makeText(requireContext(), "Imagen subida exitosamente",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{

            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    chooseImage()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.data != null
        ) {
            filePath = data.data!!
            profile_image.setImageURI(data.data)
        }
    }
}
