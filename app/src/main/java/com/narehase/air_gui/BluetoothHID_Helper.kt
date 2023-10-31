package com.narehase.air_gui

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.bluetooth.BluetoothHidDeviceAppSdpSettings
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothProfile.ServiceListener
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import java.util.concurrent.Executors

class BluetoothHID_Helper {
    val TAG = "BluetoothHID_Helper"

    var _connected = false

    var isRegister = false

    lateinit var connectionStateChangeListener:connetionInterface
    val states = mutableStateOf("")
    var bluetoothProfile: BluetoothProfile? = null
    var mBtDevice: BluetoothDevice? = null
    var mBtHidDevice: BluetoothHidDevice? = null

    fun getHidDevice(): BluetoothHidDevice? {
        return mBtHidDevice
    }
    lateinit var bluetoothAdapter:BluetoothAdapter
    val mCallback = object : BluetoothHidDevice.Callback() {
        override fun onGetReport(
            device: BluetoothDevice,
            type: Byte,
            id: Byte,
            bufferSize: Int,
        ) {
            Log.v(
                TAG, "onGetReport: device=" + device + " type=" + type
                        + " id=" + id + " bufferSize=" + bufferSize
            )
        }

        @SuppressLint("MissingPermission")
        override fun onConnectionStateChanged(
            device: BluetoothDevice,
            state: Int,
        ) {
            when (state) {
                BluetoothProfile.STATE_DISCONNECTED -> {
                    states.value = "STATE_DISCONNECTED"
                    mBtDevice = null
                }
                BluetoothProfile.STATE_CONNECTING -> {
                    states.value = "STATE_CONNECTING"
                }
                BluetoothProfile.STATE_CONNECTED -> {
                    states.value = "STATE_CONNECTED"
                    mBtDevice = device
                }
                BluetoothProfile.STATE_DISCONNECTING -> {
                    states.value = "STATE_DISCONNECTING"
                }
            }
                Log.v(
                TAG,
                "onConnectionStateChanged: device=$device deviceName=${device.name} state=${states.value}"
            )
        }
    }
    val serviceListener = object : ServiceListener {
        @SuppressLint("NewApi", "MissingPermission")
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile) {
            if (profile == BluetoothProfile.HID_DEVICE) {
                Log.d(TAG, "Got HID device")
                Log.d(TAG, "Get HID device")
                bluetoothProfile = proxy


                if (profile == BluetoothProfile.HID_DEVICE) {
                    mBtHidDevice = proxy as BluetoothHidDevice
                    val sdp = BluetoothHidDeviceAppSdpSettings(
                        HidConsts.NAME,
                        HidConsts.DESCRIPTION,
                        HidConsts.PROVIDER,
                        BluetoothHidDevice.SUBCLASS1_COMBO,
                        HidConsts.Descriptor
                    )
                    mBtHidDevice!!.registerApp(
                        sdp,
                        null,
                        null,
                        Executors.newCachedThreadPool(),
                        mCallback
                    )
                }
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServiceDisconnected(profile: Int) {
            if (profile == BluetoothProfile.HID_DEVICE) {
                Log.d(TAG, "Lost HID device")
                mBtHidDevice?.unregisterApp()
            }
        }
    }
    @SuppressLint("MissingPermission")
    fun registerApp(context:Context) {
        if(!isRegister) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            bluetoothAdapter.name = "Air_Gui by Nare"
            bluetoothAdapter.getProfileProxy(
                context,
                serviceListener,
                BluetoothProfile.HID_DEVICE
            )

        }
    }



    interface connetionInterface {

        fun onConnecting()

        fun onConnected()

        fun onDisConnected()
        
    }
}