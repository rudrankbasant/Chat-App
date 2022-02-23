package com.example.acm_vit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.acm_vit.R
import com.example.acm_vit.modelclass.Message
import com.example.acm_vit.modelclass.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MsgAdapter(private val context: Context, private val messageList: ArrayList<Message>):
    RecyclerView.Adapter<MsgAdapter.ViewHolder>() {

    private var firebaseUser: FirebaseUser?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        //0 for left side
        //1 for right side

        if(viewType==0){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_received,parent,false)
            return ViewHolder(view)
        }else{
            val view =LayoutInflater.from(parent.context).inflate(R.layout.item_sent,parent,false)
            return ViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val theUser=messageList[position]
        holder.messageTV.text=theUser.message
    }

    override fun getItemCount(): Int {
        return messageList.size
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val messageTV: TextView = view.findViewById(R.id.msgTextView)
        //val profileIcon: ImageView=view.findViewById(R.id.msgProfileIcon)
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser= FirebaseAuth.getInstance().currentUser!!

        if(messageList[position].SenderId != firebaseUser!!.uid){
            //it means left side message
            return 0
        }else{
            //it means right side
            return 1
        }
    }
}