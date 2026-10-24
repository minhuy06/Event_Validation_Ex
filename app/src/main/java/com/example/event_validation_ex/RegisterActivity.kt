package com.example.event_validation_ex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.event_validation_ex.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var validatedStudent: Student? = null

    companion object {
        const val EXTRA_STUDENT = "extra_student"
        private val NAME_REGEX = Regex("^[\\p{L} .'-]{2,50}$")
        private val PHONE_REGEX = Regex("^(0[35789])[0-9]{8}$")
        private val STUDENT_ID_REGEX = Regex("^[0-9]{10,13}$")
        private val CLASS_REGEX = Regex("^[A-Za-z0-9_-]{3,15}$")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClearErrorOnTyping()
        setupListeners()
    }

    private fun setupClearErrorOnTyping() {
        binding.edtFullName.doAfterTextChanged { binding.tilFullName.error = null }
        binding.edtEmail.doAfterTextChanged { binding.tilEmail.error = null }
        binding.edtPhone.doAfterTextChanged { binding.tilPhone.error = null }
        binding.edtStudentId.doAfterTextChanged { binding.tilStudentId.error = null }
        binding.edtClassName.doAfterTextChanged { binding.tilClassName.error = null }
    }

    private fun setupListeners() {
        // Sự kiện bấm nút Đăng ký: kiểm tra validation -> hiển thị lỗi hoặc Snackbar
        binding.btnRegister.setOnClickListener {
            hideKeyboard()
            val student = validateAndBuildStudent()
            if (student != null) {
                validatedStudent = student
                Snackbar.make(
                    binding.rootLayout,
                    "Đăng ký thành công!",
                    Snackbar.LENGTH_LONG
                ).setAction("Xem chi tiết") {
                    navigateToDetail(student)
                }.show()
            }
        }

        // Sự kiện bấm nút chuyển trang trực tiếp giữa 2 Activity
        binding.btnGoToDetail.setOnClickListener {
            navigateToDetail(validatedStudent)
        }
    }

    private fun validateAndBuildStudent(): Student? {
        val fullName = binding.edtFullName.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val phone = binding.edtPhone.text.toString().trim()
        val studentId = binding.edtStudentId.text.toString().trim()
        val className = binding.edtClassName.text.toString().trim()

        var isValid = true

        // 1. Validate Họ tên (Không rỗng, không chứa số hoặc ký tự đặc biệt)
        if (fullName.isEmpty()) {
            binding.tilFullName.error = "Vui lòng nhập họ và tên"
            isValid = false
        } else if (!NAME_REGEX.matches(fullName)) {
            binding.tilFullName.error = "Họ tên chỉ được chứa chữ cái (từ 2 ký tự trở lên)"
            isValid = false
        }

        // 2. Validate Email (Đúng chuẩn định dạng email)
        if (email.isEmpty()) {
            binding.tilEmail.error = "Vui lòng nhập email"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Định dạng email không hợp lệ (VD: sv@ute.udn.vn)"
            isValid = false
        }

        // 3. Validate Số điện thoại (Đầu số di động Việt Nam 10 chữ số)
        if (phone.isEmpty()) {
            binding.tilPhone.error = "Vui lòng nhập số điện thoại"
            isValid = false
        } else if (!PHONE_REGEX.matches(phone)) {
            binding.tilPhone.error = "SĐT phải gồm 10 chữ số, bắt đầu bằng 03, 05, 07, 08, 09"
            isValid = false
        }

        // 4. Validate Mã sinh viên (Chỉ gồm chữ số, độ dài 10 - 13 số)
        if (studentId.isEmpty()) {
            binding.tilStudentId.error = "Vui lòng nhập mã sinh viên"
            isValid = false
        } else if (!STUDENT_ID_REGEX.matches(studentId)) {
            binding.tilStudentId.error = "Mã sinh viên phải là dãy số từ 10 đến 13 chữ số"
            isValid = false
        }

        // 5. Validate Lớp (Từ 3 đến 15 ký tự chữ/số)
        if (className.isEmpty()) {
            binding.tilClassName.error = "Vui lòng nhập lớp sinh hoạt"
            isValid = false
        } else if (!CLASS_REGEX.matches(className)) {
            binding.tilClassName.error = "Mã lớp không hợp lệ (VD: 24T1, 24IT1)"
            isValid = false
        }

        return if (isValid) {
            Student(fullName, email, phone, studentId, className)
        } else {
            null
        }
    }

    private fun navigateToDetail(student: Student?) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            student?.let { putExtra(EXTRA_STUDENT, it) }
        }
        startActivity(intent)
    }

    private fun hideKeyboard() {
        currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }
}