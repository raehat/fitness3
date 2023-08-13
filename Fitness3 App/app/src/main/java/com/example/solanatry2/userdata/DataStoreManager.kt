import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.solana.mobilewalletadapter.clientlib.protocol.MobileWalletAdapterClient
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "USER_DATASTORE")
    companion object {

        val PUBLIC_KEY = stringPreferencesKey("PUBLIC_KEY")
        val ACCOUNT_LABEL = stringPreferencesKey("ACCOUNT_LABEL")
        val AUTH_TOKEN = stringPreferencesKey("AUTH_TOKEN")

    }

    suspend fun saveWalletState(payload: MobileWalletAdapterClient.AuthorizationResult) {
        context.dataStore.edit {
            it[PUBLIC_KEY] = payload.publicKey.toString()
            it[ACCOUNT_LABEL] = payload.accountLabel ?: ""
            it[AUTH_TOKEN] = payload.authToken
        }
    }

    suspend fun isWalletStateEmpty() : Boolean {
        var pubKey : String = ""
        context.dataStore.data.map {
            pubKey = it[PUBLIC_KEY] ?: ""
        }
        return pubKey.isEmpty()
    }

    suspend fun getWalletState(): WalletState? {
        var walletState : WalletState? = null
        context.dataStore.data.map {
            walletState = WalletState(
                pubKey = it[PUBLIC_KEY]?:"",
                accountLabel = it[ACCOUNT_LABEL]?:"",
                authToken = it[AUTH_TOKEN]?:""
            )
        }
        return walletState
    }

}

data class WalletState(
    val pubKey: String,
    val accountLabel: String,
    val authToken: String
)