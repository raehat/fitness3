package com.example.solanatry2.login

import DataStoreManager
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender
import com.solana.mobilewalletadapter.clientlib.MobileWalletAdapter
import com.solana.mobilewalletadapter.clientlib.TransactionResult

class LoginViewModel : ViewModel() {
    val toastMessage = MutableLiveData<String>()
    val noWalletFound = MutableLiveData<Boolean>()
    val moveToAnotherFragment = MutableLiveData<Boolean>()

    suspend fun connectToWallet(
        activityResultSender: ActivityResultSender,
        identityUri: Uri,
        iconUri: Uri,
        identityName: String,
        dataStoreManager: DataStoreManager
    ) {
        if (!dataStoreManager.isWalletStateEmpty()) {
            showToast("not empty!")
            return
        }

        val walletAdapterClient = MobileWalletAdapter()
        val result = walletAdapterClient.transact(activityResultSender) {
            authorize(
                identityUri = identityUri,
                iconUri = iconUri,
                identityName = identityName
            )
        }

        when(result) {
            is TransactionResult.Failure -> showToast("Cannot connect to wallet")
            is TransactionResult.NoWalletFound -> alertNoWalletFound()
            is TransactionResult.Success -> {
                dataStoreManager.saveWalletState(result.payload)
                moveToAnotherFragment.postValue(true)
            }
        }
    }

    private fun alertNoWalletFound() {
        noWalletFound.value = true
    }

    private fun showToast(message : String) {
        toastMessage.postValue(message)
    }

}