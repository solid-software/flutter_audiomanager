package software.solid.flutter_audiomanager;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import io.flutter.plugin.common.EventChannel.EventSink;
import io.flutter.plugin.common.EventChannel.StreamHandler;

import static io.flutter.plugin.common.PluginRegistry.Registrar;

public class HeadphonesConnectionReceiver implements StreamHandler {
    private final Registrar registrar;
    private BroadcastReceiver receiver;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    HeadphonesConnectionReceiver(Registrar registrar) {
        this.registrar = registrar;
    }

    @Override
    public void onListen(Object arguments, EventSink events) {
        receiver = createHeadphonesReceiver(events);
        registrar.context().registerReceiver(receiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
    }

    @Override
    public void onCancel(Object o) {
        registrar.context().unregisterReceiver(receiver);
        receiver = null;
    }

    private BroadcastReceiver createHeadphonesReceiver(final EventSink events) {
        return new BroadcastReceiver() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onReceive(Context context, Intent intent) {
                HeadphonesConnectionState event = HeadphonesConnectionState.DISCONNECTED;
                try {
                    String action = intent.getAction();
                    if (action != null && action.equals(Intent.ACTION_HEADSET_PLUG)) {
                        int state = intent.getIntExtra("state", -1);
                        switch (state) {
                            case 0:
                                event = HeadphonesConnectionState.DISCONNECTED;
                                break;
                            case 1:
                                event = HeadphonesConnectionState.CONNECTED;
                                break;
                        }
                    }
                    events.success(event.ordinal());
                } catch (Exception e) {
                    Log.d("HeadphonesReceiver", e.toString());
                }
            }
        };
    }
}
