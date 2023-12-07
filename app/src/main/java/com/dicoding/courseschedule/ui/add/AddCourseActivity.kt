package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.list.ListViewModelFactory
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        supportActionBar?.title = resources.getString(R.string.add_course)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ListViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                val courseName: TextView = findViewById(R.id.ed_course_name)
                val lecturer: TextView = findViewById(R.id.ed_lecturer)
                val note: TextView = findViewById(R.id.ed_note)
                val day: Spinner = findViewById(R.id.spinner_day)
                val startTime : TextView = findViewById(R.id.tv_start_time)
                val endTime : TextView = findViewById(R.id.tv_end_time)

                val edCourseName = courseName.text.toString().trim()
                val edLecture= lecturer.text.toString().trim()
                val edNote = note.text.toString().trim()
                val selectedDayPosition = day.selectedItemPosition

                if (edCourseName.isEmpty() || startTime.text.isBlank() || endTime.text.isNullOrEmpty()) {
                    val rootView: View = findViewById(android.R.id.content)
                    Snackbar.make(rootView, R.string.input_empty_message, Snackbar.LENGTH_SHORT).show()
                }
                else {
                    viewModel.insertCourse(
                        courseName = edCourseName,
                        day = selectedDayPosition,
                        startTime = startTime.text.toString(),
                        endTime = endTime.text.toString(),
                        lecturer = edLecture,
                        note = edNote,
                    )
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showTimePickerStartTime(view: View) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "startPicker")
    }

    fun showTimePickerEndTime(view: View) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "endPicker")
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        if (tag == "startPicker") {
            findViewById<TextView>(R.id.tv_start_time).text = timeFormat.format(calendar.time)
        } else {
            findViewById<TextView>(R.id.tv_end_time).text = timeFormat.format(calendar.time)
        }
    }

}