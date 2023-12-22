package com.tri.sulton.inigua.view.register

import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tri.sulton.inigua.data.api.model.Register
import com.tri.sulton.inigua.data.api.model.response.CommonResponse
import com.tri.sulton.inigua.data.api.model.response.LoginResponse
import com.tri.sulton.inigua.databinding.ActivityRegisterBinding
import com.tri.sulton.inigua.helper.Constant
import com.tri.sulton.inigua.helper.fadeIn
import com.tri.sulton.inigua.view.ViewModelFactory
import com.tri.sulton.inigua.view.login.LoginActivity
import java.security.MessageDigest

class RegisterActivity : AppCompatActivity() {
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        setupObservation()
        playAnimation()
    }

    private fun setupView() {
        supportActionBar?.hide()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding.passwordEditText.textInputLayout = binding.passwordEditTextLayout
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (username.length < 6) {
                Toast.makeText(this, "Username must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 8) {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Password and confirm password must be same", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val encryptedPassword = Constant.encodePassword(password)

            val data = Register(username, encryptedPassword)
            viewModel.register(data)
        }

        binding.loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupObservation() {
        viewModel.registerResponse.observe(this) {
            showDialog(it)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun playAnimation() {
        val title = binding.titleTextView.fadeIn(750)
        val desc = binding.descTextView.fadeIn(750)
        val illustration = binding.imageView.fadeIn(750)
        val usernameLabel = binding.usernameTextView.fadeIn()
        val usernameEdit = binding.usernameEditTextLayout.fadeIn()
        val passwordLabel = binding.passwordTextView.fadeIn()
        val passwordEdit = binding.passwordEditTextLayout.fadeIn()
        val confirmPasswordLabel = binding.confirmPasswordTextView.fadeIn()
        val confirmPasswordEdit = binding.confirmPasswordEditTextLayout.fadeIn()
        val signup = binding.signupButton.fadeIn()
        val alreadyHaveAccountLabel = binding.alreadyHaveAccountTextView.fadeIn()
        val login = binding.loginTextView.fadeIn()

        val titleAndDesc = AnimatorSet().apply {
            playTogether(title, desc)
        }

        val field = AnimatorSet().apply {
            playTogether(usernameLabel, usernameEdit, passwordLabel, passwordEdit, confirmPasswordLabel, confirmPasswordEdit)
        }
        val loginAndHaveAccount = AnimatorSet().apply {
            playTogether(signup, alreadyHaveAccountLabel, login)
        }

        AnimatorSet().apply {
            playSequentially(titleAndDesc, illustration, field, loginAndHaveAccount)
            start()
        }
    }

    private fun showDialog(registerResponse: LoginResponse) {
        if (registerResponse.status == "error") {
            AlertDialog.Builder(this).apply {
                setTitle("Oops!")
                setMessage(registerResponse.message)
                setPositiveButton("OK") { _, _ -> }
                create()
                show()
            }
            return
        }
        Toast.makeText(this, registerResponse.message, Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}