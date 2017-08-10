SignDetect
===================

This app basically consists off two Main Activities.
The main activity in which you can select which sign (classifier) to actually 
detect/evaluate and the Camera on in which the selected Classifier is applied
to the current seen Frame.

The app will most likely crash unless your phone already grants every newly installed app all the permissions it needs
Some permissions must be granted.

> **IMPORTANT:** On First Startup of App check this permissions under your 
settings tab
* Google Location 
* Camera


# The Main Activity
This is the Main Activity of the Sign Detect App.
It is like a settings Activity.
It handles the selected Classifier and launches a new Activity if required.

<img src="../android-pres/scrot01.png" width="330" height="600"/>

# Open Camera:
When everything is ready user may start the camera with the desired classifier.
In this Intent the speeds (the selected classifier) are passed onto the Activity
which handles the camera.
An Example of what it looks like when a Sign is detected:

<img src="../android-pres/scrot00.png" width="600" height="330"/>

