#!/bin/bash
#================================================================
# HEADER
#================================================================
#% SYNOPSIS
#+    ${SCRIPT_NAME} [-h] [-id<identification>] [-d<direcory>] [-f<filetype>]
#%
#% DESCRIPTION
#%    Renames every file in specified directory to 'traffic-sign-<id>.jpg'.
#%    This script just helps to keep track of all the positive files for the
#%    different signs.
#%      
#% OPTIONS
#%    -d,--directory      [directory] (optional)
#%    -f, --file-type     [file extension]
#%    -id                 [identification of files]
#%    -h, --help          Print this help
#%
#% EXAMPLES
#%    ${SCRIPT_NAME} -id 120 -f jpg -d my-folder
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
    echo "-f, --file-type     [file extension]"
    echo "-id                 [identification of files]"
    echo "-h, --help          Print this help"
    echo "example: $1 -id 120 -f jpg -d my-folder"
    echo "---------------------------------"
}

rename_func()
{
    name_id="-$1-"
    counter=0
    #name="$folder/traffic-sign-"
    name="traffic-sign"
    for i in *.$2; do
        [ -f "$i" ] || break
        name_file="$name$name_id$counter.jpg"
        mv $i $name_file
        ((counter++))
    done
}

main()
{
    if [ "$1" == "-h" ] || [ -z ${1+x} ] || [ "$1" == "--help" ]; then
        usage $0
        exit 0
    fi
    while [[ $# -gt 1 ]]
    do
        key="$1"

        case $key in
            -f|--file-type)
                extension="$2"
                shift # past argument
            ;;
            -d|--directory)
                DIR="$2"
                shift # past argument
            ;;
            -id)
                id="$2"
            shift # past argument
            ;;
            --default)
            DEFAULT=YES
            ;;
            *)
                    # unknown option we handle 2 per iteration
            ;;
        esac
        shift # past argument or value
    done
    #naviate to directory
    if [ -z ${DIR+x} ]; then
        rename_func ${id} ${extension}
    else
        cd ${DIR}
        rename_func ${id} ${extension}
    fi
}
main "$@"
