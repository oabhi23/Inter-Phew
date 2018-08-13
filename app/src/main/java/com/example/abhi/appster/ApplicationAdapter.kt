package com.example.abhi.appster

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

class ApplicationAdapter(val mContext: Context, val layoutId: Int, val applicationList: List<JobApplication>)
    :ArrayAdapter<JobApplication>(mContext, layoutId, applicationList){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val layoutInflater : LayoutInflater = LayoutInflater.from(mContext)
        val view : View = layoutInflater.inflate(layoutId, null)

        val companyName = view.findViewById<TextView>(R.id.company)
        val positionName = view.findViewById<TextView>(R.id.position)

        val deleteButton = view.findViewById<ImageButton>(R.id.delete)
        deleteButton.visibility = View.GONE
        val editButton = view.findViewById<ImageButton>(R.id.edit)
        editButton.visibility = View.GONE

        companyName.setOnClickListener {
            deleteButton.visibility = View.VISIBLE
            editButton.visibility = View.VISIBLE
        }

        val jobApplication = applicationList[position]

        //display company name and position applied for
        companyName.text = jobApplication.company + ":"
        positionName.text = jobApplication.position

        editButton.setOnClickListener{
            editApplication(jobApplication)
        }

        deleteButton.setOnClickListener{
            deleteApplication(jobApplication)
        }

        return view
    }

    /**
     * Edits single application
     */
    private fun editApplication(jobApplication: JobApplication) {
        val builder = AlertDialog.Builder(mContext) //modal to update
        builder.setTitle("Update Application")
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.activity_job_entry, null)

        val companyName = view.findViewById<EditText>(R.id.company)
        val position = view.findViewById<EditText>(R.id.position)
        val location = view.findViewById<EditText>(R.id.location)
        val interviewTime = view.findViewById<EditText>(R.id.interview)
        val status = view.findViewById<EditText>(R.id.status)
        val contact = view.findViewById<EditText>(R.id.contact)
        val contactInfo = view.findViewById<EditText>(R.id.contactInfo)
        val followUp = view.findViewById<EditText>(R.id.followUp)
        val notes = view.findViewById<EditText>(R.id.notes)

        val saveButton = view.findViewById<Button>(R.id.saveBtn)

        val checkBox = view.findViewById<CheckBox>(R.id.interviewCheck)
        checkBox.visibility = View.GONE

        companyName.setText(jobApplication.company)
        position.setText(jobApplication.position)
        location.setText(jobApplication.location)
        interviewTime.setText(jobApplication.interviewTime)
        status.setText(jobApplication.status)
        contact.setText(jobApplication.contact)
        contactInfo.setText(jobApplication.contactInfo)
        followUp.setText(jobApplication.followUp)
        notes.setText(jobApplication.notes)

        builder.setView(view)

        //updates data on click of save button
        saveButton.setOnClickListener{
            val mDatabase = FirebaseDatabase.getInstance().getReference("applications")

            val company = companyName.text.toString()
            val pos = position.text.toString()
            val loc = location.text.toString()
            val time = interviewTime.text.toString()
            val stat = status.text.toString()
            val cont = contact.text.toString()
            val contInfo = contactInfo.text.toString()
            val follow = followUp.text.toString()
            val note = notes.text.toString()

            val jobApplication = JobApplication(jobApplication.id, jobApplication.date, company, pos,
                    loc, time, stat, cont, contInfo, follow, note)

            mDatabase.child(jobApplication.id).setValue(jobApplication) //set to new application created
            Toast.makeText(mContext, "Updated", Toast.LENGTH_LONG).show() //notify user
        }

        val alert = builder.create()
        alert.show()
    }

    /**
     * Deletes single application
     */
    private fun deleteApplication(jobApplication: JobApplication) {
        val mDatabase = FirebaseDatabase.getInstance().getReference("applications")
        mDatabase.child(jobApplication.id).removeValue() //remove reference of app id
        Toast.makeText(mContext, "Deleted", Toast.LENGTH_LONG).show()
    }
}