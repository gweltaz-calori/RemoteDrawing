package net.aboutgoods.remotedrawing;

import org.json.JSONObject;

public interface DrawingActivity {
    void onLogin(JSONObject jsonData);
    void onNewColorAsked(JSONObject jsonData);
}
