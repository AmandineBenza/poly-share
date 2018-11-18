------------------------------------------------------
- TOUTAIN Xavier
- FORNALI DAMIEN
- BENZA Amandine
- HAJI Amine						

- Master II IFI
- Nice-Sophia-Antipolis University, 2018-2019

---- Cloud Computing Project ----

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
													 			- Poly-share-online.postman_collection.json || Bundle de requêtes postman qui visent l'application en ligne
													 			- Poly-share-local.postman_collection.json || Bundle de requêtes postman qui visent l'application en local
													 			- Poly-share-API.postman_collection.json || Tests Postman requis dans le sujet
													 			- Poly-share-API.postman_test_run.json || Résultats des tests Postman requis dans le sujet
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
2. importer "Poly-share-API.postman_collection.json".
3. Lancer avec delay=1000 ms et Keep variable values=true

