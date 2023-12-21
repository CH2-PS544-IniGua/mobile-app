package com.tri.sulton.inigua.view.register

import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tri.sulton.inigua.data.api.model.Register
import com.tri.sulton.inigua.data.api.model.response.CommonResponse
import com.tri.sulton.inigua.databinding.ActivityRegisterBinding
import com.tri.sulton.inigua.helper.fadeIn
import com.tri.sulton.inigua.view.ViewModelFactory
import com.tri.sulton.inigua.view.login.LoginActivity

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
        binding.emailEditText.textInputLayout = binding.emailEditTextLayout
        binding.passwordEditText.textInputLayout = binding.passwordEditTextLayout
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val username = binding.nameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            val data = Register(username, password)
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
        val illustration = binding.imageView.fadeIn(750)
        val nameLabel = binding.nameTextView.fadeIn()
        val nameEdit = binding.nameEditTextLayout.fadeIn()
        val emailLabel = binding.emailTextView.fadeIn()
        val emailEdit = binding.emailEditTextLayout.fadeIn()
        val passwordLabel = binding.passwordTextView.fadeIn()
        val passwordEdit = binding.passwordEditTextLayout.fadeIn()
        val signup = binding.signupButton.fadeIn()
        val alreadyHaveAccountLabel = binding.alreadyHaveAccountTextView.fadeIn()
        val login = binding.loginTextView.fadeIn()

        val field = AnimatorSet().apply {
            playTogether(nameLabel, nameEdit, emailLabel, emailEdit, passwordLabel, passwordEdit)
        }
        val loginAndHaveAccount = AnimatorSet().apply {
            playTogether(signup, alreadyHaveAccountLabel, login)
        }

        AnimatorSet().apply {
            playSequentially(illustration, field, loginAndHaveAccount)
            start()
        }
    }

    private fun showDialog(registerResponse: CommonResponse<String>) {
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