package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.service.*;

public class FormatPanel extends Panel implements ActionListener
{
    private MainWindow mw;    
    private List format_list;
    
    public FormatPanel(MainWindow mw)
    {
        super(new FlowLayout(FlowLayout.LEFT));
        
        this.mw = mw;
        
        setLayout(new BorderLayout());        
        add(new Label("Formats", Label.CENTER), BorderLayout.NORTH);
                
        format_list = new List();
        format_list.setFont( UI.LIST_FONT);
        format_list.addActionListener(this);
        add(format_list, BorderLayout.CENTER);
        update_list();                               
        
        // make sure we have something to start with
        Database.FORMATS[0].enabled = true;
        select_format(0);
    }
    
    public void dataChanged()
    {
        update_list();
    }
    
    private void update_list()
    {
        int n = format_list.getSelectedIndex(); // remember what was selected
        format_list.removeAll();
        
        for(final Format f : Database.FORMATS) {
            final String str = String.format("%5s [%c] %10s %d*%d", 
                      f == Database.current_format ? "-->" : "",
                      f.enabled ? 'X' : ' ', f.name, f.w, f.h);
            format_list.add(str);
        }        
        
        // select the previously selected one if possible
        if(n >= 0 && n < Database.FORMATS.length)
            format_list.select(n);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        final Object src = e.getSource();
        final int n = format_list.getSelectedIndex();
        
        if(src == format_list)
            select_format(n);       
    }
    
    private void select_format(int index)
    {
        if(index >= 0 && index < Database.FORMATS.length) {
            Database.current_format = Database.FORMATS[index];
            mw.formatChanged();
            update_list();            
        }
    }    
}
