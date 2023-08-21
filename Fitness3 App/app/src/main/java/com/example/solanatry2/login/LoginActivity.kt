package com.example.solanatry2.login

import DataStoreManager
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.solanatry2.MyApplication
import com.example.solanatry2.R
import com.example.solanatry2.databinding.ActivityLoginBinding
import com.example.solanatry2.home.MainActivity
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataStoreManager = (applicationContext as MyApplication).dataStoreManager
        val activityResultSender = ActivityResultSender(this)

        CoroutineScope(Dispatchers.Main).launch {
            (applicationContext as MyApplication).dataStoreManager.setOnBoardingFinished(true)
        }

        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        viewModel.moveToMainActivity.observe(this) {
            if (it) {
                startMainActivity()
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            if (!dataStoreManager.isWalletStateEmpty().first()) {
                startMainActivity()
            }
        }

        viewModel.toastMessage.observe(this) { message ->
            run {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.moveToAnotherFragment.observe(this) {moveToAnotherFragment ->
            if (moveToAnotherFragment) {
                run {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.login_frame_layout, LoginFragment2())
                        .commit()
                }
            } else {
                run {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.login_frame_layout, LoginFragment1(activityResultSender))
                        .commit()
                }
            }
        }

        viewModel.noWalletFound.observe(this) { value ->
            if (value) {
                run {
                    AlertDialog.Builder(this)
                        .setTitle("No wallet found")
                        .setMessage("You do not have any Solana compatible wallet installed. Please download wallet to continue.")
                        .setPositiveButton("Download") { dialog, _ ->
                            val solflare_url = "https://play.google.com/store/apps/details?id=com.solflare.mobile&hl=en&gl=US"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(solflare_url))
                            startActivity(intent)
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
    }

    fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}