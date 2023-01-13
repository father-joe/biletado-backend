# How To
## Server lokal starten
```
mvn spring-boot:run
```
## Docker-Image bauen und in lokalen Docker-Daemon installieren 
```
mvn jib:dockerBuild
```


# Backend
- Sprache: Java
- Framework: Spring-Boot
- Package-Manager: Maven

- Port: 8081


## Funktionsweise

### Zugriff auf Reservation Datenbank
Der Zugriff auf die Reservation Datenbank wird über das ReservationRepository Interface getätigt. Dieses Interface sendent Querys an die Datenbank, um somit dort Anfragen zu tätigen.


### HTTP-Anfragen bearbeiten
Das Bearbeiten der HTTP-Anfragen wird innerhalb des ReservationsController getätigt.

#### GET-Anfragen
Die GET-Anfragen werden in den Methoden getReservations und getReservation bearbeitet. Bei einer GET-Anfrage an /reservation/ werden alle in der Datenbank gespeicherten Reservationen ausgegeben. Mithilfe von Filtern kann die Suche eingeschrenkt werden. Dies geschiet in der getReservations Methode
Get-Anfragen an /reservation/{Id} werden in der getReservation Methode bearbeitet. Hier wird die Datenbank nur nach dem Eintrag mit der passenden ID abgesucht.

#### POST-Anfragen
POST-Anfragen werden in der createReservation Methode bearbeitet. Hierbei wird überprüft, ob ein Raum mit der in der Reservation angegebene Raum-ID in der Room Datenbank vorhanden ist und ob es Konflikte mit den Zeiten anderen Reservationen gibt.

#### PUT-Anfragen
PUT-Anfragen werden in der createOrUpdateReservation Methode bearbeitet. Hierbei wird ebenfalls überprüft, ob der Angegebene Raum in der Room Datenbank vorhanden ist.

#### DELETE-Anfragen
Für DELETE-Anfragen wird die deleteReservartion Methode verwerndet. Hier wird überprüft, ob die Reservation mit der angegebenen ID in der Reservation Datenbank existiert.

### Authentifizierung
Für die Authentifizierung werden JWTs verwendet. Die HTTP-Anfragen, für welche man eine Authentifizierung braucht sind POST, PUT und DELETE. Nur dann, wenn man ein gültiges JWT mit der Anfrage übergiebt, werden die entsprechenden Methoden im ReservationsController aufgerufen. Gesichter werden diese Methoden mit der @PreAuthorize Anotation. Die Konfigurationen der Authentifizierung werden in der SecurityCongiguration Klasse vorgenommen.

Nur derjenige, der ein gültiges JWT hat, kann nun POST- , PUT- und DELETE-Anfragen an das Backend schicken und diese dort bearbeiten lassen. Aus Sicherheitsgründen wird bei Verwendung eines ungültigen JWT keine Fehlermeldung an den Sender zurückgesendet. 

