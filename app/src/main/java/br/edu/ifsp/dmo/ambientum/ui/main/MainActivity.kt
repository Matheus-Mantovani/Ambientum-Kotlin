package br.edu.ifsp.dmo.ambientum.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.edu.ifsp.dmo.ambientum.R
import br.edu.ifsp.dmo.ambientum.core.receiver.NotificationReceiver
import br.edu.ifsp.dmo.ambientum.databinding.ActivityMainBinding
import br.edu.ifsp.dmo.ambientum.ui.monitor.MonitorFragment
import br.edu.ifsp.dmo.ambientum.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_POST_NOTIFICATIONS = 1001
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(MonitorFragment())
        requestNotificationPermission()
        checkScheduleExactAlarmPermission()
        scheduleNotification()

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bnav_monitor -> loadFragment(MonitorFragment())
                R.id.bnav_settings -> loadFragment(SettingsFragment())
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_POST_NOTIFICATIONS
            )
        }
    }

    private fun checkScheduleExactAlarmPermission() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivity(intent)
        }
    }

    private fun scheduleNotification() {
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val triggerTime = System.currentTimeMillis() + 10_000

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }
}
