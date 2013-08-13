package se.tube42.editor.compo;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.ui.*;

public class MainWindow extends Frame
{
    private EditWindow ew;
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
        
        setSize(600, 600);
        setVisible(true);
        setLocation(0, 0);
        addWindowListener(wc);                
        
        ew = new EditWindow(this);        
        ew.setLocation(getWidth(), 0);        
    }
    
    // -----------------------------------
    public void everythingChanged()
    {
        if(fp != null) fp.dataChanged();
        formatChanged();
    }
    
    public void formatChanged()
    {
        if(rp != null) rp.dataChanged();
        regionChanged();
    }
    
    public void regionChanged()
    {
        if(pp != null) pp.regionChanged();
        propertyChanged();        
    }
    
    public void propertyChanged() 
    {
        if(ew != null) ew.regionChanged();
    }
    
    public void displayChanged()
    {
        propertyChanged();
    }
    
    // -----------------------------------
    private void on_close()
    {
        System.exit(0);        
    }
}
