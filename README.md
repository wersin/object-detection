SignDetect -- App Detects Speed Traffic Signs
===================

Sign Detect is a simple app to detect traffic signs. You mount the smartphone onto your dashboard and drive. It detects *some* of the
speed traffic signs and displays what it has found.


# About repository
- **SginDetect** is the full application tu run on your device
- **android-pres** folder with tex slides of the final presentation
- **cascades** final generated cascades
- **lightning-mark** lightning presentation of Mark Bley
- **opencv-haar-classifier-training** some scripts to automate the training

# Prerequisites
* Have latest SDK Version (as of now August 2017 SDK Version 25)
* At least Android SDK Build Tools 26
* CMake
* LLDB (Debugger for C++ Code)
* Native Development Kit
* Have latest [OpenCV](http://opencv.org/releases.html) installed on your device

# SignDetect
The App has a simple UI to choose from the different cascades to evaluate. Since the amount of processing power required 
to evaluate one cascade and draw the found traffic sign takes some time you have to chose which one to actually evaluate.

<img src="android-pres/scrot01.png" width="330" height="600"/>

* The Speed Meter Button launches a new Activity in which the current Speed in
[m/s] is measured based on GPS Location data.
* With the Open Camera Button Camera launches in a new Activity and the elected cascade starts evaluating the frames.

<img src="android-pres/scrot00.png" width="600" height="330"/>

This is what the app looks like when running

# Cascades
Here are some useful scripts to keep everything nice and clean.
The finished Cascades and the Positive and Negative samples used for the classifiers

# Classifier Training
In this folder you will find all the necessary tools to create your own classifiers.
