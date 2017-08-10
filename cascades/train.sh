#!/bin/bash
#================================================================
# HEADER
#================================================================
#% SYNOPSIS
#+    ${SCRIPT_NAME} [--help] [-w<width>] [-h<heigth>] [-m<amount of RAM>]
#%
#% DESCRIPTION
#%    Creates Lists for the images located in 'positives' and 
#%    negatives Folder
#%    Then based on the samples creates more for the training stage.
#%    The originals are rotated, scaled... and a lot 'new positives' 
#%    ones are created.
#%    The newly generated positives are then used to train the cascade.
#%    By default the amount of RAM is set to 1024 for precalcValBufSize 
#%    and precalcIdxBufSize.
#%
#% OPTIONS
#%    -w, --width         [width of output image]
#%    -h, --height        [height of output image]
#%    -m, --memory        [dedicated RAM]
#%    --help              Print this help
#%
#% EXAMPLES
#%    ${SCRIPT_NAME} -w 80 -h 80 -m 8192
#%
#================================================================
#- IMPLEMENTATION
#-    version         ${SCRIPT_NAME} 0.0.1, August 2017
#-    author          Mark Bley
#-    copyright       None
#-    license         WTFPL
#-
#================================================================

usage()
{
    echo "---------------------------------"
    echo "Usage:"
    echo "everything is optional in any case it will be"
    echo "executed with 1024MB of RAM and default resolution of images"
    echo "-w, --width         [width of output image]"
    echo "-h, --height        [height of output image]"
    echo "-m, --memory        [dedicated RAM]"
    echo "-h, --help          Print this help"
    echo "example: $1 -w 80 -h 80 -m 8192"
    echo "---------------------------------"
}

#generates two lists with the location of the positives and negative samples
create_lists()
{
    echo "creating new list files for for samples"
    find ./positive_images -iname "*.jpg" > positives.txt
    find ./negative_images -iname "*.jpg" > negatives.txt
}

#creates sets of positive sapmples to train the classifiers
#if specified the resolution will be set acordingly
positives_merge()
{
    if [ -z ${1+x} ]; then
        echo "test 1"
        perl bin/createsamples.pl positives.txt negatives.txt samples 1500\
           "opencv_createsamples -bgcolor 0 -bgthresh 0 -maxxangle 1.1\
           -maxyangle 1.1 maxzangle 0.5 -maxidev 40 -w 80 -h 40"
    else
        echo "$1 $2"
        perl bin/createsamples.pl positives.txt negatives.txt samples 1500\
           "opencv_createsamples -bgcolor 0 -bgthresh 0 -maxxangle 1.1\
           -maxyangle 1.1 maxzangle 0.5 -maxidev 40 -w $1 -h $2"
    fi
    python ./tools/mergevec.py -v samples/ -o samples.vec
}

#trains the classifiers based on the positive and negaitve samples
#if specified the resolution will be set acordingly
train()
{
    #run in background with nohup but log output to file
    if [ -z ${2+x} ] || [ -z ${3+x} ]; then
       nohup opencv_traincascade -data classifier -vec samples.vec -bg negatives.txt\
       -numStages 20 -minHitRate 0.999 -maxFalseAlarmRate 0.5 -numPos 1000\
       -numNeg 600 -mode ALL -precalcValBufSize $1\
       -precalcIdxBufSize $1 > opencv-log
    else
        nohup opencv_traincascade -data classifier -vec samples.vec -bg negatives.txt\
       -numStages 20 -minHitRate 0.999 -maxFalseAlarmRate 0.5 -numPos 1000\
       -numNeg 600 -w $2 -h $3 -mode ALL -precalcValBufSize $1\
       -precalcIdxBufSize $1 > opencv-log
    fi
}


main()
{
    #handle user input
    if [ "$1" == "--help" ] || [ -z ${1+x} ]; then
        usage $0
        exit 0
    fi
    while [[ $# -gt 1 ]]
    do
        key="$1"

        case $key in
            -m|--memory)
                ram="$2"
                shift # past argument
            ;;
            -h|--heigth)
                height="$2"
            shift # past argument
            ;;
            -w|--width)
                width="$2"
            shift # past argument
            ;;
            *)
                    # unknown option
            ;;
        esac
        shift # past argument or value
    done
    #if set go to direcgtory
    if [ -z ${ram+x} ]; then
        ram=1024
    fi

    create_lists
    if [ -z ${height+x} ] || [ -z ${width+x} ]; then
        positives_merge
    else
        positives_merge ${width} ${height}
    fi
    train ${ram} ${width} ${height}
}
main "$@"
