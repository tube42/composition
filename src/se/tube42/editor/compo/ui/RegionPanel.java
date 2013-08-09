package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;

public class RegionPanel extends Panel implements ActionListener
{    
    private MainWindow mw;
    private Button add_region;
    private TextField edit_region;
    
    public RegionPanel(MainWindow mw)
    {
        this.mw = mw;
        
        setLayout(new BorderLayout());        
        add(new Label("Regions", Label.CENTER), BorderLayout.NORTH);
                
        
        Panel p1 = new Panel();
        add(p1, BorderLayout.SOUTH);
        p1.add(edit_region = new TextField("region1", 20));
        p1.add(add_region = new Button("Add"));                        
        add_region.addActionListener(this);
    }
    
    
    public void actionPerformed(ActionEvent e)
    {
        final Object src = e.getSource();

    }
}
