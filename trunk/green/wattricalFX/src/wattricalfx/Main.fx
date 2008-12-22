/*
 * Main.fx
 *
 * Created on Dec 7, 2008, 1:10:20 PM
 */

package wattricalfx;

import java.lang.Long;
import java.lang.Math;
import java.util.Date;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.Reflection;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import wattricalfx.Graph;
import wattricalfx.model.Feed;
import wattricalfx.model.Observation;
import wattricalfx.parser.ObsFeedParser;

/**
 * @author daniel
 */

 //def env = Env{screenWidth: 320, screenHeight: 240};
def env = Env{
    screenWidth: 480,
    screenHeight: 320};

var fakeFeed:Feed = Feed {
    name:"Fake"
    scopeId:-1
    stamp: new Date()
    value:1000;
}

def now =
new Date().getTime();
for (t in [0..300 step 2]) {
    var rnd:Random = new Random();
    var v = ( Math.sin(t  /  300.0 * 4  *  Math.PI) + 1) / 2 * 2000 + rnd.nextInt(200);
    v = t;
    def observation = Observation {
        stamp: new Date(
            now - t * 1000);
        value: v.intValue()
    }
    //println("  - {observation}");
    insert observation into fakeFeed.observations;
}

var graph:Graph =  Graph {
    env:env
    feed: fakeFeed
    /*effect: Reflection {
        fraction: 0.9
        topOpacity: 0.5
        topOffset: 0.3
    }*/
}

var statusText = Text {
    font: Font {
        size: 14
    }
    fill: Color.WHITE
    x: 10,
    y: env.screenHeight - 30
    content: "Status"
}


Stage {
    title: "Wattrical FX"
    width: env.screenWidth
    height: env.screenHeight
    scene: Scene {
        content: [
            graph,
            Text {
                font: Font {
                    size: 24
                }
                x: 100,
                y: 20
                content: "iMetrical - Wattrical"
                fill: Color.WHITE
            },
            statusText,
        ]
        fill: LinearGradient {
            startX: 0.0,
            startY: 0.0,
            endX: 0.0,
            endY: 1.0,
            proportional: true
            stops: [
                Stop {
                    offset: 0.0
                    color: Color.BLACK},
                Stop {
                    offset: 1.0
                    color: Color.GREEN}
            ]
        }
    }
}


var watcher:Watcher = Watcher{
    graph:graph};
watcher.timer.play();

class Watcher {
    var secs:Long;
    var graph:Graph;

    def parser:ObsFeedParser = ObsFeedParser {}

    public var timer : Timeline = Timeline {
        repeatCount: Timeline.INDEFINITE
        keyFrames: KeyFrame {
            time: 2s
            canSkip:true
            action: function() {
                var now:Date = new Date();
                secs=now.getTime();
                parser.parseURL();
                if (parser.parsedFeeds != null) {
                    //println("  LOCAL : {parser.parsedFeeds[0].isoStamp}");
                    //println("  GMT   : {parser.parsedFeeds[0].isoGMT()}");
                    var stamp = parser.parsedFeeds[0].isoStamp; //isoGMT()
                    var latency = (new Date().getTime()-parser.parsedFeeds[0].stamp.getTime())/1000.0;
                    statusText.content = "{stamp} ({-latency}s.)  {parser.parsedFeeds[0].value} W  {parser.parsedFeeds[2].value*24.0/1000} kWh/d";
                    var whichFeed =
                    if ( ((
                    new Date().getSeconds() / 10) mod 2) == 0) 0 else 1;
                    graph.feed = parser.parsedFeeds[whichFeed];
                }
                println("Watcher timeline: {now}");
            }
        },
    };

}