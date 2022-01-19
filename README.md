# Pong-Game
An implementation of the game "Pong" using Java and ACM libraries

To run the code, you will need to use Eclipse as the IDE.

1) Create and name a new Java project. Under "use an execution environment JRE", select "JavaSE-1.8". Then hit "finish".
2) Right click on the newly created project. Under "build path", go to "configure build path". Click on "Add externals JARs..." and select acm.jar. Hit "Apply and Close".
3) Within the project folder, locate the src folder. Right click and go to "New" and then "package". Name the package "ppPackage".
4) Drag and drop all the .java files included here into ppPackage.
5) Right click on the project folder, go to "Run as", then "1 java application".
6) Select "ppSim - ppPackage" and then hit "Ok".

You can control the right paddle by moving your mouse to hit the ball.

The "New Serve" button produces a new serve. 
The "Clear" button resets scores to 0-0. 
The "Trace" button produces a trace of the ball's trajectory.
The "+t -t" slider speeds up and slows down gameplay, and the "rtime" resets the speed to regular speed.
The "-lag +lag" slider affects the opponents tracking speed, making the opponent easier or more difficult to beat. The "rlag" resets the lag back to the original difficulty.
