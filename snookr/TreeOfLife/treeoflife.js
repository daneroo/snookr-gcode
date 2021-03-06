var svgns = "http://www.w3.org/2000/svg";

var WIDTH=800;
var HEIGHT=400;

var plants    = 30;
var leafCount = 15;

var centerX = WIDTH/2; // w/2
var offsetX = WIDTH/2 - 30; // w/2 - pad

var svgRoot;
var stems;
var leaves;

function init(e) {
    if ( window.svgDocument == null )
        svgDocument = e.target.ownerDocument;

    stems  = svgDocument.getElementById("stems");
    leaves = svgDocument.getElementById("leaves");

    generate();
}

function generate() {
    // remove stems
    while ( stems.firstChild != null ) {
        stems.removeChild(stems.firstChild);
    }

    // remove leaves
    while ( leaves.firstChild != null ) {
        leaves.removeChild(leaves.firstChild);
    }

    // make new plants
    for ( var i = 0; i < plants; i++ ) {
    //makePlant();
    }

    makeRow(0 ,100,1,1);
    makeRow(30,170,3,.7);
    makeRow(0 ,230,7,.7*.7);
    makeRow(15 ,270,25,.7*.7*.7);
    makeRow(0  ,300,25,.7*.7*.7*.7);
}

var xspacing=90;
var leftMargin=100;
function makeRow(x,y,n,scale) {
    for (var l=0;l<n;l++){
        makeLeaf({
            x: leftMargin+x+l*xspacing*scale,
            y: y
        },    8*scale, 7*scale);
    }
}

function makeLeaf(point, scaleX, scaleY) {
    var gray  = 110 + random(50);
    var color  = "rgb("+[gray,gray,gray].join(",")+")";
    var angle  = random(180) - 180;
    angle=-90;
    var trans  = "translate(" + point.x + "," + point.y + ")";
    var scale  = "scale(" + scaleX + "," + scaleY + ")";
    var rotate = "rotate(" + angle + ")";
    var t      = trans + " " + rotate + " " + scale;
    var leaf   = svgDocument.createElementNS(svgns, "path");

    leaf.setAttributeNS(null, "fill", color);
    leaf.setAttributeNS(null, "stroke", "white");
    leaf.setAttributeNS(null, "transform", t);
    leaf.setAttributeNS(null, "d", "M0,0 Q5,-5 10,0 5,5 0,0z");

    leaves.appendChild(leaf);
}

function makePlant() {
    var points = makePoints();
    var length = points.length;
    var stem   = svgDocument.createElementNS(svgns, "path");
    var values = [];
    var i;
    for ( i = 0; i < length; i++ ) {
        var point = points[i];
        values.push(point.x + "," + point.y);
    }

    stem.setAttributeNS(null, "d", "M" + values.join(" "));
    stems.appendChild(stem);

    for ( i = 0; i < leafCount; i++ ) {
        makeLeaf( points[length-1-i], 1+0.1*i, 1+0.05*i );
    }
}

function makePoints() {
    var x      = centerX + random(2*offsetX) - offsetX;
    var count  = 30 + random(35);
    var dy     = 5;
    var points = [];
    var offset = 0.007;

    var bottom=400;
    points.push(
    {
        x: x,
        y: bottom
    }
    );
    for ( var i = 1; i <= count; i++ ) {
        points.push(
        {
            x: points[i-1].x + i*offset*(random(21)-10),
            y: bottom-dy*i
        }
        );
    }

    return points;
}

function random(max) {
    return Math.round(Math.random()*max);
}
