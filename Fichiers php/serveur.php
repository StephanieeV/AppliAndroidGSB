<?php

include '../includes/class.pdogsb.inc.php';
include '../includes/fct.inc.php';

// contrôle de réception de paramètre
if(isset($_REQUEST["operation"])){
	if($_REQUEST["operation"]== "connexion"){
		try{
			print "connexion%";
			// connexion à la base de données
			$cnx = PdoGsb::getPdoGsb();
			// récuperation des données
			$lesDonnees = json_decode($_REQUEST['lesdonnees']);
			$login = $lesDonnees[0];
			$mdp = $lesDonnees[1];
			$visiteur = $cnx->getInfosVisiteur($login, $mdp);
			if (!is_array($visiteur)) {
				print "Login ou mot de passe invalide%";
			} else {
				print "Connexion réussie%";
				print (json_encode($visiteur);
			}
		} catch(PDOException $e){
			print "Erreur !%".$e->getMessage();
			die();
		}
	}elseif($_REQUEST["operation"]=="enreg"){
		try {
			print "enreg%";
			$idVisiteur = $_REQUEST['idVisiteur'];
			$objetDonnees = json_decode($_REQUEST['lesdonnees']);
			$lesDonnees = objectToArray($objetDonnees);
			// connexion à la base de données
			$cnx = PdoGsb::getPdoGsb();
			// envoie et saubegardes des données sur la base de données
			foreach($lesDonnees as $uneLigne) {
				$annee = $uneLigne['annee'];
				$mois = $uneLigne['mois'];
				if (strlen($mois) == 1) {
					$mois = '0'.$mois;
				}
				$anneeMois = $annee.$mois;
				$lesFrais = array('ETP'=>$uneLigne['etape'], 'NUI'=>$uneLigne['nuitee'],'REP'=>$uneLigne['repas'],'KM'=>$uneLigne['km']);
				if ($cnx->estPremierFraisMois($idVisiteur, $anneeMois)){
					$cnx->creeNouvellesLignesFrais($idVisiteur, $anneeMois);
				}
				$cnx->majFraisForfait($idVisiteur, $anneeMois, $lesFrais);
				$lesFraisHF=$uneLigne['lesFraisHF'];
				if(!empty($lesFraisHF)){
					foreach($lesFraisHF as $unFraisHF){
						$jour = $unFraisHF['jour'];
						$montant = $unFraisHF['montant'];
						$motif = $unFraisHF['motif'];
						$date = $jour."/".$mois."/".$annee;
						$cnx->creeNouveauFraisHorsForfait($idVisiteur, $anneeMois, $motif, $date, $montant);
					}
				}
			}
		} catch(PDOException $e){
			print "Erreur !%".$e->getMessage();
			die();
		}
	}
}


?>