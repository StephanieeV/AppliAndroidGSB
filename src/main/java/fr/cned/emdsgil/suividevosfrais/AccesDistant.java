package fr.cned.emdsgil.suividevosfrais;


import android.util.Log;

import org.json.JSONArray;

public class AccesDistant implements AsyncResponse{

    // constante
    private static final String SERVERADDR = "http://192.168.0.15/appandroid/serveur.php";


    public AccesDistant(){
        super();
    }

    /**
     * retour du serveur distant
     * @param output
     */
    @Override
    public void processFinish(String output) {
        Log.d("serveur","****************"+output);
        // découpage du message recu avec %
        String[] message = output.split("%");
        // dans message[0] : "enreg", "dernier", "Erreur !"
        // dans message[1] : reste du message

        // s'il y a 2 cases
        if (message.length>1){
            if (message[0].equals("enreg")){
                Log.d("enreg","**************"+message[1]);
            } else {
                if (message[0].equals("dernier")){
                    Log.d("dernier","**************"+message[1]);
                } else {
                    if (message[0].equals("Erreur !")){
                        Log.d("Erreur !","**************"+message[1]);
                    }
                }
            }
        }
    }

    public void envoi(String operation, JSONArray authJSON, JSONArray lesFraisJSON){
        AccesHTTP accesDonnees = new AccesHTTP();
        // lien de délégation
        accesDonnees.delegate = this;
        // ajout parametres
        accesDonnees.addParam("operation", operation);
        accesDonnees.addParam("lesdonnees", lesFraisJSON.toString());
        accesDonnees.addParam("auth", authJSON.toString());
        // appel au serveur
        accesDonnees.execute(SERVERADDR);
    }
}
