package com.example.acm_vit

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.acm_vit.databinding.ActivityProfileBinding
import com.example.acm_vit.modelclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.io.IOException


class ProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding
    private val IMAGE_REQ_CODE=2020
    var imageUri: Uri?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backProfile.setOnClickListener{
            val intent = Intent(this,ChatActivity::class.java)
            startActivity(intent)
        }

        loadDataInProfile()

        binding.imageProfile.setOnClickListener{
            selectImage()
        }

        binding.saveProfileButton.setOnClickListener{
            updateProfileData()
        }

    }



    private fun updateProfileData() {

        var firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        var databaseReference: DatabaseReference= FirebaseDatabase.getInstance().getReference("Users").child(firebase.uid)
        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val hashMap: HashMap<String,String> = HashMap()
                hashMap["name"]=binding.nameProfile.text.toString()
                databaseReference.updateChildren(hashMap as Map<String, Any>)
                Toast.makeText(this@ProfileActivity,"Name Updated", Toast.LENGTH_SHORT).show()


            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

    }

    private fun loadDataInProfile() {
        var firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        var databaseReference: DatabaseReference= FirebaseDatabase.getInstance().getReference("Users").child(firebase.uid)


        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val theUser = dataSnapshot.getValue(User::class.java)

                if(theUser!=null){

                    //Name:
                    binding.nameProfile.setText(theUser.name)
                    //Image:
                    if(theUser.profileImage==" "){
                        binding.imageProfile.setImageResource(R.drawable.profie_image)
                    }else{
                        Glide.with(this@ProfileActivity).load(theUser.profileImage).into(binding.imageProfile)

                    }
                }else{
                    binding.nameProfile.setText("Name")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())

            }
        })
    }


    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        //intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMAGE_REQ_CODE){
            imageUri= data?.data!!

            if(imageUri!=null){
                binding.imageProfile.setImageURI(imageUri)
                try{
                    var bmp: Bitmap= MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                    binding.imageProfile.setImageBitmap(bmp)
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }else{
                Log.d("heree","img uri null")
            }


        }

    }
}