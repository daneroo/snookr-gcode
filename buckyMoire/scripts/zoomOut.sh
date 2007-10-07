#!/bin/sh
# ------------------------------------------------------------------
# Title: FullerMoire 
# Description:
#  Generate Moire Patter From two BuckminsterFuller Domes
# Author: Daniel Lauzon
# Date:   2005-11-17
# ------------------------------------------------------------------
# Default values
#
outerFrequency=45;
innerFrequency=33;
outerSphereRadius=0.01;
innerSphereRadius=0.01;
outerCylinderRadius=0.01;
innerCylinderRadius=0.01;
# textures T_Chrome_4A T_Chrome_5A T_Silver_1A
outerTexture=T_Chrome_4A;
innerTexture=T_Chrome_4A;
outerDomeRadius=1.015;
innerDomeRadius=1.000;
width=640;
height=480;
numFrames=72;


DomeExec=./dome_4_80/dome
POVHome=./povray-3.6
POVExec=$POVHome/povray
workDir=work

function logStderr { #echo to stderr.
    echo $1 1>&2
}

function makeWorkDir { 
  mkdir -p $workDir
}

function makeDomeInc { 
    freq=$1;
    sphRadius=$2; 
    cylRadius=$3;

    logStderr "makeDome: $freq $sphRadius $cylRadius";
    $DomeExec -f$freq -sb deleteme.pov
    cat dome.inc| sed -e "s/0.015}$/$sphRadius}/g" | sed -e "s/0.01 }$/$cylRadius }/g" |sed -e "s/DomePoints/DomePoints$freq/g"|sed -e "s/DomeEdges/DomeEdges$freq/g" > $workDir/dome$freq.inc;
    rm deleteme.pov;

}

function makePov { 
cat >$workDir/fullerMoire${normalizedBaseName}.pov <<EOPOV
//POV-Ray script --- generated by DOME 4.80
//POV-Ray script --- modified by fullerMoire
#include"colors.inc"
#include"shapes.inc"
#include"textures.inc"
#include"metals.inc"
#include"glass.inc"
#include"dome$outerFrequency.inc"
#include"dome$innerFrequency.inc"
#declare Cam_factor = .99 + clock*clock*0.201;
#declare Camera_X = 1 * Cam_factor;
#declare Camera_Y = 0.04 * Cam_factor;
#declare Camera_Z = 0.1 * Cam_factor;
camera { location  <Camera_X, Camera_Y, Camera_Z>
         sky <0,0,1>
         look_at   <0, 0, 0> }

object { light_source { <Camera_X + 1, Camera_Y + 1 , Camera_Z + 1> color White } }

object { sphere { <0, 0, 0>,50000 }
       hollow
       texture {
               Blue_Sky3
               scale <80000, 50000, 30000>
               finish { crand .05 ambient .7 }
       }
}

#declare InnerDome=
object {
  union {
     object {DomePoints$innerFrequency no_shadow  } 
     object {DomeEdges$innerFrequency no_shadow } 
  }
}

#declare OuterDome=
object {
  union {
    object {DomePoints$outerFrequency no_shadow  } 
    object {DomeEdges$outerFrequency no_shadow } 
  }
}

#declare T_G01 = texture { pigment { color Clear } finish { F_Glass1 } }

object{ InnerDome 
  scale <$innerDomeRadius,$innerDomeRadius,$innerDomeRadius> 
  rotate <0, 0, clock*72> 
  rotate <0, 60, 0> 
  rotate <0, 0, 23.5> 
//  texture{ $innerTexture }  
  texture{ 
    NBglass
    //pigment { rgbf <0.98, 1.0, 0.99, 0.75> } 
    pigment { rgbf <0.98, 1.0, 0.99, 1.0 - .25*clock> } 
  }  
}
object{ OuterDome 
  rotate <0, 0, 0> 
  scale <$outerDomeRadius,$outerDomeRadius,$outerDomeRadius> 
//  texture{ $outerTexture }
  texture{ 
    NBglass
    pigment { rgbf <0.98, 1.0, 0.99, 1.0 - .25*clock> }
  }  
}

//End Pov Script

EOPOV

}

function thumbAndFirst { # antialiased
    $POVExec +L${POVHome}/include +L${workDir} +I${POVScript} +O${firstImage} +FN +W$width +H$height
    convert -resize 200x200 ${firstImage} ${thumbImage}
}

function generate { 
    $POVExec +L${POVHome}/include +L${workDir} +I${POVScript} +O${genImage} +FN +W$width +H$height +KFI1 +KFF${numFrames} +KI0.0 +KF1.0 +KC

    pattern=`basename ${genImage} .png`
    convert ${workDir}/${pattern}[0-9]*[0-9].png ${genAnim}
}


function usage() {
    logStderr "Usage: `basename $0` outerFreq innerFreq";
# dD inner/outer Dome radius
# fF inner/outer Dome frequency
# sS inner/outer Dome sphere radius
# cC inner/outer Dome cylinder radius
# tT inner/outer Dome texture
#  texture{T_Chrome_4A}
#  texture{T_Chrome_5A}
#  texture{T_Silver_1A}

    exit $E_BADARGS
}

if [ ! -n "$2" ]; then  # $1 has zero length
    usage;
fi  

optsyntax="f:F:s:S:c:C:t:T:w:d:D:w:h:n:"
while getopts $optsyntax flag; do
    case "$flag" in
        F) outerFrequency=$OPTARG;;
        f) innerFrequency=$OPTARG;;
        S) outerSphereRadius=$OPTARG;;
        s) innerSphereRadius=$OPTARG;;
        C) outerCylinderRadius=$OPTARG;;
        c) innerCylinderRadius=$OPTARG;;
        T) outerTexture=$OPTARG;;
        t) innerTexture=$OPTARG;;
        D) outerDomeRadius=$OPTARG;;
        d) innerDomeRadius=$OPTARG;;
        w) width=$OPTARG;;
        h) height=$OPTARG;;
        n) numFrames=$OPTARG;;
	?) usage;;
    esac
done

normalizedBaseName="F${outerFrequency}${innerFrequency}S${outerSphereRadius}${innerSphereRadius}C${outerCylinderRadius}${innerCylinderRadius}T${outerTexture}${innerTexture}D${outerDomeRadius}${innerDomeRadius}";

POVScript=$workDir/fullerMoire$normalizedBaseName.pov
firstImage=$workDir/firstFullerMoire$normalizedBaseName.png
thumbImage=$workDir/thumbFullerMoire$normalizedBaseName.png
genImage=$workDir/fullerMoire${normalizedBaseName}f.png
genAnim=$workDir/fullerMoire${normalizedBaseName}.m2v

echo $normalizedBaseName;

makeWorkDir;
makeDomeInc $outerFrequency $outerSphereRadius $outerCylinderRadius;
makeDomeInc $innerFrequency $innerSphereRadius $innerCylinderRadius;
makePov;
#thumbAndFirst;
generate;






