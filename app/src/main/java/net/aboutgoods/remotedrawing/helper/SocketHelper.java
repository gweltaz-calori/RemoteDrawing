package net.aboutgoods.remotedrawing.helper;

import android.app.Activity;
import android.graphics.Paint;

import net.aboutgoods.remotedrawing.DrawingActivity;
import net.aboutgoods.remotedrawing.DrawingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.LinkedHashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * The singleton Socket helper.
 */
public class SocketHelper {

    private static final String HOST = "http://192.168.1.95:3000";

    private LinkedHashMap<String, String> mUserList;

    private static SocketHelper mInstance = null;

    private Socket mSocket;

    private SocketHelper() {

        try {
            mSocket = IO.socket(HOST);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.connect();

        mSocket.on("userList", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray jsonArray = (JSONArray) args[0];
                mUserList = new LinkedHashMap<String, String>();
                for(int i=0; i<jsonArray.length(); i++) {
                    try {
                        JSONObject jsonUser = jsonArray.getJSONObject(i);
                        mUserList.put(jsonUser.getString("id"), jsonUser.getString("color"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Get instance socket helper.
     *
     * @return the socket helper
     */
    public static SocketHelper getInstance(){
        if(mInstance == null) {
            mInstance = new SocketHelper();
        }
        return mInstance;
    }

    /**
     * Send coordinate.
     *
     * @param oldX the old x
     * @param oldY the old y
     * @param newX the new x
     * @param newY the new y
     */
    public void sendCoordinate(float oldX, float oldY, float newX, float newY) {
        JSONObject jsonCoordinate = new JSONObject();

        try {
            JSONObject jsonOldCoordinate = new JSONObject();
            jsonOldCoordinate.put("x", oldX);
            jsonOldCoordinate.put("y", oldY);

            JSONObject jsonNewCoordinate = new JSONObject();
            jsonNewCoordinate.put("x", newX);
            jsonNewCoordinate.put("y", newY);

            jsonCoordinate.put("old", jsonOldCoordinate);
            jsonCoordinate.put("new", jsonNewCoordinate);

            mSocket.emit("drawing", jsonCoordinate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draw on view.
     *
     * @param activity    the activity
     * @param drawingView the drawing view
     */
    public void drawOn(final Activity activity, final DrawingView drawingView) {

        mSocket.on("receiveDrawing", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject jsonData = (JSONObject) args[0];
                drawingView.drawFromJson(jsonData, activity);
            }
        });

        mSocket.on("clear", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                drawingView.clear(activity);
            }
        });
    }

    /**
     * Login.
     *
     * @param activity the activity
     */
    public void login(final Activity activity) {

        Emitter.Listener listener = new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                JSONObject jsonData = (JSONObject) args[0];

                if(activity instanceof DrawingActivity) {
                    ((DrawingActivity) activity).onLogin(jsonData);
                } else {
                    throw new StackOverflowError(activity.getLocalClassName() + " must implement DrawingActivity");
                }
            }
        };

        mSocket.on("me", listener);
        mSocket.emit("login", "");
    }

    /**
     * Clear drawing surface.
     */
    public void clearDrawingSurface() {
        mSocket.emit("clear");
    }

    /**
     * Gets paint color from user id.
     *
     * @param userId the user id
     * @return the paint color from user id
     */
    public Paint getPaintColorFromUserId(String userId) {
        return PaintHelper.createPaintFromRGB(mUserList.get(userId));
    }
}
