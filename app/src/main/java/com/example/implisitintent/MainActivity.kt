package com.example.implisitintent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.CalendarContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar
import java.util.TimeZone

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val _btnKirimPesan = findViewById<Button>(R.id.btnKirimPesan)
        _btnKirimPesan.setOnClickListener {
            val _sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra("address","08112345")
                putExtra( "sms_body","ISI SMS")
                type="text/plain"
            }
            if (_sendIntent.resolveActivity( packageManager) != null) {
                startActivity(_sendIntent)
            }
        }
        val _btnSetAlarm = findViewById<Button>(R.id.btnSetAlarm)
        _btnSetAlarm.setOnClickListener {
            val _alarmIntent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE,"COBA ALARM")
                putExtra(AlarmClock.EXTRA_HOUR,20)
                putExtra(AlarmClock.EXTRA_MINUTES,15)
                putExtra(AlarmClock.EXTRA_SKIP_UI,true)
            }
            startActivity(_alarmIntent)
        }
        val _btnSetTimer = findViewById<Button>(R.id.btnSetTimer)
        _btnSetTimer.setOnClickListener {
            val _timerIntent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE,"COBA ALARM")
                putExtra(AlarmClock.EXTRA_LENGTH,20)
                putExtra(AlarmClock.EXTRA_SKIP_UI,true)
            }
            startActivity(_timerIntent)
        }
        val _btnOpenURL = findViewById<Button>(R.id.btnOpenURL)
        _btnOpenURL.setOnClickListener {
            val _etURL = findViewById<EditText>(R.id.etURL)
            var _webIntent = Intent (
                Intent.ACTION_VIEW,
                Uri.parse("http://"+_etURL.text.toString())
            )

            if (_webIntent.resolveActivity(packageManager) != null) {
                startActivity(_webIntent)
            } else {
                Toast.makeText(
                    this,
                    "Tidak ada Aplikasi Browser ditemukan",
                        Toast.LENGTH_LONG
                ).show()
            }
        }
        val _btnSetEvent = findViewById<Button>(R.id.btnSetEvent)
        _btnSetEvent.setOnClickListener {
            // Langkah 2: Mengambil tanggal dan waktu saat ini
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"))
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // Langkah 3: Menampilkan DatePicker dan TimePicker, lalu menjalankan Intent
            val datePickerDialog =
                DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                    val timePickerDialog =
                        TimePickerDialog(this, { _, selectedHour, selectedMinute ->

                            // Menyimpan waktu yang dipilih pengguna
                            val selectedDateTime = Calendar.getInstance().apply {
                                set(
                                    selectedYear,
                                    selectedMonth,
                                    selectedDay,
                                    selectedHour,
                                    selectedMinute
                                )
                            }

                            // Mengatur waktu selesai event (misalnya ditambah 1 jam dari waktu mulai)
                            val endTime = selectedDateTime.clone() as Calendar
                            endTime.add(Calendar.HOUR_OF_DAY, 1)

                            // Membuat intent untuk menambahkan event ke kalender
                            val eventIntent = Intent(Intent.ACTION_INSERT).apply {
                                data = CalendarContract.Events.CONTENT_URI
                                putExtra(CalendarContract.Events.TITLE, "Meeting")
                                putExtra(CalendarContract.Events.EVENT_LOCATION, "Kantor")
                                putExtra(CalendarContract.Events.DESCRIPTION, "Deskripsi Meeting")
                                putExtra(CalendarContract.Events.ALL_DAY, false)
                                putExtra(
                                    CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                    selectedDateTime.timeInMillis
                                )
                                putExtra(
                                    CalendarContract.EXTRA_EVENT_END_TIME,
                                    endTime.timeInMillis
                                )
                            }
                            startActivity(eventIntent)

                        }, hour, minute, true)
                    timePickerDialog.show()
                }, year, month, day)
            datePickerDialog.show()
        }

    }
}