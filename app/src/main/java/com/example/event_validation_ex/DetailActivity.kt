package com.example.event_validation_ex


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.example.event_validation_ex.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        displayStudentInfo()
        setupListeners()
    }

    private fun displayStudentInfo() {
        val student = IntentCompat.getParcelableExtra(
            intent,
            RegisterActivity.EXTRA_STUDENT,
            Student::class.java
        )

        student?.let {
            binding.tvFullName.text = "Họ tên: ${it.fullName}"
            binding.tvEmail.text = "Email: ${it.email}"
            binding.tvPhone.text = "SĐT: ${it.phoneNumber}"
            binding.tvStudentId.text = "MSV: ${it.studentId}"
            binding.tvClassName.text = "Lớp: ${it.className}"
        }
    }

    private fun setupListeners() {
        binding.btnBackToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }
    }
}