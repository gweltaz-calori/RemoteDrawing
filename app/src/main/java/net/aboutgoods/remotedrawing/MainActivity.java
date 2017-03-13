package net.aboutgoods.remotedrawing;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import net.aboutgoods.remotedrawing.helper.PaintHelper;
import net.aboutgoods.remotedrawing.helper.SocketHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements DrawingActivity {

    private SocketHelper mSocketHelper = SocketHelper.getInstance();
    private DrawingView mDrawingView;
    private TextView textViewEraser;
    private RelativeLayout relativeLayout;
    private EditText editTextRoomName;
    private boolean dialogAppStarted = false; // Afin de permettre a l'utilisateur de faire disparaitre la dialog une fois dans une room
    private AlertDialog dialogChooseRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        SocketHelper.getInstance().login(MainActivity.this);

    }

    //Dans le cas ou l'utilisateur est loggé
    @Override
    public void onLogin()
    {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                showRoomSelectionDialog(); //On lui affiche la dialog avec choix du serveur
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
    //Dans le cas ou l'utilisaeur a join la room demandée
    @Override
    public void onRoomJoined(final JSONObject jsonData) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    dialogChooseRoom.dismiss();
                    String myColor = jsonData.getString("color");
                    Boolean isEraser = jsonData.getBoolean("isEraser");
                    Paint myPaint = PaintHelper.createPaintFromRGB(myColor,12);
                    setupView(myPaint);
                    setEraserText(isEraser); // On change le text en fonction de si l'utilisateur est eraser ou non
                    Snackbar mySnackbar = Snackbar.make(relativeLayout,
                            getResources().getString(R.string.connectedMessage)+" "+editTextRoomName.getText().toString(), Snackbar.LENGTH_SHORT);
                    mySnackbar.show();
                    setTitle("#"+editTextRoomName.getText().toString());
                    dialogAppStarted = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*
    public void onNotificationReceivedFromServer(String notification){
        Snackbar mySnackbar = Snackbar.make(relativeLayout, notification, Snackbar.LENGTH_SHORT);
        mySnackbar.show();
    }*/

    private void setupView(Paint paint)
    {
        getSupportActionBar().show();
        relativeLayout = new RelativeLayout(MainActivity.this);
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
        /*
        Button buttonScaleLinePaint = new Button(MainActivity.this); //On instancie le bouton par rapport à notre MainActivity
        buttonScaleLinePaint.setLayoutParams(new LinearLayout.LayoutParams(221, 221));
        buttonScaleLinePaint.setTextSize(40);
        buttonScaleLinePaint.setText(Html.fromHtml("&#9679;"));
        buttonScaleLinePaint.setBackgroundColor(Color.WHITE);
        buttonScaleLinePaint.getBackground().setAlpha(128);
        buttonScaleLinePaint.setTextColor(Color.BLACK);
        buttonScaleLinePaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mDrawingView.getmLinePaint().setStrokeWidth(45);
            }
        });*/

        SeekBar seekBarScaleLinePaint = new SeekBar(MainActivity.this);
        seekBarScaleLinePaint.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        seekBarScaleLinePaint.setProgress(0);
        //seekBarScaleLinePaint.setSecondaryProgress(20);
        seekBarScaleLinePaint.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDrawingView.getmLinePaint().setStrokeWidth(progress + 12);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
        //linearLayoutButtonScaleLinePaint.addView(buttonScaleLinePaint); // On ajoute le premier bouton au linear layout
        linearLayoutButtonScaleLinePaint.addView(seekBarScaleLinePaint);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.browse_channels:


                showRoomSelectionDialog();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    public void showRoomSelectionDialog() // Methode permettant d'afficher la dialog pour se connecter à une room
    {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null); //Depuis mon layout custom
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        editTextRoomName = (EditText) dialogView.findViewById(R.id.editTextRoomName);
        dialogBuilder.setTitle("Choose a channel");
        dialogBuilder.setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null);
        dialogChooseRoom = dialogBuilder.create();
        dialogChooseRoom.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button buttonPositive = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                buttonPositive.setText("Join");
                buttonPositive.setOnClickListener(new View.OnClickListener() { //Click sur le bouton JOIN

                    @Override
                    public void onClick(View view)
                    {
                        if(editTextRoomName.getText().toString().length()>0)
                            mSocketHelper.joinRoom(MainActivity.this,editTextRoomName.getText().toString()); //On demande a join la room demandée
                    }
                });
                Button buttonNegative = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                buttonNegative.setText("Cancel");
                if(dialogAppStarted) //On doit empecher la dialog de se fermer avec le bouton cancel si c'est la premiere fois qu'elle se lance
                    buttonNegative.setEnabled(true);
                else
                    buttonNegative.setEnabled(false);
                buttonNegative.setOnClickListener(new View.OnClickListener() { //Click sur le bouton Cancel

                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();
                    }
                });
            }
        });
        dialogChooseRoom.show();
    }
}