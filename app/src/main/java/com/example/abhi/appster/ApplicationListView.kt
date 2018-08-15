package com.example.abhi.appster

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.google.firebase.database.*

/**
 * Displays job applications in list view
 */

class ApplicationListView : AppCompatActivity() {

    lateinit var ref: DatabaseReference
    private lateinit var listView: ListView
    lateinit var appList: MutableList<JobApplication>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application_list_view)

        appList = mutableListOf()
        listView = findViewById(R.id.app_list_view)
        ref = FirebaseDatabase.getInstance().getReference("applications")

        retrieveApplications()
    }

    /**
     * Retrieves applications and uses ApplicationAdapter class to display a view of job application entries
     */
    private fun retrieveApplications() {
        ref.orderByChild("date").startAt("2018-08-13").endAt("2018-08-15").
                addValueEventListener(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError?) {
                //empty
            }
            override fun onDataChange(p0: DataSnapshot?) {
                if (p0!!.exists()) {
                    appList.clear()
                    for (i in p0.children) {
                        ref.orderByChild("date").startAt("2018-08-13").endAt("2018-08-15")
                        val app : JobApplication? = i.getValue(JobApplication::class.java)
                        appList.add(app!!)
                    }

                    val adapter = ApplicationAdapter(this@ApplicationListView, R.layout.application, appList)
                    listView.adapter = adapter
                }
            }
        })
    }
}
