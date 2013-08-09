package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;

public class PropertyPanel extends Panel implements ActionListener
{    
    private  MainWindow mw;
    public PropertyPanel(MainWindow mw)
    {
        this.mw = mw;
        
        setLayout(new BorderLayout());        
        add(new Label("Properties", Label.CENTER), BorderLayout.NORTH);

    }
    
    public void actionPerformed(ActionEvent e)
    {
        final Object src = e.getSource();

    }
}
