package ua.org.meters.lighthouse.mobile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.org.meters.lighthouse.mobile.ui.theme.LighthouseTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentPower = intent.getBooleanExtra("power", false)
        val powerStatus: Flow<Boolean> = this.dataStore.data.map {
            preferences -> preferences[POWER_KEY] ?: intentPower
        }


        enableEdgeToEdge()
        setContent {
            LighthouseTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val powerState = powerStatus.collectAsState(initial = intentPower)
                    AppScreen(
                        powerOn = powerState.value,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
        askNotificationPermission()
        logRegistrationToken()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(baseContext, R.string.will_show_notifications, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(baseContext, R.string.will_not_show_notifications, Toast.LENGTH_SHORT).show()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "Notification permission already granted")
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Toast.makeText(baseContext, R.string.notifications_needed, Toast.LENGTH_SHORT).show()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun logRegistrationToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Cannot get registration token", task.exception)
                return@OnCompleteListener
            }

            Log.i(TAG, "Got FCM token: " + task.result)
        })
    }

    companion object {
        private const val TAG = "MainActivity";
    }
}

@Composable
fun AppScreen(
    powerOn: Boolean,
    modifier: Modifier = Modifier
) {
    val imageRes = if (powerOn) R.drawable.lighthouse_on else R.drawable.lighthouse_off
    val stringRes = if (powerOn) R.string.power_on else R.string.power_off

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.screen_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Image(
            painter = painterResource(imageRes),
            contentDescription = stringResource(stringRes)
        )
        Text(
            text = stringResource(stringRes)
        )
    }
}

@Preview(
    showBackground = true,
    name = "App screen - power on"
)
@Composable
fun AppScreenPreview() {
    LighthouseTheme {
        AppScreen(powerOn = true)
    }
}

@Preview(
    showBackground = true,
    name = "App screen - power off"
)
@Composable
fun AppScreenPowerOffPreview() {
    LighthouseTheme {
        AppScreen(powerOn = false)
    }
}
