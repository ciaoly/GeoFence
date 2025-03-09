package top.cha01.geofence

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.IBinder
import android.text.SpannableStringBuilder
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.app.PendingIntentCompat.getActivity
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.util.XonXoffFilter
import de.kai_morich.simple_usb_terminal.SerialListener
import de.kai_morich.simple_usb_terminal.TextUtil
import top.cha01.geofence.data.database.AppDatabase
import top.cha01.geofence.libs.UsbSerial.DeviceEndpoints
import top.cha01.geofence.services.SerialService
import top.cha01.geofence.ui.router.Screen
import top.cha01.geofence.ui.router.appNavigation
import top.cha01.geofence.ui.router.navigationList
import top.cha01.geofence.ui.theme.GeoFenceTheme
import top.cha01.geofence.ui.viewmodels.MainActivityViewModel
import java.util.ArrayDeque

class MainActivity : ComponentActivity(), ServiceConnection, SerialListener {
    private enum class Connected {
        False, Pending, True
    }

    private lateinit var viewModel: MainActivityViewModel

    private val flowControlFilter: XonXoffFilter? = null

    init {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (Constants.INTENT_ACTION_GRANT_USB == intent.action) {
                    val granted = intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)
                    connect(granted)
                }
            }
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as SerialService.LocalBinder
            viewModel.service = binder.getService()
            viewModel.service?.attach(this@MyActivity)
            bound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            viewModel.service = null
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app-db").build()

        setContent {

            GeoFenceTheme {
                val navController = rememberNavController()
                // 获取当前选中的路由
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val windowSize = with(LocalDensity.current) {
                    currentWindowSize().toSize().toDpSize()
                }
                val layoutType = if (windowSize.width >= 1200.dp) {
                    NavigationSuiteType.NavigationDrawer
                } else {
                    NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
                        currentWindowAdaptiveInfo()
                    )
                }

                NavigationSuiteScaffold(
                    layoutType = layoutType,
                    navigationSuiteItems = {
                        navigationList.forEach {
                            item(
                                icon = { Icon(it.icon, contentDescription = null) },
                                label = { Text(it.title) },
                                selected = currentRoute == it.screen.route, // 根据当前路由判断是否选中
                                onClick = {
                                    navController.navigate(it.screen.route) {
                                        // 避免重复导航到同一个目的地
                                        launchSingleTop = true
                                    } },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                    }
                ) {
                    NavHost(navController, startDestination = Screen.RcConfig.route) {
                        appNavigation(db, endpoints)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        if (connected != Connected.False) disconnect()
        getActivity(this).stopService(Intent(getActivity(), SerialService::class.java))
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        if (service != null) service!!.attach(this)
        else getActivity().startService(
            Intent(
                getActivity(),
                SerialService::class.java
            )
        ) // prevents service destroy on unbind from recreated activity caused by orientation change

        ContextCompat.registerReceiver(
            getActivity(),
            broadcastReceiver,
            IntentFilter(Constants.INTENT_ACTION_GRANT_USB),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    public override fun onStop() {
        getActivity().unregisterReceiver(broadcastReceiver)
        if (service != null && !getActivity().isChangingConfigurations()) service.detach()
        super.onStop()
    }

    @Suppress("deprecation") // onAttach(context) was added with API 23. onAttach(activity) works for all API versions
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        getActivity().bindService(
            Intent(getActivity(), SerialService::class.java),
            this, BIND_AUTO_CREATE
        )
    }

    override fun onDetach() {
        try {
            getActivity().unbindService(this)
        } catch (ignored: Exception) {
        }
        super.onDetach()
    }

    public override fun onResume() {
        super.onResume()
        if (initialStart && service != null) {
            initialStart = false
            getActivity().runOnUiThread(this::connect)
        }
        if (connected == Connected.True) controlLines.start()
    }

    public override fun onPause() {
        controlLines.stop()
        super.onPause()
    }

    override fun onServiceConnected(name: ComponentName?, binder: IBinder) {
        service = (binder as SerialBinder).getService()
        service.attach(this)
        if (initialStart && isResumed()) {
            initialStart = false
            getActivity().runOnUiThread(this::connect)
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        service = null
    }


    /*
     * Serial + UI
     */
    private fun connect() {
        connect(null);
    }

    private fun connect(permissionGranted: Boolean) {
        UsbDevice device = null;
        UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        for(UsbDevice v : usbManager.getDeviceList().values())
            if(v.getDeviceId() == deviceId)
                device = v;
        if(device == null) {
            status("connection failed: device not found");
            return;
        }
        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        if(driver == null) {
            driver = CustomProber.getCustomProber().probeDevice(device);
        }
        if(driver == null) {
            status("connection failed: no driver for device");
            return;
        }
        if(driver.getPorts().size() < portNum) {
            status("connection failed: not enough ports at device");
            return;
        }
        usbSerialPort = driver.getPorts().get(portNum);
        UsbDeviceConnection usbConnection = usbManager.openDevice(driver.getDevice());
        if(usbConnection == null && permissionGranted == null && !usbManager.hasPermission(driver.getDevice())) {
            int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_MUTABLE : 0;
            Intent intent = new Intent(Constants.INTENT_ACTION_GRANT_USB);
            intent.setPackage(getActivity().getPackageName());
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, flags);
            usbManager.requestPermission(driver.getDevice(), usbPermissionIntent);
            return;
        }
        if(usbConnection == null) {
            if (!usbManager.hasPermission(driver.getDevice()))
                status("connection failed: permission denied");
            else
                status("connection failed: open failed");
            return;
        }

        connected = Connected.Pending;
        try {
            usbSerialPort.open(usbConnection);
            try {
                usbSerialPort.setParameters(baudRate, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            } catch (UnsupportedOperationException e) {
                status("Setting serial parameters failed: " + e.getMessage());
            }
            SerialSocket socket = new SerialSocket(getActivity().getApplicationContext(), usbConnection, usbSerialPort);
            service.connect(socket);
            // usb connect is not asynchronous. connect-success and connect-error are returned immediately from socket.connect
            // for consistency to bluetooth/bluetooth-LE app use same SerialListener and SerialService classes
            onSerialConnect();
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

    private fun disconnect() {
        connected = Connected.False;
        controlLines.stop();
        service.disconnect();
        updateSendBtn(SendButtonState.Idle);
        usbSerialPort = null;
    }

    private fun send(str: String): Unit {
        if(connected != Connected.True) {
            Toast.makeText(getActivity(), "not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        String msg;
        byte[] data;
        if(hexEnabled) {
            StringBuilder sb = new StringBuilder();
            TextUtil.toHexString(sb, TextUtil.fromHexString(str));
            TextUtil.toHexString(sb, newline.getBytes());
            msg = sb.toString();
            data = TextUtil.fromHexString(msg);
        } else {
            msg = str;
            data = (str + newline).getBytes();
        }
        try {
            SpannableStringBuilder spn = new SpannableStringBuilder(msg + '\n');
            spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorSendText)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            receiveText.append(spn);
            service.write(data);
        } catch (SerialTimeoutException e) { // e.g. writing large data at low baud rate or suspended by flow control
            mainLooper.post(() -> sendAgain(data, e.bytesTransferred));
        } catch (Exception e) {
            onSerialIoError(e);
        }
    }

    private fun receive(datas: ArrayDeque<ByteArray>): Unit {
        SpannableStringBuilder spn = new SpannableStringBuilder();
        for (byte[] data : datas) {
            if (flowControlFilter != null)
                data = flowControlFilter.filter(data);
            if (hexEnabled) {
                spn.append(TextUtil.toHexString(data)).append('\n');
            } else {
                String msg = new String(data);
                if (newline.equals(TextUtil.newline_crlf) && msg.length() > 0) {
                    // don't show CR as ^M if directly before LF
                    msg = msg.replace(TextUtil.newline_crlf, TextUtil.newline_lf);
                    // special handling if CR and LF come in separate fragments
                    if (pendingNewline && msg.charAt(0) == '\n') {
                        if(spn.length() >= 2) {
                            spn.delete(spn.length() - 2, spn.length());
                        } else {
                            Editable edt = receiveText.getEditableText();
                            if (edt != null && edt.length() >= 2)
                                edt.delete(edt.length() - 2, edt.length());
                        }
                    }
                    pendingNewline = msg.charAt(msg.length() - 1) == '\r';
                }
                spn.append(TextUtil.toCaretString(msg, newline.length() != 0));
            }
        }
        receiveText.append(spn);
    }

    fun status(str: String): Unit {
        val spn: SpannableStringBuilder = SpannableStringBuilder(str + '\n');
        spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorStatusText)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        receiveText.append(spn);
    }

    /*
     * starting with Android 14, notifications are not shown in notification bar by default when App is in background
     */

    private void showNotificationSettings() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("android.provider.extra.APP_PACKAGE", getActivity().getPackageName());
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(Arrays.equals(permissions, new String[]{Manifest.permission.POST_NOTIFICATIONS}) &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !service.areNotificationsEnabled())
            showNotificationSettings();
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        TODO("Not yet implemented")
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        TODO("Not yet implemented")
    }

    override fun onSerialConnect() {
        status("connected");
        connected = Connected.True;
        controlLines.start();
    }

    override fun onSerialConnectError(e: Exception?) {
        status("connection failed: " + e.getMessage());
        disconnect();
    }

    override fun onSerialRead(data: ByteArray?) {
        ArrayDeque<byte[]> datas = new ArrayDeque<>();
        datas.add(data);
        receive(datas);
    }

    override fun onSerialRead(datas: ArrayDeque<ByteArray>?) {
        receive(datas);
    }

    override fun onSerialIoError(e: Exception?) {
        status("connection lost: " + e.getMessage())
        disconnect();
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GeoFenceTheme {
    }
}