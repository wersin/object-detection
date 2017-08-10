#!/bin/bash
#================================================================
# HEADER
#================================================================
#% SYNOPSIS
#+    ${SCRIPT_NAME} [-h] [-m<movie>]
#%
#% DESCRIPTION
#%    Take any kind of video on your device and extract the images.
#%    Feel free to change the intervals in which a snapshot is taken.
#%    
#%
#% OPTIONS
#%    -m, --movie         [movie to extract images from]
#%    -h, --help          Print this help
#%
#% EXAMPLES
#%    ${SCRIPT_NAME} $1 -n 500 -m asngry-birds.mp4
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
    echo "-n, --count       Amont of negatives to create"
    echo "-h or help display this message"
    echo "example: $1 -n 500 -m angry-birds.mp4"
    echo "---------------------------------"
    exit 0
}


create_negative()
{
    counter_sec=0
    counter_min=0
    for i in `seq 1 $1`; do
        #ffmpeg was no installed on the machine i run this so i run the binary directly
        #./ffmpeg-3.3.3-64bit-static/ffmpeg -i angry-birds.mp4 -ss 00:$counter_min:$counter_sec.00 -vframes 1 negatives-$i.png
        ffmpeg -i $2 -ss 00:$counter_min:$counter_sec.00 -vframes 1 negatives-$i.png
        ((counter_sec++))
        ((counter_sec++))
        if [ $counter_sec -ge 59 ]; then
            ((counter_min++))
        fi
        counter_sec=$((counter_sec%60))
    done
}


main()
{
    #handle user input
    if [ "$1" == "--help" ] || [ -z ${1+x} ] || [ "$1" == "-h" ]; then
        usage $0
    fi
    while [[ $# -gt 1 ]]
    do
        key="$1"

        case $key in
            -n|--count)
                amt="$2"
                shift
                ;;
            -m|--movie)
                movie="$2"
                shift
                ;;
                *)
                        # unknown option
                ;;
        esac
        shift # past argument or value
    done
    if [ -z ${movie+x} ]; then
        echo "You must specify the source of the images."
        exit -1
    fi
    if [ -z ${amt+x} ]; then
        amt=5
        create_negative ${amt} ${movie}
    else
        create_negative ${amt} ${movie}
    fi


}
main "$@"
