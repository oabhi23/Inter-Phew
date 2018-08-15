package com.example.abhi.appster

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Job application entry form
 */
class JobEntry : AppCompatActivity() {
    //reference to database
    private var mDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("applications")

    lateinit var companyName : EditText
    lateinit var position : EditText
    lateinit var location : EditText
    lateinit var interviewTime : EditText
    lateinit var status : EditText
    lateinit var contact : EditText
    lateinit var contactInfo : EditText
    lateinit var followUp : EditText
    lateinit var notes : EditText

    lateinit var saveButton : Button
    lateinit var interviewCheckBox : CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_entry)

        //get reference to edittext views
        companyName = findViewById(R.id.company)
        position = findViewById(R.id.position)
        location = findViewById(R.id.location)
        interviewTime = findViewById(R.id.interview)
        status = findViewById(R.id.status)
        contact = findViewById(R.id.contact)
        contactInfo = findViewById(R.id.contactInfo)
        followUp = findViewById(R.id.followUp)
        notes = findViewById(R.id.notes)

        interviewTime.visibility = View.GONE
        status.visibility = View.GONE
        contact.visibility = View.GONE
        contactInfo.visibility = View.GONE
        followUp.visibility = View.GONE

        //get reference to save button
        saveButton = findViewById(R.id.saveBtn)

        //get reference to checkbox - add additional info
        interviewCheckBox = findViewById(R.id.interviewCheck)

        //set additional fields to visible if user has an interview
        interviewCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(interviewCheckBox.isChecked) {
                interviewTime.visibility = View.VISIBLE
                status.visibility = View.VISIBLE
                contact.visibility = View.VISIBLE
                contactInfo.visibility = View.VISIBLE
                followUp.visibility = View.VISIBLE
            } else {
                interviewTime.visibility = View.GONE
                status.visibility = View.GONE
                contact.visibility = View.GONE
                contactInfo.visibility = View.GONE
                followUp.visibility = View.GONE
            }
        }

        //save data on click of save button
        saveButton.setOnClickListener{
            //get current day
            val today = Calendar.getInstance()
            val date = SimpleDateFormat("yyyy-MM-dd").format(today.time)

            //convert edittext to string values
            val company = companyName.text.toString()
            val pos = position.text.toString()
            val loc = location.text.toString()
            val time = interviewTime.text.toString()
            val stat = status.text.toString()
            val cont = contact.text.toString()
            val contInfo = contactInfo.text.toString()
            val follow = followUp.text.toString()
            val note = notes.text.toString()

            //call save to database with params
            loadDatabase(mDatabaseReference, date, company, pos, loc, time, stat, cont, contInfo, follow, note)
        }
    }

    /**
     * Add to database
     */
    private fun loadDatabase(firebaseDatabase: DatabaseReference, date: String, company: String, pos: String, loc: String,
                     time: String, stat: String, cont: String, contInfo: String, follow: String, note: String){

        val applicationId = firebaseDatabase.push().key
        val jobApplication = JobApplication(applicationId, date, company, pos, loc, time, stat, cont, contInfo, follow, note)
        firebaseDatabase.child(applicationId).setValue(jobApplication).addOnCompleteListener{
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
        }
    }
}
