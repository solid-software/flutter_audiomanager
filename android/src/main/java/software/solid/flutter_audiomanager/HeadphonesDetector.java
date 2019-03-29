package software.solid.flutter_audiomanager;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;

import static io.flutter.plugin.common.PluginRegistry.Registrar;

public class HeadphonesDetector implements MethodCallHandler {
    // For wired headphones
    private static final int WIRED_HEADPHONES = 0;

    // For bluetooth headphones
    private static final int BLUETOOTH_HEADPHONES = 1;

    // One of the types
    private static final int ANY = 2;

    private final Registrar registrar;

    HeadphonesDetector(Registrar registrar) {
        this.registrar = registrar;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private HeadphonesConnectionState isWiredHeadphonesPlugged() {
        AudioManager audioManager =
                (AudioManager) registrar.context().getSystemService(Context.AUDIO_SERVICE);

        AudioDeviceInfo[] audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_ALL);

        for (AudioDeviceInfo deviceInfo : audioDevices) {
            int deviceType = deviceInfo.getType();
            if (deviceType == AudioDeviceInfo.TYPE_WIRED_HEADPHONES
                    || deviceType == AudioDeviceInfo.TYPE_WIRED_HEADSET) {
                return HeadphonesConnectionState.CONNECTED;
            }
        }

        return HeadphonesConnectionState.DISCONNECTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private HeadphonesConnectionState isBluetoothHeadphonesPlugged() {
        BluetoothManager bluetoothManager =
                (BluetoothManager) registrar.context().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        final int state = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);

        HeadphonesConnectionState result;
        switch (state) {
            case BluetoothAdapter.STATE_CONNECTED:
                result = HeadphonesConnectionState.CONNECTED;
                break;
            case BluetoothAdapter.STATE_CONNECTING:
                result = HeadphonesConnectionState.CONNECTING;
                break;
            case BluetoothAdapter.STATE_DISCONNECTED:
                result = HeadphonesConnectionState.DISCONNECTED;
                break;
            case BluetoothAdapter.STATE_DISCONNECTING:
                result = HeadphonesConnectionState.DISCONNECTING;
                break;
            default:
                result = HeadphonesConnectionState.DISCONNECTED;
        }

        return result;
    }

    @Override
    public void onMethodCall(MethodCall call, MethodChannel.Result result) {
        if (call.method.equals("isHeadphonesConnected")) {
            Integer type = call.argument("headphonesType");
            if (type != null) {
                switch (type) {
                    case WIRED_HEADPHONES:
                        result.success(isWiredHeadphonesPlugged().ordinal());
                        break;
                    case BLUETOOTH_HEADPHONES:
                        result.success(isBluetoothHeadphonesPlugged().ordinal());
                        break;
                    case ANY:
                        if (isWiredHeadphonesPlugged() == HeadphonesConnectionState.CONNECTED
                                || isBluetoothHeadphonesPlugged() == HeadphonesConnectionState.CONNECTED) {
                            result.success(HeadphonesConnectionState.CONNECTED.ordinal());
                        } else {
                            result.success(HeadphonesConnectionState.DISCONNECTED.ordinal());
                        }
                        break;
                    default:
                        result.success(HeadphonesConnectionState.DISCONNECTED.ordinal());
                }
            }
        } else {
            result.notImplemented();
        }
    }
}
