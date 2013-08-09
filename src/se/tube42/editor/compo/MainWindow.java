package se.tube42.editor.compo;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.ui.*;

public class MainWindow extends Frame
{
    private FormatPanel fp;
    private RegionPanel rp;
    private PropertyPanel pp;
    
    public MainWindow()
    {
        super("[Composition]");
        
        setLayout(new GridLayout(1, 3));
        
        add( fp = new FormatPanel(this));
        add( rp = new RegionPanel(this));
        add( pp = new PropertyPanel(this));
        
        // Frame stuff        
        final WindowAdapter wc = new WindowAdapter() {
            public void windowClosing(WindowEvent e) { 
                on_close();
            }
        };
        
        setSize(800, 600);
        setVisible(true);
        addWindowListener(wc);                
    }
    
    // -----------------------------------
    public void setFormat(Format f)
    {
        // TODO
    }
    // -----------------------------------
    private void on_close()
    {
        System.exit(0);        
    }
}
