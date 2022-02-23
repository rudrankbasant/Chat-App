package com.example.acm_vit

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.acm_vit.adapters.RVAdapter
import com.example.acm_vit.databinding.ActivityChatBinding
import com.example.acm_vit.modelclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    var userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userRV.layoutManager=LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        var myAdapter= RVAdapter(this, userList)
        binding.userRV.adapter=myAdapter


        //Getting Data From Firebase:
        getUserList()


        binding.profileIcon.setOnClickListener{
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUserList() {

        var firebase: FirebaseUser =FirebaseAuth.getInstance().currentUser!!
        var databaseReference: DatabaseReference=FirebaseDatabase.getInstance().getReference("Users")


        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList.clear()

                //Fetching userList Array
                for(snapshot: DataSnapshot in dataSnapshot.children){
                    val user = snapshot.getValue(User::class.java)
                    if(user!!.userID != firebase.uid){
                        userList.add(user)
                    }else{
                        //Updating Name
                        binding.userChatName.text=user.name
                    }
                }

                //Updating RV
                val rvAdapter= RVAdapter(this@ChatActivity, userList)
                binding.userRV.adapter=rvAdapter
                Log.d("here",userList.size.toString())



            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                Toast.makeText(this@ChatActivity,databaseError.message,Toast.LENGTH_SHORT).show()
            }

        })

    }
}