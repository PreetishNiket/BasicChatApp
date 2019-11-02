package com.codingblocks.firebaseauth

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.database.FirebaseListAdapter
import com.firebase.ui.database.FirebaseListOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*


class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val user = FirebaseAuth.getInstance().currentUser

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName("Preetish")
            .build()

        user?.updateProfile(profileUpdates)


        button2.setOnClickListener {
            FirebaseDatabase.getInstance().reference.child("messages")
                .push()
                .setValue(
                    chatmessage(
                        input.text.toString(),
                        FirebaseAuth.getInstance().currentUser?.displayName!!,
                        Date().time

                    )
                )
            input.setText("")
        }
        val query = FirebaseDatabase.getInstance()
            .reference
            .child("messages")

       val options =FirebaseListOptions.Builder<chatmessage>()
           .setLayout(R.layout.item_chat)
           .setQuery(query,chatmessage::class.java)
           .build()


        val adapter= object : FirebaseListAdapter<chatmessage>(options) {
            override fun populateView(v: View, model: chatmessage, position: Int) {
                val message =v.findViewById<TextView>(R.id.message)
                val name=v.findViewById<TextView>(R.id.name)
                val time=v.findViewById<TextView>(R.id.time)

                message.text=model.message
                name.text=model.name
                time.text=android.text.format.DateFormat.format("dd-MMM-yy  HH:mm:ss",model.time)
            }
        }
        adapter.startListening()

        messages.adapter=adapter

    }
}
data class chatmessage (
    val message:String,
    val name:String,
    val time:Long

)
{
    constructor():this("","",0L)
}
