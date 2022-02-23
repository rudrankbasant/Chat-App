package com.example.acm_vit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.acm_vit.adapters.MsgAdapter
import com.example.acm_vit.adapters.RVAdapter
import com.example.acm_vit.databinding.ActivityMessagingBinding
import com.example.acm_vit.modelclass.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MessagingActivity : AppCompatActivity() {
    lateinit var binding: ActivityMessagingBinding
    private var firebaseUser: FirebaseUser?=null
    //var firebaseRef: DatabaseReference?=null
    var messageList = ArrayList<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMessagingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.msgRV.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        var theAdapter= MsgAdapter(this, messageList)
        binding.msgRV.adapter=theAdapter


        //fetching name
        val theName = intent.getStringExtra("theName")
        val userId = intent.getStringExtra("userId")
        binding.messagingName.text=theName

        //back button
        binding.backMessaging.setOnClickListener{
            val intent = Intent(this,ChatActivity::class.java)
            startActivity(intent)
        }

        firebaseUser= FirebaseAuth.getInstance().currentUser!!
        //firebaseRef= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)



        binding.sendButton.setOnClickListener{
            var msg: String= binding.msgEditText.text.toString()

            if(TextUtils.isEmpty(msg)){
                Toast.makeText(this,"Message not entered",Toast.LENGTH_SHORT).show()
            }else{
                sendMessage(msg,firebaseUser!!.uid,userId!!)
                binding.msgEditText.text.clear()
            }

        }

        getMessages(firebaseUser!!.uid,userId!!)


    }

    private fun sendMessage(msg:String,senderID: String, ReceiverId: String) {

        firebaseUser= FirebaseAuth.getInstance().currentUser!!
        var firebaseReference: DatabaseReference= FirebaseDatabase.getInstance().reference


        val hashMap: HashMap<String,String> = HashMap()
        hashMap["message"] = msg
        hashMap["SenderId"] = senderID
        hashMap["ReceiverId"] = ReceiverId

        firebaseReference.child("theMessages").push().setValue(hashMap)

    }


    private fun getMessages(senderID: String,receiverId: String){

        val firebaseReference: DatabaseReference= FirebaseDatabase.getInstance().getReference("theMessages")


        firebaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                //Fetching messageList Array
                for(dataSnapshot: DataSnapshot in snapshot.children){


                    val theMessage = dataSnapshot.getValue(Message::class.java)
                    Log.d("the message is ", theMessage.toString())

                    if(  theMessage!!.SenderId == senderID && theMessage.ReceiverId == receiverId ||
                        theMessage.SenderId == receiverId && theMessage.ReceiverId == senderID
                    ){
                        messageList.add(theMessage)
                    }
                }


             /*   Log.d("Sender ID ", senderID)
                Log.d("ReceiverID ", receiverId)
                Log.d("number of messages ", messageList.size.toString())*/


                //Updating RV
                val msgRVAdapter= MsgAdapter(this@MessagingActivity, messageList!!)
                binding.msgRV.adapter= msgRVAdapter

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }
}