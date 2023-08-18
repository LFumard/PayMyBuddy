# PayMyBuddy

Transfert d'argent entre amis

## Infos
- Java version : 17
- Spring Boot 2.7.12
- Mysql 8.0.32 

## Paramètres et pré requis

- Le port local localhost http par défaut est 8080 et peut être adapté depuis la section *server.port* du fichier de configuration application.properties
                         `server.port=8080`
- Mot de passe de connexion au serveur MySQL à préciser dans la section *spring.datasource.password*  du fichier de configuration application.properties

- Executer le script SQL pour création de la base de données et schéma associé à partir du fichier scriptBdD/Schema.sql

## MPD et Diagramme UML
- MPD
![MPD](https://github.com/LFumard/PayMyBuddy/blob/main/src/main/resources/static/images/MPD.png)

- Diagramme UML
![Diagram UML](https://github.com/LFumard/PayMyBuddy/blob/main/src/main/resources/static/images/Class.png)
