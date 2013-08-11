package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;


public class EditFormat
extends Panel
implements ItemListener, TextListener
{
    private MainWindow mw;
    private TextField grid_edit;
    private Checkbox grid_show, grid_enable;
    
    public EditFormat(MainWindow mw)
    {
        this.mw = mw;  
        
        Label l = new Label("Grid:");
        l.setForeground(Color.RED);
        add(l);
        
        add(new Label("size"));
        add(grid_edit = new TextField("" + Database.grid_size, 6));
        add(grid_enable = new Checkbox("snap", Database.grid_enable));
        add(grid_show = new Checkbox("show", Database.grid_show));        
        
        grid_edit.addTextListener(this);
        grid_enable.addItemListener(this);
        grid_show.addItemListener(this);
    }
    
    public void itemStateChanged(ItemEvent ie)
    {
        Database.grid_enable = grid_enable.getState();        
        Database.grid_show = grid_show.getState();        
        mw.displayChanged();
    }
    
    public void textValueChanged(TextEvent te)
    {
        try {
            Database.grid_size = Math.max(4, Integer.parseInt(grid_edit.getText()));
            mw.displayChanged();
        } catch(Exception e) { 
        }
    }
    
}
