package fr.cned.emdsgil.suividevosfrais;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class SyncActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        setTitle("GSB : Synchronisation");

        // chargement des méthodes événementielles
        imgReturn_clic() ;
        cmdSync_clic();


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

    /**
     * retourne l'idVisiteur et le mdp au format JSON
     * @return JSONArray
     */
    private JSONArray getAuthJSON(){
        String idVisiteur = ((EditText)findViewById(R.id.txtLogin)).getText().toString();
        String mdp = ((EditText)findViewById(R.id.txtMdp)).getText().toString();
        List laListe = new ArrayList();
        laListe.add(idVisiteur);
        laListe.add(mdp);
        return new JSONArray(laListe);
    }

    /**
     * retourne toutes les données de frais sérializées au format JSON
     * @return JSONObject
     */
    private JSONArray getLesFraisJSON(){
        List listeMois = new ArrayList();
        Set<Integer> keys = Global.listFraisMois.keySet();
        for (Integer unMois: keys){
            List listeFraisDuMois = new ArrayList();

            listeFraisDuMois.add(Global.listFraisMois.get(unMois).getEtape());
            listeFraisDuMois.add(Global.listFraisMois.get(unMois).getKm());
            listeFraisDuMois.add(Global.listFraisMois.get(unMois).getNuitee());
            listeFraisDuMois.add(Global.listFraisMois.get(unMois).getRepas());

            List listeLesFraisHf = new ArrayList();

            for (FraisHf unFraisHf : Global.listFraisMois.get(unMois).getLesFraisHf()){
                List listeInfosDuFraisHf = new ArrayList();

                listeInfosDuFraisHf.add(unFraisHf.getJour());
                listeInfosDuFraisHf.add(unFraisHf.getMontant());
                listeInfosDuFraisHf.add(unFraisHf.getMotif());

                listeLesFraisHf.add(listeInfosDuFraisHf);
            }
            listeFraisDuMois.add(listeLesFraisHf);

            listeMois.add(listeFraisDuMois);
        }
        return new JSONArray(listeMois);
    }



    /**
     * Sur le clic du bouton synchroniser: synchronisation avec la base de donnée distante
     */

    private void cmdSync_clic() {
        findViewById(R.id.cmdSync).setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                AccesDistant accesDistant = new AccesDistant();
                accesDistant.envoi("enreg", getAuthJSON(), getLesFraisJSON());
                Log.d("auth","************** authJSON : "+ getAuthJSON().toString());
                Log.d("lesFrais", "*********************** les fraisJSON : " + getLesFraisJSON().toString());
            }
        }) ;
    }


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

    public void envoieSucces() {
        Toast.makeText(this, "Les frais ont été pris en compte.", Toast.LENGTH_LONG).show();
    }

    public void envoieErreur() {
        Toast.makeText(this, "Identifiant ou mot de passe incorrecte.", Toast.LENGTH_LONG).show();
    }
}