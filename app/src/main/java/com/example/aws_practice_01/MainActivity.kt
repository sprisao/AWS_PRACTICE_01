package com.example.aws_practice_01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.aws_practice_01.backend.Backend
import com.example.aws_practice_01.data.model.UserData
import com.example.aws_practice_01.ui.adapter.NoteRecyclerViewAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.item_list
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)

        // prepare our List view and RecyclerView (cells)
        setupRecyclerView(item_list)

        setupAuthButton(UserData)

        UserData.isSignedIn.observe(this, Observer<Boolean> {isSignedUp ->

            Timber.i("User signed in: $isSignedUp")
            if (isSignedUp) {
                fabAuth.setImageResource(R.drawable.baseline_lock_open_24)
            }else {
                fabAuth.setImageResource(R.drawable.ic_baseline_lock)
            }
        })
    }

    // recycler view is the list of cells
    private fun setupRecyclerView(recyclerView: RecyclerView?) {

        // update individual cell when the Note data are modified
        UserData.notes().observe(this, Observer<MutableList<UserData.Note>> { notes ->
            Log.d(TAG, "Note observer received ${notes.size} notes")

            // let's create a RecyclerViewAdapter that manages the individual cells
            recyclerView?.adapter = NoteRecyclerViewAdapter(notes)
        })
    }

    private fun setupAuthButton(userData: UserData){
        // register a click listener
        fabAuth.setOnClickListener { view ->
            val authButton = view as FloatingActionButton

            if (userData.isSignedIn.value!!){
                authButton.setImageResource(R.drawable.baseline_lock_open_24)
                Backend.signOut()
            } else {
                authButton.setImageResource(R.drawable.ic_baseline_lock)
                Backend.signIn(this)
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
