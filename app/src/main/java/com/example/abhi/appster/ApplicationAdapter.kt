package com.example.abhi.appster

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
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

        var companyName = view.findViewById<TextView>(R.id.company)
        var deleteButton = view.findViewById<ImageButton>(R.id.delete)
        var editButton = view.findViewById<ImageButton>(R.id.edit)

        deleteButton.visibility = View.INVISIBLE
        editButton.visibility = View.INVISIBLE

        var jobApplication = applicationList[position]

        //display company name and position applied for
        companyName.text = jobApplication.company + " - " + jobApplication.position

        var typeface : Typeface? = ResourcesCompat.getFont(this.mContext, R.font.roboto_medium)
        companyName!!.setTypeface(typeface, Typeface.NORMAL)

        editButton.setOnClickListener {
            editApplication(jobApplication)
        }

        deleteButton.setOnClickListener {
            deleteApplication(jobApplication)
        }

        var flag = 0
        companyName.setOnClickListener {
            if (flag == 0) {
                deleteButton.visibility = View.VISIBLE
                editButton.visibility = View.VISIBLE
                flag++
            } else {
                deleteButton.visibility = View.GONE
                editButton.visibility = View.GONE
                flag--
            }
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

        //alert dialog to confirm delete
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle("Confirm Delete")
        builder.setMessage("Are you sure you want to delete this application?")

        builder.setPositiveButton("Yes") {dialog, which ->
            mDatabase.child(jobApplication.id).removeValue() //remove reference of app id
            Toast.makeText(mContext, "Deleted", Toast.LENGTH_LONG).show()
        }

        builder.setNegativeButton("No") {dialog, which -> }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}