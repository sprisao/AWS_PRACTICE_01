package com.example.aws_practice_01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.aws_practice_01.data.model.UserData
import com.example.aws_practice_01.ui.adapter.NoteRecyclerViewAdapter
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)

        // prepare our List view and RecyclerView (cells)
        setupRecyclerView(item_list)
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

    companion object {
        private const val TAG = "MainActivity"
    }
}
