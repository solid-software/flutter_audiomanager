package software.solid.flutter_audiomanager;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import io.flutter.plugin.common.EventChannel.EventSink;
import io.flutter.plugin.common.EventChannel.StreamHandler;

import static io.flutter.plugin.common.PluginRegistry.Registrar;

public class HeadphonesBluetoothConnectionReceiver implements StreamHandler {
    private final Registrar registrar;
    private BroadcastReceiver receiver;
    private HeadphonesDetector detector;

    HeadphonesBluetoothConnectionReceiver(Registrar registrar) {
        this.registrar = registrar;
        detector = new HeadphonesDetector(registrar);
    }

    @Override
    public void onListen(Object arguments, EventSink eventSink) {
        receiver = createBluetoothReceiver(eventSink);
        registrar.context().registerReceiver(receiver,
                new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED));
        eventSink.success(detector.bluetoothHeadphonesConnectionState().ordinal());
    }

    @Override
    public void onCancel(Object o) {
        registrar.context().unregisterReceiver(receiver);
        receiver = null;
    }

    private BroadcastReceiver createBluetoothReceiver(final EventSink eventSink) {
        return new BroadcastReceiver() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                HeadphonesConnectionState event = HeadphonesConnectionState.DISCONNECTED;
                if (action != null && action.equals(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)) {
                    BluetoothManager bluetoothManager =
                            (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
                    BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

                    final int state = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
                    switch (state) {
                        case BluetoothAdapter.STATE_DISCONNECTED:
                            event = HeadphonesConnectionState.DISCONNECTED;
                            break;
                        case BluetoothAdapter.STATE_DISCONNECTING:
                            event = HeadphonesConnectionState.DISCONNECTING;
                            break;
                        case BluetoothAdapter.STATE_CONNECTED:
                            event = HeadphonesConnectionState.CONNECTED;
                            break;
                        case BluetoothAdapter.STATE_CONNECTING:
                            event = HeadphonesConnectionState.CONNECTING;
                            break;
                    }
                }
                eventSink.success(event.ordinal());
            }
        };
    }
}
