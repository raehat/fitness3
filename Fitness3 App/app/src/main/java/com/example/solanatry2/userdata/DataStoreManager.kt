import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.solana.mobilewalletadapter.clientlib.protocol.MobileWalletAdapterClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "USER_DATASTORE")
    companion object {
        val PUBLIC_KEY = stringPreferencesKey("PUBLIC_KEY")
        val ACCOUNT_LABEL = stringPreferencesKey("ACCOUNT_LABEL")
        val AUTH_TOKEN = stringPreferencesKey("AUTH_TOKEN")
        val ON_BOARDING = booleanPreferencesKey("ON_BOARDING")
        val IS_WALLET_EMPTY = booleanPreferencesKey("IS_WALLET_EMPTY")

        val NAME = stringPreferencesKey("NAME")
        val DOB = stringPreferencesKey("DOB")
        val WEIGHT = stringPreferencesKey("WEIGHT")
        val HEIGHT = stringPreferencesKey("HEIGHT")

        val USER_DATA_SAVED = booleanPreferencesKey("USER_DATA_SAVED")
        val STEPS_COMPLETED = intPreferencesKey("STEPS_COMPLETED")
    }
    suspend fun saveWalletState(payload: MobileWalletAdapterClient.AuthorizationResult) {
        context.dataStore.edit {
            it[PUBLIC_KEY] = payload.publicKey.toString()
            it[ACCOUNT_LABEL] = payload.accountLabel ?: ""
            it[AUTH_TOKEN] = payload.authToken
            it[IS_WALLET_EMPTY] = false
        }
    }

    suspend fun setOnBoardingFinished(value: Boolean) {
        context.dataStore.edit {
            it[ON_BOARDING] = value
        }
    }

    fun isOnBoardingFinished() : Flow<Boolean?> =
        context.dataStore.data.map {
            it[ON_BOARDING]
        }

    fun isWalletStateEmpty() = context.dataStore.data.map {
        it[IS_WALLET_EMPTY] ?: true
    }

    fun getWalletState(): WalletState? {
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

    suspend fun setUserData(userData: UserData) {
        context.dataStore.edit {
            it[NAME] = userData.name
            it[DOB] = userData.dob
            it[WEIGHT] = userData.weight
            it[HEIGHT] = userData.height
        }
    }

    fun getPublicKey() = context.dataStore.data.map { it[PUBLIC_KEY] }

    fun getName() = context.dataStore.data.map { it[NAME] }
    fun getDOB() = context.dataStore.data.map { it[DOB] }
    fun getWeight() = context.dataStore.data.map { it[WEIGHT] }
    fun getHeight() = context.dataStore.data.map { it[HEIGHT] }

    suspend fun userDataSaved(value: Boolean) {
        context.dataStore.edit {
            it[USER_DATA_SAVED] = value
        }
    }

    fun isUserDataSaved() = context.dataStore.data.map { it[USER_DATA_SAVED] }

    suspend fun setStepsCompleted(value: Int) {
        context.dataStore.edit {
            it[STEPS_COMPLETED] = value
        }
    }

    fun getStepsCompleted() = context.dataStore.data.map { it[STEPS_COMPLETED] }
}

data class WalletState(
    val pubKey: String,
    val accountLabel: String,
    val authToken: String
)

data class UserData(
    val name: String,
    val dob: String,
    val weight: String,
    val height: String
)