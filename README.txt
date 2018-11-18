---- Cloud Computing Project - Fabrice HUET ----
-----------------------------------------------
Groupe:
- TOUTAIN Xavier
- FORNALI Damien
- BENZA Amandine
- HAJJI Amine

Contexte:
- Master II IFI
- Nice-Sophia-Antipolis University, 2018-2019
-----------------------------------------------

>> Pour lancer l'application

- Clonez le repository github https://github.com/Damoy/poly-share.git.
- Initialisez l'environement gcloud à l'aide de la commande 'gcloud init'.
- Choisissez le projet 'poly-share' en europe-west3.
- Rendez-vous à la racine du dossier polyshare (au niveau du pom.xml).
- Executez la commande mvn appengine:update.

------------  

>> Composition de l'archive:
-- Polyshare 
						- README.txt 
						- nbactions.xml
						- pom.xml 		

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
													 		-> Requests
													 			- Poly-share-noob-to-casual.postman_collection.json || Scénario de promotion d'un utilisateur Noob
													 			- TODO
													 			- TODO
													 		-> Accounts 
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

[Lancer les Tests Postman]

Logiciel:
- Postman: https://www.getpostman.com/

1. Lancer Postman.
2. Importer /src/main/resources/requests
3. Lancer avec delay=500 ms et Keep variable values=true

- Contenu de "src/main/resources/requests":

1. "Poly-share-noob-to-casual.postman_collection.json"
	- Promotion d'un utilisateur de rang Noob vers Casual
2. "Poly-share-dl-limit-by-rank.postman_collection.json"
	- Démonstration de la limite de download par utilisateur suivant leur niveau
3. ""
	-
4. "Poly-share-trials.postman_collection"
	- Requêtes additionnelles
5. "Poly-share-run.postman_environment.json"
	- Environnement nécessaire au bon déroulement des requêtes
6. "Poly-share-noob-to-casual.postman_test_run.json"
	- Résultat du lancement des requêtes postman pour la promotion d'un utilisateur de rang Noob vers Casual
7. ""
	-

------------  

[Points d'entrée]

/leaderboard || Accès direct au leaderboard

/reset || Accès direct à la réinitialisation de la base de données

/taskqueues/datastoreUpload || Creation et consultation des utilisateurs
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
 