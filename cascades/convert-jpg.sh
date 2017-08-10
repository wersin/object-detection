#!/bin/bash
#================================================================
# HEADER
#================================================================
#% SYNOPSIS
#+    ${SCRIPT_NAME} 
#%
#% DESCRIPTION
#%    Converts every png file to jpg witgout overriding the original
#%    png file.
#%
#================================================================
#- IMPLEMENTATION
#-    version         ${SCRIPT_NAME} 0.0.1, August 2017
#-    author          Mark Bley
#-    copyright       None
#-    license         WTFPL
#-
#================================================================

main()
{
    for i in *.png ; do convert "$i" "${i%.*}.jpg" ; done
}

main "$@"
