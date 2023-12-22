package com.tri.sulton.inigua.view.login

import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.tri.sulton.inigua.data.api.model.Login
import com.tri.sulton.inigua.data.pref.UserModel
import com.tri.sulton.inigua.databinding.ActivityLoginBinding
import com.tri.sulton.inigua.helper.Constant
import com.tri.sulton.inigua.helper.fadeIn
import com.tri.sulton.inigua.view.ViewModelFactory
import com.tri.sulton.inigua.view.main.MainActivity
import com.tri.sulton.inigua.view.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        setupObservation()
        playAnimation()
    }

    private fun setupView() {
        supportActionBar?.hide()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val encryptedPassword = Constant.encodePassword(password)

            val data = Login(username, encryptedPassword)
            viewModel.login(data)
        }

        binding.registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupObservation() {
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.loginResponse.observe(this) { loginResponse ->
            if (loginResponse.status == "error") {
                AlertDialog.Builder(this).apply {
                    setTitle("Oops!")
                    setMessage(loginResponse.message)
                    setPositiveButton("OK") { _, _ -> }
                    create()
                    show()
                }
            } else {
                if (loginResponse != null) {
                    val user = UserModel(
                        token = loginResponse.data
                    )
                    viewModel.saveSession(user)
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }
    }

    private fun playAnimation() {
        val title = binding.titleTextView.fadeIn(750)
        val desc = binding.descTextView.fadeIn(750)
        val illustration = binding.imageView.fadeIn()
        val usernameLabel = binding.usernameTextView.fadeIn()
        val usernameEdit = binding.usernameEditTextLayout.fadeIn()
        val passwordLabel = binding.passwordTextView.fadeIn()
        val passwordEdit = binding.passwordEditTextLayout.fadeIn()
        val login = binding.loginButton.fadeIn()
        val haveNoAccountLabel = binding.haveNoAccountTextView.fadeIn()
        val register = binding.registerTextView.fadeIn()

        val titleAndDesc = AnimatorSet().apply {
            playTogether(title, desc)
        }

        val field = AnimatorSet().apply {
            playTogether(usernameLabel, usernameEdit, passwordLabel, passwordEdit)
        }
        val loginAndNoAccount = AnimatorSet().apply {
            playTogether(login, haveNoAccountLabel, register)
        }

        AnimatorSet().apply {
            playSequentially(titleAndDesc, illustration, field, loginAndNoAccount)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}