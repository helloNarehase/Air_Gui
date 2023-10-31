package com.narehase.air_gui


class HidReport(var deviceType: DeviceType, var ReportId: Byte, var ReportData: ByteArray) {
    enum class DeviceType {
        None, Mouse, Keyboard
    }

    enum class State {
        None, Sending, Sended, Failded
    }

    companion object {
        @JvmField
        var SendState = State.None
    }
}