package net.aboutgoods.remotedrawing;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

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

    // Dans le cas ou une nouvelle couleur à été renvoyé par le serveur on fera le traitement nécéssaire avec celle ci (la signature onNewColorAsked est dans l'interface drawing activity)
    @Override
    public void onNewColorAsked(final JSONObject jsonData)
    {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                // TODO Alban Création du setter dans DrawingView pour changer de couleur


            }
        });

    }

    private void setupView(Paint paint) {
        RelativeLayout relativeLayout = new RelativeLayout(MainActivity.this);
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(relativeLayoutParams);

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

        //Ajout du bouton demande d'une autre couleur
        Button buttonAskNewColor = new Button(MainActivity.this); //On instancie le bouton par rapport à notre MainActivity
        buttonAskNewColor.setText(getString(R.string.askNewColor)); // On définit le text par rapport à une string du dossier strings.xml
        buttonAskNewColor.setBackgroundColor(Color.TRANSPARENT); // On choisi une couleur de fond transparent
        buttonAskNewColor.setTextColor(Color.WHITE); // Le texte sera blanc
        buttonAskNewColor.setPadding(16, 16, 16, 16); // On choisi un padding de 16dp
        buttonAskNewColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SocketHelper.getInstance().askNewColor(MainActivity.this);
            }
        });

        //Création d'un linear layout pour les boutons supérieurs

        LinearLayout linearLayoutTopButtons = new LinearLayout(MainActivity.this); // On instancie le LinearLayout par rapport à notre MainActivity
        linearLayoutTopButtons.setOrientation(LinearLayout.HORIZONTAL); // On choisi l'orientation de notre layout (ici horizontal)
        LinearLayout.LayoutParams linearLayoutTopButtonsLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);// On choisi la hauteur et la largeur
        linearLayoutTopButtons.setLayoutParams(linearLayoutTopButtonsLayoutParams);
        linearLayoutTopButtons.setGravity(Gravity.CENTER); // On centre le tout au milieu
        linearLayoutTopButtons.addView(button); // On ajoute le premier bouton au linear layout
        linearLayoutTopButtons.addView(buttonAskNewColor); // On ajoute le premier bouton au linear layout


        // Ajout du layout boutons au Linear layout général
        relativeLayout.addView(linearLayoutTopButtons);
        setContentView(relativeLayout);

        mSocketHelper.drawOn(MainActivity.this, mDrawingView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocketHelper.disconnect();
    }
}