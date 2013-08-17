package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.service.*;

public class RegionPanel 
extends Panel 
implements ActionListener, ItemListener
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
        region_list.addItemListener(this);
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
        }                        
    }
    
    public void itemStateChanged(ItemEvent e)
    {
        int n = region_list.getSelectedIndex(); // remember what was selected        
        Database.current_region = (n == -1) ? null : Database.regions.get(n);
        mw.regionChanged();
        
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
        region_list.removeAll();
                
        final int len = Database.regions.size();
        for(int i = 0; i < len; i++) {
            final String s = Database.regions.get(i);
            
            final boolean hidden = i < 32 && 
                  (Database.regions_hidden & (1 << i)) != 0;
            
            final String str = String.format(" [%c] %s", 
                      hidden ? ' ' : 'X', s);
            region_list.add(str);
        }        
        
        int n = ServiceProvider.getCurrentRegionIndex();
        region_list.select(n);
    }
}
