
## Erzeugen eines Docker Image
## Image erhält den Namen mysql_image 
## Muss nur einmal ausgeführt werden

docker build -f Dockerfile -t swtp_rideshare_image .


## Erzeugen eines Docker Container
## - Im Container werden automatisch SQL-Scripte ausgeführt
## - Es erfolgt ein Netzwerk- und Datei-Mapping
## Ausführen im Projektverzeichnis
## Muss nur einmal ausgeführt werden

== Remove old Images and containers ==
docker rm -f swtpRideShare 
docker rmi swtp_rideshare_image
==Build image ==
docker build -t swtp_rideshare_image .

== Starten ==

docker run --name swtpRideShare -d -p3306:3306 swtp_rideshare_image



## Nachdem der Container erzeugt wurde
## kann er immer wieder gestartet oder beendet werden

docker start swtpRideShare 
docker stop swtpRideShare 

