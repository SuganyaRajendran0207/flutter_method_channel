package com.example.flutter_method_channel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity: FlutterActivity() {
    private val CHANNEL = "samples.flutter.dev/battery"
    private val MESSAGE_CHANNEL = "samples.flutter.dev/message"
    private val DEVICE_CHANNEL = "samples.flutter.dev/device_info"

    companion object {
        @JvmStatic
        var msgresult: MethodChannel.Result? = null
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
                call, result ->
            if (call.method == "getBatteryLevel") {
                val batteryLevel = getBatteryLevel()

                if (batteryLevel != -1) {
                    result.success(batteryLevel)
                } else {
                    result.error("UNAVAILABLE", "Battery level not available.", null)
                }
            } else {
                result.notImplemented()
            }
        }

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, DEVICE_CHANNEL).setMethodCallHandler {
                call, result ->
            if (call.method == "getDeviceId") {
                val imei = getIMEIDeviceId(context)
                if(imei?.isNotEmpty() == true) {
                    result.success(imei)
                }else{
                    result.error("UNAVAILABLE", "Can't finf device info", null)
                }
            } else {
                result.notImplemented()
            }
        }

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, MESSAGE_CHANNEL).setMethodCallHandler {
                call, result ->
            if (call.method == "getMessageFromNative") {
                msgresult = result
                Log.e("TAG", "Result -> ${call.argument<String>("message")}")
                getMessageFromNative(call.argument<String>("message"))
            } else {
                result.notImplemented()
            }
        }
    }

    private fun getBatteryLevel(): Int {
        val batteryLevel: Int
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(
                Intent.ACTION_BATTERY_CHANGED)
            )
            batteryLevel = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(
                BatteryManager.EXTRA_SCALE, -1)
        }

        return batteryLevel
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceId(): String {
        val telephonyMgr: TelephonyManager = (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)

        val  imei: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            telephonyMgr.imei
        } else {
            telephonyMgr.deviceId
        }
        return imei
    }

    @SuppressLint("HardwareIds")
    fun getIMEIDeviceId(context: Context): String? {
        val deviceId: String
        deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } else {
            val mTelephony = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return ""
                }
            }
            if (mTelephony.deviceId != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mTelephony.imei
                } else {
                    mTelephony.deviceId
                }
            } else {
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            }
        }
        Log.d("deviceId", deviceId)
        return deviceId
    }
    private fun getMessageFromNative(preMsg: String?) {
        startActivity(Intent(applicationContext, MessageActivity::class.java).putExtra("result", preMsg))
    }
}
