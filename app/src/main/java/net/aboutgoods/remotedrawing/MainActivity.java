package net.aboutgoods.remotedrawing;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import net.aboutgoods.remotedrawing.helper.PaintHelper;
import net.aboutgoods.remotedrawing.helper.SocketHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity implements DrawingActivity {

    private SocketHelper mSocketHelper = SocketHelper.getInstance();
    private DrawingView mDrawingView;
    private TextView textViewEraser;
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
                    Boolean isEraser = jsonData.getBoolean("isEraser");
                    Paint myPaint = PaintHelper.createPaintFromRGB(myColor,12);
                    setupView(myPaint);
                    setEraserText(isEraser); // On change le text en fonction de si l'utilisateur est eraser ou non
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
                try{
                    String myColor = jsonData.getString("color");
                    Boolean isEraser = jsonData.getBoolean("isEraser");
                    setEraserText(isEraser);// On change le text en fonction de si l'utilisateur est eraser ou non
                    Paint myPaint = PaintHelper.createPaintFromRGB(myColor,mDrawingView.getmLinePaint().getStrokeWidth());
                    mDrawingView.setmLinePaint(myPaint);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void setupView(Paint paint) {
        RelativeLayout relativeLayout = new RelativeLayout(MainActivity.this);
        RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(relativeLayoutParams);

        mDrawingView = new DrawingView(MainActivity.this, paint);
        relativeLayout.addView(mDrawingView);

        //Ajout du bouton pour clear
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

        //Création d'un linear layout pour les boutons supérieurs ("clear for all" et "ask new color")
        LinearLayout linearLayoutTopButtons = new LinearLayout(MainActivity.this); // On instancie le LinearLayout par rapport à notre MainActivity
        linearLayoutTopButtons.setOrientation(LinearLayout.HORIZONTAL); // On choisi l'orientation de notre layout (ici horizontal)
        LinearLayout.LayoutParams linearLayoutTopButtonsLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);// On choisi la hauteur et la largeur
        linearLayoutTopButtons.setLayoutParams(linearLayoutTopButtonsLayoutParams);
        linearLayoutTopButtons.setGravity(Gravity.CENTER); // On centre le tout au milieu
        linearLayoutTopButtons.addView(button); // On ajoute le premier bouton au linear layout
        linearLayoutTopButtons.addView(buttonAskNewColor); // On ajoute le premier bouton au linear layout


        //Ajout du bouton de taille de pinceau
        Button buttonScaleLinePaint = new Button(MainActivity.this); //On instancie le bouton par rapport à notre MainActivity
        buttonScaleLinePaint.setWidth(61);
        buttonScaleLinePaint.setHeight(61);
        buttonScaleLinePaint.setTextSize(40);
        buttonScaleLinePaint.setText(Html.fromHtml("&#9679;"));
        buttonScaleLinePaint.setBackgroundColor(Color.WHITE);
        buttonScaleLinePaint.getBackground().setAlpha(128);
        buttonScaleLinePaint.setTextColor(Color.BLACK);
        buttonScaleLinePaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SocketHelper.getInstance().askNewColor(MainActivity.this);
                mDrawingView.getmLinePaint().setStrokeWidth(45);
            }
        });

        //Ajout du textview pour l'eraser
        textViewEraser = new TextView(MainActivity.this);//On instancie le textview par rapport à notre MainActivity
        textViewEraser.setText("You are the eraser"); // On définit le texte
        textViewEraser.setTextColor(Color.WHITE); // Choix de la couleur noire
        textViewEraser.setTextSize(21); //On choisi la taille de police
        textViewEraser.setPadding(16, 16, 16, 16);

        textViewEraser.setVisibility(View.INVISIBLE); // On le met invisble au depart

        //Ajout du Linear layout pour changer la taille du pinceau
        LinearLayout linearLayoutButtonScaleLinePaint = new LinearLayout(MainActivity.this); // On instancie le LinearLayout par rapport à notre MainActivity
        linearLayoutButtonScaleLinePaint.setOrientation(LinearLayout.HORIZONTAL); // On choisi l'orientation de notre layout (ici horizontal)
        LinearLayout.LayoutParams linearLayoutButtonScaleLinePaintParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);// On choisi la hauteur et la largeur
        linearLayoutButtonScaleLinePaint.setLayoutParams(linearLayoutButtonScaleLinePaintParams);
        linearLayoutButtonScaleLinePaint.setGravity(Gravity.CENTER); // On centre le tout au milieu
        linearLayoutButtonScaleLinePaint.addView(buttonScaleLinePaint); // On ajoute le premier bouton au linear layout

        //Ajout du Relative layout pour changer les infos eraser
        RelativeLayout relativeLayoutEraserInfos = new RelativeLayout(MainActivity.this); // On instancie le RelativeLayout par rapport à notre MainActivity

        RelativeLayout.LayoutParams linearLayoutEraserInfosParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);// On choisi la hauteur et la largeur
        linearLayoutEraserInfosParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);//On aligne en bas
        relativeLayoutEraserInfos.setLayoutParams(linearLayoutEraserInfosParams);
        relativeLayoutEraserInfos.setGravity(Gravity.CENTER); // On centre le tout au milieu

        relativeLayoutEraserInfos.addView(textViewEraser);//On ajout le textview au layout

        //Linear Layout pour le positionnement des menus en haut
        LinearLayout linearLayoutTopMenus = new LinearLayout(MainActivity.this); // On instancie le LinearLayout par rapport à notre MainActivity
        linearLayoutTopMenus.setOrientation(LinearLayout.VERTICAL); // On choisi l'orientation de notre layout
        linearLayoutTopMenus.setLayoutParams(linearLayoutButtonScaleLinePaintParams);
        linearLayoutTopMenus.setGravity(Gravity.CENTER); // On centre le tout au milieu
        linearLayoutTopMenus.addView(linearLayoutTopButtons);
        linearLayoutTopMenus.addView(linearLayoutButtonScaleLinePaint);

        // Ajout du liner layout des menus au Linear layout général
        relativeLayout.addView(linearLayoutTopMenus);
        relativeLayout.addView(relativeLayoutEraserInfos);
        setContentView(relativeLayout);

        mSocketHelper.drawOn(MainActivity.this, mDrawingView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocketHelper.disconnect();
    }
    public void setEraserText(Boolean isEraser)
    {
        if(isEraser)
            textViewEraser.setVisibility(View.VISIBLE); //Visible si il est eraser
        else
            textViewEraser.setVisibility(View.INVISIBLE); //Invisible sinon
    }
}