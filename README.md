Implemented using JavaFX, Java .


This is the BattleShip Game. 
1. It has two modes 
  a. Two players
  b. One player VS Computer

2. You can pause the game and store it and you can load the same file and resume the game.
3. You can play one vs one in a network.
4. You can check the ships on the opponent ship if you want to cheat. 
5. You can select different modes , if you select specific mode you can get suggestion after some time.
6. Computer AI is good .


For dockerize application:

run the command 
docker build -t imagename .

Then on a separate command prompt run 
socat TCP-LISTEN:6000,reuseaddr,fork UNIX-CLIENT:\"$DISPLAY\"

Now run the docker container
docker run -v /tmp/.X11-unix:/tmp/.X11-unix -e DISPLAY=$(ipconfig getifaddr en0):0 myImageName


Reference: https://medium.com/@learnwell/how-to-dockerize-a-java-gui-application-bce560abf62a