package net.snookr.util;

class Progress {
    long start = new Date().getTime();
    int total
    int sofar=0;
    int showEvery = 100;
    
    String units;
    Progress() { this(-1,"") }
    Progress(int total) { this(total,"") }
    Progress(String units) { this(-1,units) }
    Progress(int total,String units) {
        this.total=total;
        this.units=units;
    }
    Progress(int total,String units,int showEvery) {
        this(total,units);
        this.showEvery=showEvery;
    }
    synchronized void increment(){ 
        sofar++;
        if ( (total>0 && sofar>=total) || (sofar%showEvery)==0 ) {
            show();
        }
    }
    void show() {
        def diff = (new Date().getTime()-start)/1000; // in seconds
        String rate = "";  
        if (diff>0) { 
            rate = "rate: ${sofar/diff}${units}/s";
        }
        String eta = ""; 
        String done = "${sofar}"; 
        if (total>0) {
            eta= "eta: ${diff * (total-sofar) / sofar}s";
            done = "${sofar}/${total}";
        }
        println "Time: ${diff}s ${done} ${rate} ${eta}";
    }
}
