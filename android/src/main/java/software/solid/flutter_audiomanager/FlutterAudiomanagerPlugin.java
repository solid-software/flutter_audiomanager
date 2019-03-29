package software.solid.flutter_audiomanager;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterAudiomanagerPlugin
 */
public class FlutterAudiomanagerPlugin {
    private static final String HEADPHONES_FINDER = "com.example.flutter_headphones/finder";
    private static final String HEADPHONES_RECEIVER = "com.example.flutter_headphones/receiver";
    private static final String BLUETOOTH_RECEIVER = "com.example.flutter_headphones/bluetooth_receiver";

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final HeadphonesDetector finder = new HeadphonesDetector(registrar);
        final MethodChannel finderChannel = new MethodChannel(registrar.messenger(),
                HEADPHONES_FINDER);
        finderChannel.setMethodCallHandler(finder);

        final HeadphonesConnectionReceiver receiver = new HeadphonesConnectionReceiver(registrar);
        final EventChannel receiveHeadphonesChannel = new EventChannel(registrar.messenger(),
                HEADPHONES_RECEIVER);
        receiveHeadphonesChannel.setStreamHandler(receiver);

        final HeadphonesBluetoothConnectionReceiver bluetoothReceiver = new HeadphonesBluetoothConnectionReceiver(registrar);
        final EventChannel receiveBluetoothChannel = new EventChannel(registrar.messenger(), BLUETOOTH_RECEIVER);
        receiveBluetoothChannel.setStreamHandler(bluetoothReceiver);
    }
}
