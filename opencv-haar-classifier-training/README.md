# Train your own OpenCV Haar classifier

This repository aims to provide tools and information on training your own
OpenCV Haar classifier.  Use it in conjunction with this blog post: [Train your own OpenCV Haar.
classifier](http://coding-robin.de/2013/07/22/train-your-own-opencv-haar-classifier.html).



## Instructions

1. Install OpenCV & get OpenCV source

        $ git clone https://github.com/opencv/opencv.git
        $ cd ~/opencv
        $ mkdir release
        $ cd release
        $ cmake -D CMAKE_BUILD_TYPE=RELEASE -D CMAKE_INSTALL_PREFIX=/usr/local ..
        $ cd <cmake_binary_dir> // enter newly created bin directoty
        $ make
        $ sudo make install

2. Put your positive images in the *./positive_images* folder and the negative
images in the *./negative_images* folder

3. Then just run the train.sh script (the script runs in the background 
and will create a log as it trains the classifier):

        ./train.sh $1 -w 80 -h 80 -m 8192


## References & Links:

- [Naotoshi Seo - Tutorial: OpenCV haartraining (Rapid Object Detection With A Cascade of Boosted Classifiers Based on Haar-like Features)](http://note.sonots.com/SciSoftware/haartraining.html)
- [Material for Naotoshi Seo's tutorial](https://code.google.com/p/tutorial-haartraining/)
- [OpenCV Documentation - Cascade Classifier Training](http://docs.opencv.org/doc/user_guide/ug_traincascade.html)
