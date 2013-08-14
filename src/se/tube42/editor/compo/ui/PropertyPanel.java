package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;

public class PropertyPanel 
extends Panel 
implements ActionListener, ItemListener, KeyListener
{    
    private static final String [] FIELD_NAMES = {
        "X0", "Y0", "X1", "Y1", "Flags" 
    };        
    
    private MainWindow mw;
    private Panel p;
    private TextField [] texts;
    private Choice [] types;
    private boolean allow_update;
    private Label msg, name;
    private Button remove, hide;
    
    public PropertyPanel(MainWindow mw)
    {
        this.mw = mw;
        this.texts = new TextField[5];
        this.types = new Choice[4];
        
        setLayout(new BorderLayout());        
        add(name = new Label("Properties", Label.CENTER), BorderLayout.NORTH);
        add(new FilePanel(mw), BorderLayout.SOUTH);
    
        this.p = new Panel(new StackLayout(4, 8) );
        add(p, BorderLayout.CENTER);
                
        p.add(msg = new Label("", Label.CENTER));
        msg.setForeground(Color.RED);
        
        for(int i = 0; i < FIELD_NAMES.length; i++) {            
            final Label l1 = new Label(FIELD_NAMES[i], Label.CENTER);
            l1.setForeground(Color.RED);
            l1.setBackground(Color.LIGHT_GRAY);
            p.add(l1);
            
            Panel p2 = new Panel(new GridLayout(2, 2, 4, 4));
            p2.add(new Label("Value", Label.RIGHT));
            p2.add(texts[i] = new TextField("0", 20));
            if(i < 4) {
                p2.add(new Label("Type", Label.RIGHT));
                p2.add( types[i] = new Choice());                
                for(int j = 0; j < Database.TYPES.length; j++) 
                    types[i].add(Database.TYPES[j]);
                types[i].select(0);
            } else {
                p2.add(new Label(""));
                p2.add(new Label(""));
            }
            p.add(p2);            
        }
        
        final Label l1 = new Label("Control", Label.CENTER);
        l1.setForeground(Color.RED);
        l1.setBackground(Color.LIGHT_GRAY);
        p.add(l1);
        
        Panel p0 = new Panel(new GridLayout(1, 2, 12, 12));
        p.add(p0);
        p0.add(remove = new Button("Remove"));
        p0.add(hide = new Button("Hide"));
        
        regionChanged();
        
        // for(TextField tf : texts) tf.addTextListener(this);
        for(TextField tf : texts) tf.addKeyListener(this);
        for(Choice c : types) c.addItemListener(this);
        this.allow_update = true;
        
        remove.addActionListener(this);
        hide.addActionListener(this);
    }
    
    // 
    public void actionPerformed(ActionEvent e)
    {
        if(Database.current_region == null) return;
        
        
        final Object src = e.getSource();        
                
        if(src == hide) {
            final int len = Math.min(32, Database.regions.size());
            for(int i = 0; i < len ; i++) {
                if(Database.regions.get(i).equals(Database.current_region))
                   Database.regions_hidden ^= (1 << i);
            }
        } else if(src == remove) {
            Database.regions.remove(Database.current_region);
            Database.current_region = 
                  Database.regions.size() > 0 ? 
                  Database.regions.get(0) : null;
        }
        
        mw.everythingChanged();
    }
    
    
    public void itemStateChanged(ItemEvent e)
    {
        dataChanged();        
    }
    
    public void keyTyped(KeyEvent e) { }
    public void keyPressed(KeyEvent e) { }
    
    public void keyReleased(KeyEvent e)
    {
        dataChanged();
    }

    //
    public void regionChanged()
    {   
        if(Database.current_region == null) {
            p.setEnabled(false);
            msg.setText("(no region selected)");
            name.setText("Properties");
            return;
        }
        
        msg.setText("");
        p.setEnabled(true);        
        copy_data(true);
        
        name.setText("Properties for " + Database.current_region);
    }
    
    public void dataChanged()
    {
        if(!allow_update) return;
        
        try {
            allow_update = false; // to avoid additional update events...
            
            if(copy_data(false)) {
                msg.setText("");
                mw.propertyChanged();            
            } else {
                msg.setText("ERROR!");                
            }
        } finally {
            allow_update = true;
        }
    }
    
    private boolean copy_data(boolean from_ui)
    {
        try {
        
            final Format f = Database.current_format;
            final String rname = Database.current_region;
            
            if(f == null || rname == null) return false;
            final RegionData rd = f.getRegion(rname);
            
            if(from_ui) {
                for(int i = 0; i < 4; i++) {
                    texts[i].setText("" + rd.values[i]);
                    types[i].select(rd.types[i]);
                }
                texts[4].setText("" + rd.flags);
            } else {
                for(int i = 0; i < 4; i++) {
                    rd.values[i] = Integer.parseInt(texts[i].getText());
                    rd.types[i] = types[i].getSelectedIndex();
                }
                rd.flags = Integer.parseInt(texts[4].getText());
            }        
        } catch(Exception e) {
            return false;
        }
        return true;
    } 
    
    
}