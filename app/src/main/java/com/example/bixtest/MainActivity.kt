package com.example.bixtest

import android.hardware.usb.UsbDevice
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.bixolon.labelprinter.BixolonLabelPrinter
import com.example.bixtest.ui.theme.BixTestTheme
import kotlin.coroutines.resume

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadBixolonLibrary()
        setContent {
            BixTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    private fun loadBixolonLibrary() {
        try {
            System.loadLibrary("bxl_common")
        } catch (e: Exception) {
            print(e.message)
        }
    }
}

@Composable
fun Greeting(name: String) {
    val context = LocalContext.current
    val mutablePrinter = remember {
        var printer: BixolonLabelPrinter? = null
        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    BixolonLabelPrinter.MESSAGE_USB_DEVICE_SET -> {
                        val printers = msg.obj as Set<UsbDevice>
                        val firstFoundPrinter = printers.first()
                        printer?.connect(firstFoundPrinter, firstFoundPrinter.deviceName)
                    }
                    BixolonLabelPrinter.MESSAGE_STATE_CHANGE -> {
                        when (msg.arg1) {
                            BixolonLabelPrinter.STATE_CONNECTED -> print("Connected")
                            BixolonLabelPrinter.STATE_CONNECTING -> print("Connecting")
                            BixolonLabelPrinter.STATE_NONE -> print("None")
                        }
                    }
                }
            }
        }
        printer = BixolonLabelPrinter(
            context,
            handler,
            null
        )
        printer
    }


    mutablePrinter.findUsbPrinters()


    Text(text = "Hello $name!")
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BixTestTheme {
        Greeting("Android")
    }
}