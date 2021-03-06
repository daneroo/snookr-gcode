// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LightningDemo.java
package unc.gamma;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class LightningApp extends Applet
        implements AdjustmentListener, MouseListener {

    public LightningApp() {
        resumeLabel = "Grow";
        pauseLabel = "Pause";
        resetLabel = "Reset";
        renderLabel = "Add Glow";
        fft = null;
        eta = 1.0F;
    }

    public void init() {
        Panel panel = new Panel();
        GridLayout gridlayout = new GridLayout(0, 1);
        gridlayout.setHgap(10);
        gridlayout.setVgap(10);
        panel.setLayout(gridlayout);
        panel.setBackground(new Color(100, 100, 100));
        setBackground(new Color(100, 100, 100));
        Label label = new Label("Re-Simulation");
        label.setFont(new Font("Dialong", 3, 24));
        Label label1 = new Label("Controls");
        label1.setFont(new Font("Dialog", 3, 24));
        setFont(new Font("TimesRoman", 0, 12));
        Panel panel1 = new Panel();
        panel1.setLayout(new GridLayout(0, 1));
        panel1.add(label);
        Panel panel2 = new Panel();
        panel2.setLayout(new GridLayout(1, 2));
        panel2.add(new Label("Resolution"));
        Choice choice = new Choice();
        choice.addItem("64");
        choice.addItem("128");
        choice.addItem("256");
        panel2.add(choice);
        Panel panel3 = new Panel();
        panel3.setLayout(new GridLayout(0, 1));
        panel3.add(new Button(resumeLabel));
        panel3.add(new Button(pauseLabel));
        panel3.add(new Button(resetLabel));
        Panel panel4 = new Panel();
        GridLayout gridlayout1 = new GridLayout(0, 1);
        gridlayout1.setVgap(5);
        gridlayout1.setHgap(5);
        panel4.setLayout(gridlayout1);
        panel4.setBackground(new Color(128, 128, 128));
        panel4.add(panel1);
        panel4.add(panel2);
        panel4.add(new Button(resumeLabel));
        panel4.add(new Button(pauseLabel));
        panel4.add(new Button(resetLabel));
        panel.add(panel4);
        lightningRGB = new int[3];
        lightningRGB[0] = 128;
        lightningRGB[1] = 45;
        lightningRGB[2] = 45;
        Label label2 = new Label("Rendering");
        label2.setFont(new Font("Dialog", 3, 24));
        Label label3 = new Label("Controls");
        label3.setFont(new Font("Dialog", 3, 24));
        Panel panel5 = new Panel();
        panel5.setLayout(new GridLayout(0, 1));
        panel5.add(label2);
        Panel panel6 = new Panel();
        panel6.setLayout(new GridLayout(3, 2));
        redScroll = new Scrollbar(0, lightningRGB[0], 1, 0, 256);
        redLabel = new Label("Red");
        redLabel.setFont(new Font("TimesRoman", 0, 12));
        panel6.add(redLabel);
        panel6.add(redScroll);
        redScroll.addAdjustmentListener(this);
        greenScroll = new Scrollbar(0, lightningRGB[1], 1, 0, 256);
        greenLabel = new Label("Green");
        greenLabel.setFont(new Font("TimesRoman", 0, 12));
        panel6.add(greenLabel);
        panel6.add(greenScroll);
        greenScroll.addAdjustmentListener(this);
        blueScroll = new Scrollbar(0, lightningRGB[2], 1, 0, 256);
        blueLabel = new Label("Blue");
        blueLabel.setFont(new Font("TimesRoman", 0, 12));
        panel6.add(blueLabel);
        panel6.add(blueScroll);
        blueScroll.addAdjustmentListener(this);
        Panel panel7 = new Panel();
        panel7.setLayout(new GridLayout(0, 1));
        panel7.setBackground(new Color(128, 128, 128));
        panel7.add(panel5);
        panel7.add(panel6);
        panel7.add(new Button(renderLabel));
        addMouseListener(this);
        panel.add(panel7);
        Panel panel8 = new Panel();
        panel8.setLayout(new GridLayout(0, 1));
        panel8.setBackground(new Color(128, 128, 128));
        Panel panel9 = new Panel();
        panel9.setLayout(new GridLayout(0, 1));
        Label label4 = new Label("Advanced");
        label4.setFont(new Font("Dialog", 3, 24));
        panel9.add(label4);
        panel8.add(panel9);
        Panel panel10 = new Panel();
        panel10.setLayout(new GridLayout(1, 2));
        panel10.add(new Label("Add Charge"));
        Choice choice1 = new Choice();
        choice1.addItem("None");
        choice1.addItem("Attractor");
        panel10.add(choice1);
        panel8.add(panel10);
        Panel panel11 = new Panel();
        panel11.setLayout(new GridLayout(0, 1));
        panel11.add(new Label("Select type of charge from"));
        panel11.add(new Label("dropdown, and click in window"));
        panel11.add(new Label("with mouse."));
        panel8.add(panel11);
        panel.add(panel8);
        backBuffer = new BackBuffer();
        backBuffer.setSize(512, 512);
        setLayout(new BorderLayout());
        add("East", panel);
        add("Center", backBuffer);
        statusBox = new TextArea(4, 10);
        add("South", statusBox);
        statusBox.append("Press 'Grow' to begin simulation.\n");
        backBuffer.init();
        drawThread = new Thread(backBuffer);
        drawThread.setPriority(1);
        drawThread.start();
        lightning = new Lightning(64, 64, 1.0F, backBuffer, statusBox);
        simThread = new Thread(lightning);
        simThread.setPriority(1);
        simThread.start();
        backBuffer.setLightning(lightning);
    }

    public void destroy() {
        removeMouseListener(this);
    }

    public boolean action(Event event, Object obj) {
        if (event.target instanceof Button) {
            if (((String) obj).equals(pauseLabel)) {
                statusBox.append("Simulation paused.\n");
                lightning.pauseFlag = true;
            } else if (((String) obj).equals(resumeLabel)) {
                statusBox.append("Simulation running.\n");
                lightning.pauseFlag = false;
                lightning.wake();
            } else if (((String) obj).equals(resetLabel)) {
                int i = lightning.xRes();
                statusBox.append("Resetting ... ");
                lightning.init(i, i, eta);
                statusBox.append("Finished.\n");
                lightning.pauseFlag = true;
                lightning.drawTree();
                repaint();
            } else if (((String) obj).equals(renderLabel)) {
                if (fft == null) {
                    statusBox.append("Retrieving glow filter (takes a few seconds) ... ");
                    readFilter();
                }
                statusBox.append("Applying glow (takes a few seconds) ... ");
                BufferedImage bufferedimage = lightning.drawTree();
                if (bufferedimage == null) {
                    System.err.println("drawTree return null BufferdImage");
                    return true;
                }
                System.out.println("render: bi is " + ((bufferedimage == null) ? "null" : "not null"));
                System.out.println("render: bi.g is " + ((bufferedimage.getGraphics() == null) ? "null" : "not null"));

                BufferedImage bufferedimage1 = fft.convolve2D(bufferedimage, lightningRGB);

                System.out.println("render: bi1 is " + ((bufferedimage1 == null) ? "null" : "not null"));
                System.out.println("render: bi1.g is " + ((bufferedimage1.getGraphics() == null) ? "null" : "not null"));


                backBuffer.backBuffer = bufferedimage1;
                backBuffer.backBufferContext = bufferedimage1.getGraphics();
                statusBox.append("Done.\n");
                statusBox.append("The lightning color can be tweaked using the RGB sliders.\n");
                repaint();
            }
            return true;
        }
        if (event.target instanceof Choice) {
            if (((String) obj).equals("Repulsor")) {
                lightning.chargeType = 0;
                return true;
            }
            if (((String) obj).equals("Attractor")) {
                lightning.chargeType = 1;
                return true;
            }
            if (((String) obj).equals("None")) {
                lightning.chargeType = -1;
                return true;
            }
            int j = Integer.parseInt((String) obj);
            if (j == lightning.xRes()) {
                return true;
            }
            if (j > 128) {
                statusBox.append("WARNING: Resolutions above 128 can take a very long time to simulate!\n");
                statusBox.append("         However, the visual quality of the lightning improves considerably.\n");
            }
            lightning.init(j, j, eta);
            statusBox.append("Resolution changed.\n");
            lightning.drawGridAndTree();
            backBuffer.pauseFlag = false;
            backBuffer.wake();
            repaint();
        }
        return false;
    }

    public void adjustmentValueChanged(AdjustmentEvent adjustmentevent) {
        int i = redScroll.getValue();
        int j = greenScroll.getValue();
        int k = blueScroll.getValue();
        lightningRGB[0] = i;
        lightningRGB[1] = j;
        lightningRGB[2] = k;
        if (fft == null) {
            return;
        }
        BufferedImage bufferedimage = fft.relight(lightningRGB);
        //System.out.println("adj: bi is "+((bufferedimage==null)?"null":"not null"));
        //System.out.println("adj: bi.g is "+((bufferedimage.getGraphics()==null)?"null":"not null"));

        if (bufferedimage != null) {
            backBuffer.backBuffer = bufferedimage;
            backBuffer.backBufferContext = bufferedimage.getGraphics();
        }
        repaint();
    }

    public void readFilter() {
        // use new jdk 1,4 method for loading images !
        Image image = null;
        try {
            String imPath = "apsfShift.jpg";

            // Read from a file
            File file = new File(imPath);
            image = ImageIO.read(file);

        /*
        // Read from an input stream
        InputStream is = new BufferedInputStream(
        new FileInputStream(imPath));
        image = ImageIO.read(is);
        
        // Read from a URL
        //URL url = new URL("http://hostname.com/image.gif");
        URL url = file.toURL();
        image = ImageIO.read(url);
         */
        } catch (IOException e) {
            e.printStackTrace();
        }


        BufferedImage bufferedimage = toBufferedImage(image);
        fft = new FFT(bufferedimage);
    }

    public static BufferedImage toBufferedImage(Image image) {
        BufferedImage bufferedimage = new BufferedImage(512, 512, 1);
        java.awt.Graphics2D graphics2d = bufferedimage.createGraphics();
        graphics2d.drawImage(image, 0, 0, null);
        return bufferedimage;
    }

    public void mouseClicked(MouseEvent mouseevent) {
    }

    public void mouseReleased(MouseEvent mouseevent) {
    }

    public void mousePressed(MouseEvent mouseevent) {
    }

    public void mouseExited(MouseEvent mouseevent) {
    }

    public void mouseEntered(MouseEvent mouseevent) {
    }
    Thread drawThread;
    Thread simThread;
    BackBuffer backBuffer;
    Lightning lightning;
    int lightningRGB[];
    String resumeLabel;
    String pauseLabel;
    String resetLabel;
    String renderLabel;
    TextArea statusBox;
    Label etaValue;
    Label redLabel;
    Label greenLabel;
    Label blueLabel;
    Scrollbar etaScroll;
    Scrollbar redScroll;
    Scrollbar blueScroll;
    Scrollbar greenScroll;
    FFT fft;
    float eta;

    public static void main(String args[]) {
        //final LightningApp app = new LightningApp();
        final LightningApp app = new LightningApp();
        app.init();
        app.start();
        Frame f = new Frame("Title");
        f.addWindowListener(
                new WindowAdapter() {

                    public void windowClosing(WindowEvent e) {
                        app.stop();
                        app.destroy();
                        System.exit(0);
                    }
                });
        f.setSize(700, 620);
        f.add("Center", app);
        f.show();
    }
}
