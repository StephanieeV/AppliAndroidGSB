package fr.cned.emdsgil.suividevosfrais;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker.OnDateChangedListener;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SyncActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        setTitle("GSB : Synchronisation");

        //accesDistant = new AccesDistant() ;
        //accesDistant.envoi("dernier", new JSONArray(), new JSONArray());

        // chargement des méthodes événementielles
        imgReturn_clic() ;
        //cmdSync_clic();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals(getString(R.string.retour_accueil))) {
            retourActivityPrincipale() ;
        }
        return super.onOptionsItemSelected(item);
    }


    //private static AccesDistant accesDistant;

    /**
     * Sur le clic du bouton synchroniser: synchronisation avec la base de donnée distante
     */
    /*
    private void cmdSync_clic() {
        findViewById(R.id.cmdSync).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                accesDistant.envoi("enreg", new JSONArray(), new JSONArray());
                retourActivityPrincipale() ;
            }
        }) ;
    }*/


    /**
     * Sur la selection de l'image : retour au menu principal
     */
    private void imgReturn_clic() {
        findViewById(R.id.imgTransfertReturn).setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                retourActivityPrincipale() ;
            }
        }) ;
    }





    /**
     * Retour à l'activité principale (le menu)
     */
    private void retourActivityPrincipale() {
        Intent intent = new Intent(SyncActivity.this, MainActivity.class) ;
        startActivity(intent) ;
    }

}