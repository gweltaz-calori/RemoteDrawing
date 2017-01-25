package net.aboutgoods.remotedrawing;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import net.aboutgoods.remotedrawing.helper.PaintHelper;
import net.aboutgoods.remotedrawing.helper.SocketHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements DrawingActivity {

    private SocketHelper mSocketHelper = SocketHelper.getInstance();
    private DrawingView mDrawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SocketHelper.getInstance().login(MainActivity.this);
    }

    @Override
    public void onLogin(final JSONObject jsonData) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String myColor = jsonData.getString("color");
                    Paint myPaint = PaintHelper.createPaintFromRGB(myColor);
                    setupView(myPaint);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupView(Paint paint) {
        RelativeLayout relativeLayout = new RelativeLayout(MainActivity.this);

        mDrawingView = new DrawingView(MainActivity.this, paint);
        relativeLayout.addView(mDrawingView);

        Button button = new Button(MainActivity.this);
        button.setText(getString(R.string.clearAll));
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setTextColor(Color.WHITE);
        button.setPadding(16, 16, 16, 16);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.clear(MainActivity.this);
                mSocketHelper.clearDrawingSurface();
            }
        });

        relativeLayout.addView(button);
        setContentView(relativeLayout);

        mSocketHelper.drawOn(MainActivity.this, mDrawingView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocketHelper.disconnect();
    }
}