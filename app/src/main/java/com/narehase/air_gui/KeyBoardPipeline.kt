package com.narehase.air_gui

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHidDevice
import android.bluetooth.BluetoothProfile

class KeyBoardPipeline {
    val TAG = "KeyBoardPipeline"

    var _connected = false

    var isRegister = false

    lateinit var connectionStateChangeListener:connetionInterface

    var bluetoothProfile: BluetoothProfile? = null
    var mDevice: BluetoothDevice? = null
    var mHidDevice: BluetoothHidDevice? = null

    interface connetionInterface {

        fun onConnecting()

        fun onConnected()

        fun onDisConnected()
        
    }
}