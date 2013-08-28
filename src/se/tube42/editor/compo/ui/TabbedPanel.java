package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.service.*;

/**
 * minimal tabbed panel since we like inventing the wheel
 */

public class TabbedPanel 
extends Panel 
implements ActionListener
{    
    private CardLayout layout;
    private String [] names;    
    private Button [] buttons;
    private Panel [] panels;    
    private Panel p1;
    
    
    public TabbedPanel(String [] names)
    {
        super(new BorderLayout(4, 4));
        
        final int n = names.length;
        
        this.names = names;
        this.buttons = new Button[n];
        this.panels = new Panel[n];
        
        Panel p0 = new Panel(new GridLayout( 1, n, 4, 4));
        add(p0, BorderLayout.NORTH);
        
        p1 = new Panel(layout = new CardLayout(4, 4));
        add(p1, BorderLayout.CENTER);
        
        for(int i = 0; i < n; i++) {
            p0.add( buttons[i] = new Button(names[i]));
            p1.add( panels[i] = new Panel(), names[i]);            
            buttons[i].addActionListener(this);
        }        
        
        set(0);
    }
    
   
    public void actionPerformed(ActionEvent e)
    {
        final Button src = (Button) e.getSource();                        
        layout.show(p1, src.getLabel());
    }
    
    
    public Panel [] getPanels()
    {
        return panels;
    }
    
    public void set(int index)
    {
        if(index < 0 || index >= buttons.length)
            return;
        
        layout.show(p1, names[index]);
    }
    
    public void setEnabled(boolean e)
    {
        for(Button b : buttons)
            b.setEnabled(e);
        super.setEnabled(e);
    }
}
