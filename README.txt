---- Cloud Computing - Poly-Share - Fabrice HUET ----
-----------------------------------------------------
>> Groupe
- TOUTAIN Xavier
- FORNALI Damien
- BENZA Amandine
- HAJJI Amine

>> Contexte
- Master II IFI
- Nice-Sophia-Antipolis University, 2018-2019
- https://docs.google.com/document/d/1VVrKrDH9Hj0iGAbUADNwkXJDBd2Hnzye52RD3ASlBMA/edit#
-----------------------------------------------------

>> Pour lancer l'application

- Clonez le repository github https://github.com/Damoy/poly-share.git.
- Initialisez l'environement gcloud à l'aide de la commande 'gcloud init'.
- Choisissez le projet 'poly-share' en europe-west3.
- Rendez-vous à la racine du dossier polyshare (au niveau du pom.xml).
- Executez la commande mvn appengine:update.

------------  

>> Accéder à la page d'accueil
- https://poly-share.appspot.com/

------------  

>> Tests Postman

Logiciel:
- Postman: https://www.getpostman.com/

1. Lancer Postman.
2. Importer le dossier /src/main/resources/requests
3. Svp, lancer l'environnement "Poly-share-run.postman_environment.json" avec delay=500 ms et Keep variable values=true

- Contenu de "src/main/resources/requests"

1. "Poly-share-noob-to-casual.postman_collection.json"
	- Promotion d'un utilisateur de rang Noob vers Casual
2. "Poly-share-dl-limit-by-rank.postman_collection.json"
	- Démonstration de la limite de download par utilisateur suivant leur niveau
3. "Poly-share-concurrent-dl.postman_collection.json"
	- Démonstration des download en parallèle pour différents utilisateurs
4. "Poly-share-trials.postman_collection"
	- Requêtes additionnelles
5. "Poly-share-run.postman_environment.json"
	- Environnement nécessaire au bon déroulement des requêtes
6. "Poly-share-noob-to-casual.postman_test_run.json"
	- Résultats du lancement des requêtes Postman pour la promotion d'un utilisateur de rang Noob vers Casual
7. "Poly-share-concurrent-dl.postman_test_run.json"
	- Résultats du lancement des requêtes Postman pour les download en parallèle pour différents utilisateurs.

>> En cas de soucis
- Veuillez, s'il vous plaît, vérifier que le total d'envoi de mail est inférieur au quota max (100). Etat de facturation -> ressource -> Destinataires contactés par e-mail:
	https://console.cloud.google.com/appengine?hl=fr&project=poly-share&serviceId=default&duration=PT1H

Si l'utilisation actuelle est au dessus du quota maximum, s'il vous plaît, passez le champ "Utils.MAILS_ACTIVATED" à false puis mvn clean install puis mvn appengine:update puis relancez les tests ! 
------------  

>> Composition de l'archive

-- Polyshare 
						- README.txt : this
						- nbactions.xml
						- pom.xml : maven's pom
						- upload.html : Un utilisateur se rend sur une page d'accueil où il peut uploader des fichiers.

						-> deliverables 
									- ArchitectureAndInstances.pdf (Rendu 1)
									- ArchitectureOverview.pdf (Rendu 1)
									- CloudStorage.pdf (Rendu 2)

						-> Eclipse Project  -> Polyshare -> Java
															- ServletYo.java || Permet de tester bon déploiement de l'application 
															-> commons
																- JSONUtils.java || Utilitaire JSON
																- ServletReset.java || Permet de nettoyer BD
																- Utils.java || Bundle d'utilitaires
															-> datastore
																- DataStoreWorker.java || Worker pour la BD Datastore de Google
																- DownloadDataStoreWorker.java || Worker pour réaliser les tâches de téléchargement
																-> model
																	- DataStoreMessage.java || Utilitaire 
																	- EnumUserRank.java || Rang d'un utilisateur
																	- UserManager.java || Travail relatif à la manipulation d'utilisateurs
															-> download
																- ServletDownload.java || Permet de lancer une requête de téléchargement
															-> jobs
																- DeleteFilesJob.java || Permet de supprimer des fichiers 
																- DeleteLinksJob. java || Permet de supprimer de liens au bout de 5 minutes
															-> mails
																- ServletBounceHandler.java || Erreurs Mails
																- ServletSendMails.java || Permet d'envoyer des mails aux utilisateurs
															-> scores
																- ServletLeaderBoard.java || Affiche un leaderboard
															-> upload
																- CloudStorageHelper.java || Utilitaire pour manipulation du Cloud Storage de Google
																- Upload.java || Permet de lancer une requetre d'upload
																- Worker.java || Implementation d'un worker 
													 -> Resources 
													 		-> postman_collections || Contient les scénarios Postman ainsi que l'environnement à utiliser [NECESSAIRE]
													 		-> postman_runner_results || Contient les résultats des exécutions de ces scénarios Postman
													 		-> gmail_accounts 
													 			- Polyshare_accounts.txt || Comptes GMAIL de nos 6 utilisateurs ainsi que les mots de passe associés
											 		-> Webapp
											 		 	- index.jsp
											 		 	-> WEB-INF
											 		 		- appengine-web.xml
											 		 		- cron.xml 
											 		 		- index.yaml
											 		 		- logging.properties
											 		 		- queue.xml
											 		 		- web.xml

------------  

>> Points d'entrée de l'application

/leaderboard || Accès direct au leaderboard

/reset || Accès direct à la réinitialisation de la base de données

/taskqueues/datastoreUpload || Création et consultation des utilisateurs
		- Exemple de requête : 
			- curl --request POST \
  			--url https://poly-share.appspot.com/taskqueues/datastoreUpload \
  			--header 'Content-Type: application/json' \
  			--data '{"event":"create-users", "data":[{"mail":"noobpolyshare1@gmail.com","rank":"Noob"},{"mail":"noobypolyshare2@gmail.com","rank":"Noob"}]}'

/Upload || Upload d'un fichier 
	- Exemple de requêtes : 
		- curl -X POST "https://poly-share.appspot.com/Upload?mail=xavier96@rocketmail.com&generatedFileSize=1048576" -d ""
		- curl -X POST -F file=@"Nouveau document.py" https://poly-share.appspot.com/Upload?mail=leetpolyshare1@gmail.com

/Download || Téléchargement d'un fichier 
	- Exemple de requête : 
		- curl -X POST "https://poly-share.appspot.com/Download?fileName={{generatedFileName}}&mail=noobpolyshare1@gmail.com"
 