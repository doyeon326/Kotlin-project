package com.doyeon.chapter5.tinder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class LikeActivity: AppCompatActivity(), CardStackListener {

    private val adapter = CardItemAdapter()
    private val cardItem = mutableListOf<CardItem>()
    private val manager by lazy {
        CardStackLayoutManager(this, this)
    }

    private lateinit var userDB: DatabaseReference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like)

        userDB = Firebase.database.reference.child("Users")
        val currentUserDB = userDB.child(getCurrentUserID())
        currentUserDB.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("name").value == null ) {
                    showNameInputPopup()
                    return
                }
                getUnSelectedUsers()
                //todo 유저정보를 갱신해라
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        initCardStackView()
        initSignOutButton()
        initMathcedListButton()
    }

    private fun initSignOutButton() {
        val signOutButton = findViewById<Button>(R.id.signOutButton)
        signOutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun initMathcedListButton() {
        val matchedListButton = findViewById<Button>(R.id.matchListButton)
        matchedListButton.setOnClickListener {
            startActivity(Intent(this, MatchedUserActivity::class.java))
        }
    }

    private fun initCardStackView() {
        val stackView = findViewById<CardStackView>(R.id.cardStackView)

        stackView.layoutManager = manager
        stackView.adapter = adapter
    }

    private fun getUnSelectedUsers() {
        userDB.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.child("userId").value != getCurrentUserID()
                    && snapshot.child("likedBy").child("like").hasChild(getCurrentUserID()).not()
                    && snapshot.child("likedBy").child("disLike").hasChild(getCurrentUserID()).not()) {

                    val userId = snapshot.child("userId").value.toString()
                    var name = "undecided"
                    if(snapshot.child("name").value != null) {
                        name = snapshot.child("name").value.toString()
                    }
                    cardItem.add(CardItem(userId, name))
                    adapter.submitList(cardItem)
                    adapter.notifyDataSetChanged()
                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                cardItem.find { it.userId == snapshot.key }?.let {
                    it.name = snapshot.child("name").value.toString()
                }
                adapter.submitList(cardItem)
                adapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) { }

        })
    }
    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            Toast.makeText(this, "로그인이 되어있지 않습니다", Toast.LENGTH_SHORT).show()
            finish()
        }
        return auth.currentUser?.uid.orEmpty()
    }

    private fun showNameInputPopup() {
        val editText = EditText(this)

        AlertDialog.Builder(this)
            .setView(editText)
            .setTitle("이름을 입력해주세요")
            .setPositiveButton("저장") { _, _ ->
                if (editText.text.isEmpty()) {
                    showNameInputPopup()
                } else {
                    saveUserName(editText.text.toString())
                }
            }
            .setCancelable(false)
            .show()
    }

    private fun saveUserName(name: String ) {
        val userId = getCurrentUserID()
        val currentUserDB = userDB.child(userId)
        val user = mutableMapOf<String, Any>()
        user["userId"] = userId
        user["name"] = name
        currentUserDB.updateChildren(user)

        getUnSelectedUsers()
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    private fun like() {
        val card = cardItem[manager.topPosition -1]
        cardItem.removeFirst()

        userDB.child(card.userId)
            .child("likedBy")
            .child("like")
            .child(getCurrentUserID())
            .setValue(true)
            saveMatchIfOtherUserLikedMe(card.userId)


        Toast.makeText(this, "${card.name}님을 Like 하셨습니다.",Toast.LENGTH_SHORT).show()
    }

    private fun dislike() {
        val card = cardItem[manager.topPosition -1]
        cardItem.removeFirst()

        userDB.child(card.userId)
            .child("likedBy")
            .child("disLike")
            .child(getCurrentUserID())
            .setValue(true)


        Toast.makeText(this, "${card.name}님을 disLike 하셨습니다.",Toast.LENGTH_SHORT).show()
    }

    private fun saveMatchIfOtherUserLikedMe(otherUserId: String) {
        val otherUserDB = userDB.child(getCurrentUserID()).child("likedBy").child("like").child(otherUserId)
        otherUserDB.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("LikeActivity", "onDataChange ${snapshot.value}")
                if (snapshot.value == true) {
                    Log.d("LikeActivity", "onDataChange -- saveMatchedIfOtherUserLikedMe ${snapshot.value}")
                    userDB.child(getCurrentUserID())
                        .child("likedBy")
                        .child("match")
                        .child(otherUserId)
                        .setValue(true)

                    userDB.child(otherUserId)
                        .child("likedBy")
                        .child("match")
                        .child(getCurrentUserID())
                        .setValue(true)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Right -> like()
            Direction.Left -> dislike()

            else -> {

            }
        }
    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardDisappeared(view: View?, position: Int) {
    }
}