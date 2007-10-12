// Groovy source file
import net.snookr.flickr.Flickr;
import net.snookr.flickr.Photos;
import net.snookr.db.Database;
import net.snookr.util.Spawner;
import net.snookr.util.Progress;
import net.snookr.util.MD5;
import net.snookr.model.FlickrImage;

boolean forceUpload=true;
if (forceUpload) { // direct invocation of Flickr method (returning xml)
    Flickr f = new Flickr();
    println f.uploadPhoto(new File("snookr.jpg"));
}

Photos p = new Photos();
File f = new File("snookr.jpg");

int nuPhotoid = p.uploadPhoto(f);
println "new photoid: ${nuPhotoid}";
