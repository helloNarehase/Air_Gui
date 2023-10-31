package com.narehase.air_gui

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.narehase.air_gui.ui.theme.Air_GuiTheme


class MainActivity : ComponentActivity(), BluetoothHID_Helper.connetionInterface {

    val names: MutableState<ArrayList<String>> = mutableStateOf( arrayListOf())
    val mDevices: MutableState<ArrayList<BluetoothDevice?>> = mutableStateOf( arrayListOf())
    lateinit var bluetoothHID_Helper:BluetoothHID_Helper
    private fun btListDevices() {
        if ((ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED)
            || (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.BLUETOOTH,
                    android.Manifest.permission.BLUETOOTH_SCAN,
                    android.Manifest.permission.BLUETOOTH_ADVERTISE,
                    android.Manifest.permission.BLUETOOTH_CONNECT
                ),
                1
            )
            return
        }
        names.value.clear()
        mDevices.value.clear()
    }

    var discoverPermission = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        Log.d("TAG", ": ${it.resultCode}")
        if (it.resultCode == 120) {
//            start()
        }
    }
    var bluetoothPermission = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
//            if (isEnableBluetooth()) {
//                showToast(R.string.toast_bluetooth_on)
//            } else {
//                showToast(R.string.toast_bluetooth_off)
//            }
        }
    }
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            bluetoothHID_Helper = BluetoothHID_Helper()
            bluetoothHID_Helper.registerApp(this)

            Air_GuiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ){
                        Row {

                            Button(onClick = {
                                bluetoothPermission.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                                discoverPermission.launch(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE))

                            }) {
                                Icon(Icons.Default.Search, contentDescription = Icons.Default.Search.toString())
                            }
                            Button(onClick = {

                                var ModifierByte: Byte = 0x00
                                var KeyByte: Byte = 0x00

                                val a = bluetoothHID_Helper.mBtHidDevice!!.sendReport(bluetoothHID_Helper.mBtDevice,
                                    0x02.toByte().toInt(), byteArrayOf(0x00, 36.toByte()))
                                bluetoothHID_Helper.mBtHidDevice!!.sendReport(bluetoothHID_Helper.mBtDevice,
                                    0x02.toByte().toInt(), byteArrayOf(0x00, 0.toByte()))
                                Log.e("Gahi", bluetoothHID_Helper.mBtDevice!!.name)

                                Log.e("AFAF", "$a")
                            }) {
                                Icon(Icons.Default.Create, contentDescription = Icons.Default.Create.toString())
                            }
                        }

                    }
                }
            }
        }
    }

    override fun onConnecting() {
        TODO("Not yet implemented")
    }

    override fun onConnected() {
        TODO("Not yet implemented")
    }

    override fun onDisConnected() {
        TODO("Not yet implemented")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Air_GuiTheme {
        Greeting("Android")
    }
}