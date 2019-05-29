<?php

require_once 'fct.inc.php';
require_once 'class.pdogsb.inc.php';
$pdo = PdoGsb::getPdoGsb();

if ($_REQUEST["operation"]=="enreg"){
    $data = $_REQUEST["auth"];
    $auth = json_decode($data) ;
    $login = $auth[0] ;
    $mdp = $auth[1] ;

    try {
        $leVisiteur = $pdo->getInfosVisiteur($login,$mdp);

        if($leVisiteur){
            print_r("Connexion-OK%");
            $idVisiteur = $leVisiteur[0];

            $donnee = $_REQUEST["lesdonnees"];
            $lesMois = json_decode($donnee);

            foreach ($lesMois as $unMois){
                $leMois= $unMois[0];

                if (!$pdo->existeFicheFrais($idVisiteur, $leMois)){
                    $pdo->creeNouvellesLignesFrais($idVisiteur, $leMois);
                    print ("Création nouvelle fiche");
                } else {
                    print"Fiche déjà existante";
                    var_dump($pdo->existeFicheFrais($idVisiteur, $leMois));
                }

                $data = $_REQUEST["lesdonnees"];

                $lesDonnees = json_decode($data);

                foreach ($lesDonnees as $uneDonnee){

                    $lesFraisForfait = array(
                        "ETP" => $uneDonnee[1],
                        "KM" => $uneDonnee[2],
                        "NUI" => $uneDonnee[3],
                        "REP" => $uneDonnee[4]);

                    $pdo->majFraisForfait($idVisiteur, $leMois, $lesFraisForfait);
                    print("enregFraisForfait-OK%");

                    $lesFraisHf = $uneDonnee[5];

                    foreach($lesFraisHf as $key => $value){

                        $annee = substr($leMois, 0, 4);
                        $mois = substr($leMois, 4, 2);
                        $jour = $value[0];

                        if (strlen($jour) == 1){
                            $jour = "0" . $jour;
                        }

                        $date = $jour . '/' . $mois . '/' . $annee;
                        $montant = $value[1];
                        $libelle = $value[2];

                        $pdo->creeNouveauFraisHorsForfait($idVisiteur, $leMois, $libelle, $date, $montant);
                        print("enregFraisHF-OK%");
                        
                    }
                }
            }
        } else {
            print ("Identifiants incorrectes");
        }
    } catch (Exception $e) {
        print("erreur");
        die();
    }
}
?>					
