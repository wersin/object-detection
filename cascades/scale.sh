#!/bin/bash
#================================================================
# HEADER
#================================================================
#% SYNOPSIS
#+    ${SCRIPT_NAME} [--help] [-w<width>] [-h<heigth>] [-d<direcory>]
#%
#% DESCRIPTION
#%      Scales all the jpg files in specified or current directory
#%      to the resolution sepcified by -w and -h.
#%
#% OPTIONS
#%    -d,--directory      [directory] (optional)
#%    -w, --width         [width of output image]
#%    -h, --height        [height of output image]
#%    --help              Print this help
#%
#% EXAMPLES
#%    ${SCRIPT_NAME} -d my-folder -w 40 -h 80
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
    echo "-d, --directory     [directory] (optional)"
    echo "-w, --width         [width of output image]"
    echo "-h, --height        [height of output image]"
    echo "--help              Print this help"
    echo "example: $1 -d my-folder -w 40 -h 80"
    echo "---------------------------------"
}


scale()
{
    # apply the ffmpeg conversion for every jpg file in current directory
    for i in *.jpg; do
        [ -f "$i" ] || continue
        # conversion with ffmpeg to desired resolution
        # answer yes in case the same file already exists it is overwritten
        yes Y | ffmpeg -i $i -vf scale=$1:$2 $i
    done
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
            -d|--directory)
                DIR="$2"
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
    if [ -z ${width+x} ] || [ -z ${height+x}]; then
        echo "You must set width (-w) and height (-h)."
        exit -1
    fi
    if [ -z ${DIR+x} ]; then
        scale ${width} ${height}
    else
        #if set go to direcgtory
        cd ${DIR}
        scale ${width} ${height}
    fi
}
main "$@"
