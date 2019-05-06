package fr.cned.emdsgil.suividevosfrais;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

public class AccesDistant implements AsyncResponse{

    // constante
    private static final String SERVERADDR = "http://ppe.stephanie-vieville.fr/includes/serveur.php";
    private Context context;


    /**
     * Constructeur
     */
    public AccesDistant(){
        super();
        this.context = context;
    }

    /**
     * retour du serveur distant
     * @param output
     */
    @Override
    public void processFinish(String output) {
        // message sur retour du serveur
        Log.d("serveur","****************"+output);
        // découpage du message recu avec %
        String[] message = output.split("%");
        Log.d("retour", "***************   "+message[0]);
        // dans message[0] : "enreg", "dernier", "Erreur !"
        // dans message[1] : reste du message

        // contrôle si le serveur a retourné un message
        if (message.length>0){
            // test si l'utilisateur a bien été connecté
            if (message[0].equals("authentification-OK")){
                ((SyncActivity)context).envoieSucces();
                Log.d("retour","****************"+message[0]);
            }else if (message[0].equals("authentification-ERROR")){
                ((SyncActivity)context).envoieErreur();
                Log.d("retour","***************"+message[0]);
            }else if (message[0].equals("Erreur !")) {
                Log.d("retour", "************* erreur");
            }


        }
    }


    public void envoi(String operation, JSONArray authJSON, JSONArray lesFraisJSON){
        AccesHTTP accesDonnees = new AccesHTTP();
        // lien de délégation
        accesDonnees.delegate = this;
        // ajout parametres
        accesDonnees.addParam("operation", operation);
        accesDonnees.addParam("auth", authJSON.toString());
        accesDonnees.addParam("lesdonnees", lesFraisJSON.toString());

        // appel au serveur
        accesDonnees.execute(SERVERADDR);
    }

}
