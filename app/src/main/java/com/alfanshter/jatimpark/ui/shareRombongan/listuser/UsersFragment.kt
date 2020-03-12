package com.alfanshter.jatimpark.ui.shareRombongan.listuser


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfanshter.jatimpark.R
import com.alfanshter.jatimpark.ui.shareRombongan.listuser.Adapter.UsersRecyclerAdapter
import com.alfanshter.jatimpark.ui.shareRombongan.listuser.Model.Users
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class UsersFragment : Fragment() {
    lateinit var mUsersListView: RecyclerView

    lateinit var usersList: MutableList<Users>
    private var usersRecyclerAdapter: UsersRecyclerAdapter? = null

    private var mFirestore: FirebaseFirestore? = null

    fun UsersFragment() { // Required empty public constructor
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_users, container, false)
        mUsersListView = root.findViewById(R.id.users_list_view)
        mFirestore = FirebaseFirestore.getInstance()


        usersList = ArrayList()
        usersRecyclerAdapter = UsersRecyclerAdapter(container!!.context,
            usersList as ArrayList<Users>
        )

        mUsersListView.setHasFixedSize(true)
        mUsersListView.layoutManager = LinearLayoutManager(container.context)
        mUsersListView.adapter = usersRecyclerAdapter

        return root
    }

    override fun onStart() {
        super.onStart()

  usersList.clear()

        mFirestore!!.collection("Users").addSnapshotListener(
            activity!!,
            object : EventListener<QuerySnapshot> {

                override fun onEvent(documentSnapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {
                    for (doc in documentSnapshots!!.documentChanges) {
                        if (doc.type == DocumentChange.Type.ADDED) {
                            val user_id = doc.document.id
                            val users: Users = doc.document.toObject(Users::class.java).withId(user_id)
                            usersList.add(users)
                            usersRecyclerAdapter!!.notifyDataSetChanged()
                        }
                    }
                }
            })
    }


}
