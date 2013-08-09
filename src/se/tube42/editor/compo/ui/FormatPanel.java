package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;

public class FormatPanel extends Panel implements ActionListener
{
    private MainWindow mw;    
    private List format_list;
    private Button toggle_button, use_button;
    
    public FormatPanel(MainWindow mw)
    {
        this.mw = mw;
        
        setLayout(new BorderLayout());        
        add(new Label("Formats", Label.CENTER), BorderLayout.NORTH);
                
        format_list = new List();
        format_list.setFont( new Font(Font.MONOSPACED, Font.PLAIN, 14));
        add(format_list, BorderLayout.CENTER);
        update_list();        
        
        Panel p1 = new Panel(new GridLayout(1, 2, 12, 12));
        add(p1, BorderLayout.SOUTH);
        p1.add(toggle_button = new Button("Toggle"));
        p1.add(use_button = new Button("Use"));                
        
        toggle_button.addActionListener(this);
        use_button.addActionListener(this);
        
        // make sure we have something to start with
        mw.setFormat(Database.FORMATS[0]);        
    }
    
    private void update_list()
    {
        format_list.removeAll();
        
        for(final Format f : Database.FORMATS) {   
            final String str = String.format("[%c] %10s %d*%d", f.enabled ? 'X' : ' ', f.name, f.w, f.h);
            format_list.add(str);
        }        
    }
    
    public void actionPerformed(ActionEvent e)
    {
        final Object src = e.getSource();
        final int n = format_list.getSelectedIndex();
        
        if(src == toggle_button) {
            if(n != -1) {
                Database.FORMATS[n].enabled ^= true;
                update_list();
            }
        } else if(src == use_button) {
            if(n != -1) {
                mw.setFormat(Database.FORMATS[n]);
            }            
        }
    }
}
