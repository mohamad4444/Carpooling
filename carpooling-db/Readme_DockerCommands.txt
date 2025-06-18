
## Erzeugen eines Docker Image
## Image erhält den Namen mysql_image 
## Muss nur einmal ausgeführt werden

docker build -f Dockerfile -t swtp_carpooling_image .


## Erzeugen eines Docker Container
## - Im Container werden automatisch SQL-Scripte ausgeführt
## - Es erfolgt ein Netzwerk- und Datei-Mapping
## Ausführen im Projektverzeichnis
## Muss nur einmal ausgeführt werden

== Remove old Images and containers ==
docker rm -f swtpCarpooling 
docker rmi swtp_carpooling_image
==Build image ==
docker build -t swtp_carpooling_image .

== Starten ==

docker run --name swtpCarpooling -d -p3306:3306 swtp_carpooling_image



## Nachdem der Container erzeugt wurde
## kann er immer wieder gestartet oder beendet werden

docker start swtpCarpooling 
docker stop swtpCarpooling 

