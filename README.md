# CMSC-335-Final

**Overview**
In this project you will construct a Java GUI that uses event handlers, listeners and incorporates Java’s
concurrency functionality and the use of threads. 

**Assignment Details**
As a new engineer for a traffic congestion mitigation company, you have been tasked with developing a
Java GUI that displays time, traffic signals and other information for traffic analysts. The final GUI design
is up to you but should include viewing ports/panels to display the following components of the
simulation:
1. Current time stamps in 1 second intervals
2. Real-time Traffic light display for three major intersections
3. X, Y positions and speed of up to 3 cars as they traverse each of the 3 intersections
Some of the details of the simulation are up to you but the following guidelines will set the guardrails:
1. The components listed above should run in separate threads.
2. Loop through the simulation with button(s) providing the ability to start, pause, stop and
continue the simulation.
3. You will need to use basic distance formulas such as distance = Speed * time. Be sure to be
consistent and define your units of measure (e.g. mile/hour, versus km/hour)
4. Assume a straight distance between each traffic light of 1000 meters.
5. Since you are traveling a straight line, you can assume Y = 0 for your X,Y positions.
