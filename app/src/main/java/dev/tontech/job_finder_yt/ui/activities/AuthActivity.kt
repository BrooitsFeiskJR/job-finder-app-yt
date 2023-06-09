package dev.tontech.job_finder_yt.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.tontech.job_finder_yt.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private var binding: ActivityAuthBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}