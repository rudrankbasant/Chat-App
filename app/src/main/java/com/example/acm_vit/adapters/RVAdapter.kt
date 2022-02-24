package com.example.acm_vit.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.acm_vit.MessagingActivity
import com.example.acm_vit.R
import com.example.acm_vit.modelclass.User
import com.example.acm_vit.adapters.RVAdapter.ViewHolder

class RVAdapter(private val context: Context, private val userList: ArrayList<User>):
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user= userList[position]
        holder.name.text=user.name
        Glide.with(context).load(user.profileImage).placeholder(R.drawable.profie_image).into(holder.userImg)
        val theName= user.name

        holder.userItemLayout.setOnClickListener{
            val intent = Intent(context, MessagingActivity::class.java)
            intent.putExtra("theName",theName)
            intent.putExtra("userId",user.userID)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView= view.findViewById(R.id.nameTextView)
        val userImg: ImageView=view.findViewById(R.id.userImgView)
        val userItemLayout: LinearLayout=view.findViewById(R.id.userItemLayout)
    }


}