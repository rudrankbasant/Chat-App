package com.example.acm_vit.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.acm_vit.ChatActivity
import com.example.acm_vit.R
import com.example.acm_vit.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginFragment : Fragment() {

    private var bind: FragmentLoginBinding?=null
    private val binding get() = bind!!

    private lateinit var auth: FirebaseAuth
    private var firebaseUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bind = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firebaseUser=auth.currentUser


        //Check if user already logged in:
        /*if(firebaseUser!=null){
            val intent= Intent(activity, ChatActivity::class.java)
            startActivity(intent)
        }*/

        binding.registerTextView.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginButton.setOnClickListener{
            val emailID: String = binding.editTextEmailLogin.text.toString().trim{it <=' '}
            val pass: String = binding.editTextPassLogin.text.toString().trim{it <=' '}

            when{
                TextUtils.isEmpty(emailID)-> Toast.makeText(activity, "Enter Email ID", Toast.LENGTH_SHORT).show()
                TextUtils.isEmpty(pass)-> Toast.makeText(activity, "Enter Password", Toast.LENGTH_SHORT).show()
                else -> loginUser(emailID,pass)
            }
        }
    }

    private fun loginUser(emailID: String, password:String) {
        auth.signInWithEmailAndPassword(emailID, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Heree", "signInWithEmail:success")
                    Toast.makeText(activity, "Logged In Successfully.",
                        Toast.LENGTH_SHORT).show()

                    //val user = auth.currentUser

                    val intent= Intent(activity, ChatActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }


}