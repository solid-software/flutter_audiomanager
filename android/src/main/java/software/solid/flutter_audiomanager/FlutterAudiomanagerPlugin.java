package software.solid.flutter_audiomanager;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterAudiomanagerPlugin
 */
public class FlutterAudiomanagerPlugin {
    private static final String DETECT_STATE = "com.example.flutter_headphones/detect_state";
    private static final String WIRED_STATE_EVENTS = "com.example.flutter_headphones/wired_state_events";
    private static final String BLUETOOTH_STATE_EVENTS = "com.example.flutter_headphones/bluetooth_state_events";

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final HeadphonesDetector detector = new HeadphonesDetector(registrar);
        final MethodChannel detectorChannel = new MethodChannel(registrar.messenger(),
                DETECT_STATE);
        detectorChannel.setMethodCallHandler(detector);

        final HeadphonesWiredConnectionReceiver wiredConnectionReceiver =
                new HeadphonesWiredConnectionReceiver(registrar);
        final EventChannel receiveHeadphonesChannel = new EventChannel(registrar.messenger(),
                WIRED_STATE_EVENTS);
        receiveHeadphonesChannel.setStreamHandler(wiredConnectionReceiver);

        final HeadphonesBluetoothConnectionReceiver bluetoothConnectionReceiver =
                new HeadphonesBluetoothConnectionReceiver(registrar);
        final EventChannel receiveBluetoothChannel = new EventChannel(registrar.messenger(),
                BLUETOOTH_STATE_EVENTS);
        receiveBluetoothChannel.setStreamHandler(bluetoothConnectionReceiver);
    }
}
