package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.service.*;

public class FormatPanel 
extends Panel 
implements ItemListener, ActionListener
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
        format_list.addItemListener(this);
        format_list.addActionListener(this);
        add(format_list, BorderLayout.CENTER);
        update_list();                               
        
        // make sure we have something to start with
        Database.current_format = Database.FORMATS[0];
        Database.current_format.enabled = true;
        format_list.select(0);
        dataChanged();
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
            final String str = String.format(" [%c] %10s %d*%d", 
                      f.enabled ? 'X' : ' ', f.name, f.w, f.h);
            format_list.add(str);
        }        
        
        
        // somehow we have selected a different format
        if(n < 0 || n >= Database.FORMATS.length || Database.FORMATS[n] != Database.current_format) {
            n = ServiceProvider.getCurrentFormatIndex();
        }        
        
        // select the previously selected one if possible
        format_list.select(n);
    }
    
    public void itemStateChanged(ItemEvent e)
    {
        final int n = format_list.getSelectedIndex(); // remember what was selected
        Database.current_format = (n == -1) ? null : Database.FORMATS[n];
        mw.formatChanged();        
    }
    
    public void actionPerformed(ActionEvent e)
    {
        final Object src = e.getSource();                
        
        if(src == format_list) {
            ServiceProvider.toggleCurrentFormat();                        
            mw.everythingChanged();                    
        }
    }    

}
