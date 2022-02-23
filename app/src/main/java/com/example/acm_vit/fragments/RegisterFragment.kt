package com.example.acm_vit.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.acm_vit.ChatActivity
import com.example.acm_vit.R
import com.example.acm_vit.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment() {
    private var bind: FragmentRegisterBinding?=null
    private val binding get() = bind!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bind = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.loginTextView.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.registerButton.setOnClickListener{
            val name: String=binding.editTextNameRegister.text.toString().trim {it <=' '}
            val emailID: String = binding.editTextEmailRegister.text.toString().trim{it <=' '}
            val pass: String = binding.editTextPassRegister.text.toString().trim{it <=' '}
            val cpass: String =binding.editTextConfPassRegister.text.toString().trim{it <=' '}


            when {
                TextUtils.isEmpty(name) -> {
                    Toast.makeText(activity, "Enter Name", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(emailID) -> {
                    Toast.makeText(activity, "Enter Email ID", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(pass) -> {
                    Toast.makeText(activity, "Enter Password", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(cpass) -> {
                    Toast.makeText(activity, "Enter Confirm Password", Toast.LENGTH_SHORT).show()
                }
                pass != cpass -> {
                    Toast.makeText(activity, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    registerUser(name,emailID,pass)

                }
            }
        }
    }

    private fun registerUser(name: String,emailID:String,password:String) {
        auth.createUserWithEmailAndPassword(emailID, password)
            .addOnCompleteListener(requireActivity()){ task ->

                if (task.isSuccessful) {
                    Log.d("here", "createUserWithEmail:success")
                    val user = auth.currentUser
                    val userID= user!!.uid
                    Toast.makeText(
                        activity, "Authentication successful.",
                        Toast.LENGTH_SHORT
                    ).show()


                    // Write a message to the database
                    val database = Firebase.database
                    val myRef = database.getReference("Users").child(userID)

                    val hashMap: HashMap<String,String> = HashMap()
                    hashMap["userID"] = userID
                    hashMap["name"] = name
                    hashMap["email"] = emailID
                    hashMap["profileImage"] = " "

                    myRef.setValue(hashMap).addOnCompleteListener(requireActivity()){
                        if (it.isSuccessful){
                            val intent= Intent(activity,ChatActivity::class.java)
                            startActivity(intent)
                        }
                    }


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        activity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }
}



