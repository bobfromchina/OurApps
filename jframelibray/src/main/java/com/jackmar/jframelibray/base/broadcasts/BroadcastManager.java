package com.jackmar.jframelibray.base.broadcasts;

import android.content.Intent;
import android.content.IntentFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JackMar on 2017/6/9.
 */

public class BroadcastManager {
    static volatile BroadcastManager single = null;
    private Map<String, BroMessage> mReceiverMap = new HashMap();
    private IntentFilter mIntentFilter = new IntentFilter("j.intent.action.JBROADCAST");

    public synchronized static BroadcastManager getInstance() {
        if (single == null) {
            single = new BroadcastManager();
        }
        return single;
    }

    public BroadcastManager() {
    }

    public void registerReceiver(IBroadcast broadcast) {
        String key = String.valueOf(broadcast.hashCode());
        BroMessage receiver = (BroMessage) this.mReceiverMap.get(key);
        if (receiver == null) {
            receiver = new BroMessage(broadcast);

            try {
                broadcast.getContext().registerReceiver(receiver, this.mIntentFilter);
                this.mReceiverMap.put(String.valueOf(broadcast.hashCode()), receiver);
            } catch (Exception var5) {
                var5.printStackTrace();
            }

        }
    }

    public void unregisterReceiver(IBroadcast broadcast) {
        String key = String.valueOf(broadcast.hashCode());
        BroMessage receiver = (BroMessage) this.mReceiverMap.get(key);
        if (receiver == null) {
            this.mReceiverMap.remove(key);
        } else {
            broadcast.getContext().unregisterReceiver(receiver);
            this.mReceiverMap.remove(key);
        }
    }

    public void sendBroadcastMessage(IBroadcast broadcast, Intent intent, String filter) {
        intent = intent == null ? new Intent() : intent;
        filter = filter.toString();
        intent.putExtra("filter", filter);
        intent.setAction("j.intent.action.JBROADCAST");
        broadcast.getContext().sendBroadcast(intent);
    }

}
