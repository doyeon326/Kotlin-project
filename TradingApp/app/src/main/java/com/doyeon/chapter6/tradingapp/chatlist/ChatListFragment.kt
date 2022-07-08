package com.doyeon.chapter6.tradingapp.chatlist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.doyeon.chapter6.tradingapp.DBKey
import com.doyeon.chapter6.tradingapp.DBKey.Companion.CHILD_CHAT
import com.doyeon.chapter6.tradingapp.DBKey.Companion.DB_USERS
import com.doyeon.chapter6.tradingapp.R
import com.doyeon.chapter6.tradingapp.chatDetail.ChatRoomActivity
import com.doyeon.chapter6.tradingapp.databinding.FragmentChatlistBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatListFragment: Fragment(R.layout.fragment_chatlist)  {

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private var binding: FragmentChatlistBinding? = null
    private lateinit var chatListAdapter: ChatListAdapter
    private val chatRoomList = mutableListOf<ChatListItem>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentChatListBinding = FragmentChatlistBinding.bind(view)
        binding = fragmentChatListBinding

        chatListAdapter = ChatListAdapter(onItemClicked = { chatRoom ->
            //채팅방으로 이동하는 코드

            context?.let {
                val intent = Intent(it, ChatRoomActivity::class.java)
                intent.putExtra("chatKey", chatRoom.key)
                startActivity(intent)
            }

        })

        chatRoomList.clear()

        fragmentChatListBinding.chatListRecyclerView.adapter = chatListAdapter
        fragmentChatListBinding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)


        if (auth.currentUser == null ) {
            return
        }

        auth.currentUser?.uid?.let {
            val chatDB = Firebase.database.reference.child(DB_USERS).child(it).child(CHILD_CHAT)
            chatDB.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { data ->
                        val model = data.getValue(ChatListItem::class.java)


                        model ?: return

                        chatRoomList.add(model)
                        Log.d("ChatListFragment", "Chat list: ${model?.key}, chatRoomList coubnt: ${chatRoomList.count()}")
                    }
                    chatListAdapter.submitList(chatRoomList)
                    chatListAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }



    }

    override fun onResume() {
        super.onResume()

        chatListAdapter.notifyDataSetChanged()
    }
}