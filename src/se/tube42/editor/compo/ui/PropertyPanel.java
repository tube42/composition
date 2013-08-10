package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;

public class PropertyPanel 
extends Panel 
implements ActionListener, TextListener, ItemListener
{    
    private static final String [] FIELD_NAMES = {
        "X0", "Y0", "X1", "Y1", "Flags" 
    };        
    
    private MainWindow mw;
    private Panel p;
    private TextField [] texts;
    private Choice [] types;
    private boolean allow_update;
    
    public PropertyPanel(MainWindow mw)
    {
        this.mw = mw;
        this.texts = new TextField[5];
        this.types = new Choice[4];
        
        setLayout(new BorderLayout());        
        add(new Label("Properties", Label.CENTER), BorderLayout.NORTH);
        
        this.p = new Panel(new StackLayout(4, 8) );
        add(p, BorderLayout.CENTER);
        
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
            }
            p.add(p2);            
        }
        
        regionChanged();
        
        for(TextField tf : texts) tf.addTextListener(this);
        for(Choice c : types) c.addItemListener(this);
        this.allow_update = true;
    }
    
    // 
    public void actionPerformed(ActionEvent e)
    {
        final Object src = e.getSource();        
    }
    
    public void textValueChanged(TextEvent e)
    {
        System.out.println("textValueChanged " + e); // DEBUG
        dataChanged();
    }
    
    public void itemStateChanged(ItemEvent e)
    {
        System.out.println("itemStateChanged " + e); // DEBUG
        dataChanged();        
    }
    
    //
    public void regionChanged()
    {   
        if(Database.current_region == null) {
            p.setEnabled(false);
            return;
        }
        
        p.setEnabled(true);        
        copy_data(true);
    }
    
    public void dataChanged()
    {
        if(!allow_update) return;
        
        try {
            allow_update = false; // to avoid additional update events...
            
            copy_data(false);                    
            mw.propertyChanged();
            
        } finally {
            allow_update = true;
        }
    }
    
    private void copy_data(boolean from_ui)
    {
        final Format f = Database.current_format;
        final String rname = Database.current_region;
        
        if(f == null || rname == null) return;
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
        
    }
    
}
