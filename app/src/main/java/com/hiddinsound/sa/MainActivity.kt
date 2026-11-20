package com.hiddinsound.sa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hiddinsound.sa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()

        binding.btnPlay.setOnClickListener {
            if (isPlaying) {
                stopService(Intent(this, HiddenSoundService::class.java))
                binding.btnPlay.text = "▶ PLAY HIDDIN SOUND"
                binding.txtStatus.text = "Stopped"
                binding.txtStatus.setTextColor(getColor(android.R.color.holo_red_light))
                isPlaying = false
            } else {
                val intent = Intent(this, HiddenSoundService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                } else {
                    startService(intent)
                }
                binding.btnPlay.text = "⏸ STOP"
                binding.txtStatus.text = "Playing... 🔊"
                binding.txtStatus.setTextColor(getColor(android.R.color.holo_green_light))
                isPlaying = true
            }
        }

        binding.btnVolumeUp.setOnClickListener {
            Toast.makeText(this, "Volume Up - Coming in full version", Toast.LENGTH_SHORT).show()
        }
        binding.btnVolumeDown.setOnClickListener {
            Toast.makeText(this, "Volume Down - Coming in full version", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissions() {
        val perms = mutableListOf(Manifest.permission.RECORD_AUDIO)
        if (Build.VERSION.SDK_INT >= 33) {
            perms.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (Build.VERSION.SDK_INT >= 31) {
            perms.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        val needed = perms.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (needed.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, needed.toTypedArray(), 100)
        }
    }
}
