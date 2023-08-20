package com.example.solanatry2.login

import DataStoreManager
import UserData
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.solana.mobilewalletadapter.clientlib.ActivityResultSender
import com.solana.mobilewalletadapter.clientlib.MobileWalletAdapter
import com.solana.mobilewalletadapter.clientlib.TransactionResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    fun saveLoginDataAndMoveToHomePage(
        gender: String,
        dob: String,
        weight: String,
        height: String,
        dataStoreManager: DataStoreManager
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            dataStoreManager.setUserData(
                UserData(gender, dob, weight, height)
            )
            dataStoreManager.userDataSaved(true)
        }
    }

}