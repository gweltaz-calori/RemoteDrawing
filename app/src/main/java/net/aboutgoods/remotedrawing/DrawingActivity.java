package net.aboutgoods.remotedrawing;

import org.json.JSONObject;

public interface DrawingActivity {
    void onLogin();
    void onNewColorAsked(JSONObject jsonData);
    void onRoomJoined(JSONObject jsonData);
    //void onNotificationReceivedFromServer(String notification);

}
