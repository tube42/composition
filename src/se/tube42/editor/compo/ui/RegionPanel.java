package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;

public class RegionPanel extends Panel implements ActionListener
{    
    private MainWindow mw;
    private Button region_add;
    private TextField region_name;
    private List region_list;
    
    public RegionPanel(MainWindow mw)
    {
        this.mw = mw;
        
        setLayout(new BorderLayout());        
        add(new Label("Regions", Label.CENTER), BorderLayout.NORTH);
        
        region_list = new List();
        region_list.setFont( UI.LIST_FONT);        
        region_list.addActionListener(this);
        add(region_list, BorderLayout.CENTER);
        
        Panel p1 = new Panel(new FlowLayout(FlowLayout.LEFT));
        add(p1, BorderLayout.SOUTH);
        p1.add(region_name = new TextField("region1", 12));
        p1.add(region_add = new Button("Add"));                        
        region_add.addActionListener(this);
        
        update_list();
    }
    
    public void dataChanged()
    {
        update_list();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        final Object src = e.getSource();
        if(src == region_add) {           
            final String name = region_name.getText();
            region_add(name);
        } else if(src == region_list) {  
            final int n = region_list.getSelectedIndex();
            select_region(n);
        }                        
    }
    
    private void region_add(String name)
    {
        if(name.length() == 0 || Database.regions.contains(name))
            return;        
        Database.current_region = name;
        Database.regions.add(name);
        update_list();        
        mw.regionChanged();       
    }
    
    private void select_region(int index)
    {
        if(index < 0 || index >= Database.regions.size())
            return;
        
        Database.current_region = Database.regions.get(index);
        update_list();
        
        mw.regionChanged();
    }
    
    private void update_list()
    {
        int n = region_list.getSelectedIndex(); // remember what was selected
        region_list.removeAll();
        
        
        
        final int len = Database.regions.size();
        for(int i = 0; i < len; i++) {
            final String s = Database.regions.get(i);
            
            final boolean hidden = i < 32 && 
                  (Database.regions_hidden & (1 << i)) != 0;
            
            final String str = String.format("%5s %c %s", 
                      s.equals(Database.current_region) ? "-->" : "",
                      hidden ? 'H' : ' ',                      
                      s);
            region_list.add(str);
        }        
        
        // select the previously selected one if possible
        if(n >= 0 && n < Database.regions.size())
            region_list.select(n);
    }
}
