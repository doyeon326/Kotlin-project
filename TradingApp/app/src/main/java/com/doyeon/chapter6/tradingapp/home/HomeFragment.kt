package com.doyeon.chapter6.tradingapp.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.doyeon.chapter6.tradingapp.DBKey.Companion.CHILD_CHAT
import com.doyeon.chapter6.tradingapp.DBKey.Companion.DB_ARTICLES
import com.doyeon.chapter6.tradingapp.DBKey.Companion.DB_USERS
import com.doyeon.chapter6.tradingapp.R
import com.doyeon.chapter6.tradingapp.chatlist.ChatListItem
import com.doyeon.chapter6.tradingapp.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val articleList = mutableListOf<ArticleModel>()
    private lateinit var userDB: DatabaseReference
    private lateinit var articleDB: DatabaseReference
    private var binding: FragmentHomeBinding? = null
    private lateinit var articleAdapter: ArticleAdapter
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val listener = object: ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            //데이터 맵핑
            val articleModel = snapshot.getValue(ArticleModel::class.java)
            //null 일경우
            articleModel?: return
            //아닐경우,
            articleList.add(articleModel)
            articleAdapter.submitList(articleList)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

        override fun onCancelled(error: DatabaseError) {}

    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //OnCreatedView
        articleList.clear()
        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding




        fragmentHomeBinding.addFloatingButton.setOnClickListener {

            //requireContext() 를 쓰는 이유는 context 가 널일수도 있기때문이다. 만약 쓰려면  context?.let { it.. } 으로 쓸 수 있다.

            Log.d("HomeFragment", "current User, ${auth.currentUser}, ${auth.currentUser?.email}")
            if (auth.currentUser != null) {
                val intent = Intent(requireContext(), AddArticleActivity::class.java)
                startActivity(intent)
            } else {
                Snackbar.make(view,"로그인 후 사용해 주세요", Snackbar.LENGTH_LONG).show()
            }

        }


        articleDB = Firebase.database.reference.child(DB_ARTICLES)
        userDB = Firebase.database.reference.child(DB_USERS)

        articleAdapter = ArticleAdapter(onItemClicked = { articleModel ->
            if (auth.currentUser != null) {
                //로그인을 한 상태
                if (auth.currentUser?.uid != articleModel.sellerId) {
                    val chatRoom = ChatListItem(
                        buyerId = auth.currentUser?.uid ?: "",
                        sellerId = articleModel.sellerId,
                        itemTitle = articleModel.title,
                        key = System.currentTimeMillis()

                    )

                    userDB.child(auth.currentUser?.uid ?: "")
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(chatRoom)

                    userDB.child(articleModel.sellerId)
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(chatRoom)


                    Snackbar.make(view, "채팅방이 생성되었습니다. 채팅탭에서 확인해주세요", Snackbar.LENGTH_LONG).show()
                } else {
                    //내가 올린 아이템
                    Snackbar.make(view, "내가 올린 아이템입니다", Snackbar.LENGTH_LONG).show()
                }
            } else {
              //로그인을 안한 상태
                Snackbar.make(view, "로그인 후 사용해주세요", Snackbar.LENGTH_LONG).show()
            }



        })
//        articleAdapter.submitList(mutableListOf<ArticleModel>().apply {
//            add(ArticleModel("0", "aaaa", 1000000, "5000원", "" ))
//            add(ArticleModel("0", "aaaa", 2000000, "10000원", "" ))
//        })
        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = articleAdapter
        articleDB.addChildEventListener(listener)

    }

    override fun onResume() {
        super.onResume()
        articleAdapter.notifyDataSetChanged()
    }
    override fun onDestroyView() {
        super.onDestroyView()

        articleDB.removeEventListener(listener)
    }
}