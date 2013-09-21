package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.service.*;


public class PropertyPanel 
extends Panel 
implements ActionListener, ItemListener, KeyListener
{    
    private static final String [] POINT_NAMES = {
        "X0", "Y0", "X1", "Y1"
    };        
    
    private static final String [] TAB_NAMES = {
        "Position", "Anchors", "Misc."
    };
    
    private MainWindow mw;
    private TabbedPanel p1;
    private TextField [] texts;
    private Choice [] targets, types;
    private Choice alignment;
    private Button alignment_force;
    private Label msg, name, dx, dy;
    private String [] target_names;
    
    public PropertyPanel(MainWindow mw)
    {
        this.mw = mw;
        this.texts = new TextField[5];
        this.targets = new Choice[4];
        this.types = new Choice[4];
        
        setLayout(new BorderLayout(8, 8));                
        add(name = new Label("Properties", Label.CENTER), BorderLayout.NORTH);
        add( msg = new Label("", Label.CENTER), BorderLayout.SOUTH);
        msg.setForeground(Color.RED);
        
        p1 = new TabbedPanel(TAB_NAMES);
        add(p1, BorderLayout.CENTER);
        final Panel [] ps = p1.getPanels();
        
        for(Panel p : ps)
            p.setLayout( new StackLayout(4, 4, 2));
        
        
        // position
        add_header(ps[0], "Point 0", "");
        texts[0] = add_edit(ps[0], "X0", "0");
        texts[1] = add_edit(ps[0], "Y0", "0");
        
        add_header(ps[0], "Point 1", "");
        texts[2] = add_edit(ps[0], "X1", "0");
        texts[3] = add_edit(ps[0], "Y1", "0");
        
        add_header(ps[0], "size", "");        
        ps[0].add(new Label("dX", Label.RIGHT));
        ps[0].add(dx = new Label("", Label.LEFT));        
        ps[0].add(new Label("dY", Label.RIGHT));        
        ps[0].add(dy = new Label("", Label.LEFT));
        
        
        // targets        
        add_header(ps[1], "Point 0", "");
        targets[0] = add_choice(ps[1], "X0 anchor");
        types[0] = add_choice(ps[1], "X0 type");        
        targets[1] = add_choice(ps[1], "Y0 anchor");
        types[1] = add_choice(ps[1], "Y0 type");

        add_header(ps[1], "Point 1", "");
        targets[2] = add_choice(ps[1], "X1 anchor");
        types[2] = add_choice(ps[1], "X1 type");        
        targets[3] = add_choice(ps[1], "Y1 anchor");
        types[3] = add_choice(ps[1], "Y1 type");
                
        // misc
        add_header(ps[2], "Region extras", "");
        texts[4] = add_edit(ps[2], "Flags", "0");
                        
        
        add_header(ps[2], "Global parameters", "");
        
        for(Choice type : types) {
            for(int j = 0; j < Database.TYPES.length; j++) 
                type.add(Database.TYPES[j]);
            type.select(0);
        }
        
        alignment = add_choice(ps[2], "Real-time alignment");
        alignment.add("None");
        for(int i = 1; i < 8; i++)
            alignment.add("" + (1 << i) + " pixels");
        
        alignment_force = add_button(ps[2], "Set to RT value", "Editor alignment");
        
        
        alignment.select(Database.global_alignment);
        p1.set(0);        
        regionChanged();        
        
        // for(TextField tf : texts) tf.addTextListener(this);
        for(TextField tf : texts) tf.addKeyListener(this);
        for(Choice c : targets) c.addItemListener(this);
        for(Choice c : types) c.addItemListener(this);
        
        alignment.addItemListener(this);
        alignment_force.addActionListener(this);
    }
    
    // ------------------------------------------------------
    // UI helpers
    
    private Button add_button(Panel p, String label, String text)
    {    
        Button b = new Button(label);
        p.add(new Label(text == null ? "" : text, Label.RIGHT));
        p.add(b);
        return b;
    }
    
    private TextField add_edit(Panel p, String label, String data)
    {    
        TextField tf = new TextField(data, 20);        
        p.add(new Label(label, Label.RIGHT));
        p.add(tf);
        return tf;
    }
    
    private Choice add_choice(Panel p, String label)
    {    
        Choice c = new Choice();
        p.add(new Label(label, Label.RIGHT));
        p.add(c);
        return c;
    }
    
    private Label add_header(Panel p, String t0, String t1)
    {
        Label l0 = new Label(t0, Label.CENTER);
        Label l1 = new Label(t1, Label.CENTER);
        l0.setBackground(Color.GRAY);
        l1.setBackground(Color.GRAY);
        l0.setForeground(Color.BLUE);        
        l1.setForeground(Color.BLUE);
        p.add(l0);
        p.add(l1);
        return l0;
    }    
    
    // ------------------------------------------------------
    public void actionPerformed(ActionEvent e)
    {
        final Object src = e.getSource();                        
        
        if(src == alignment_force) {
            ServiceProvider.alignCurrentFormat();
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
            p1.setEnabled(false);
            msg.setText("(no region selected)");
            name.setText("Properties");
            return;
        }
        
        msg.setText("");
        p1.setEnabled(true);                
        name.setText("Properties for " + Database.current_region);                
    }
    
    public void propertyChanged()
    {
        if(Database.current_region != null)
            copy_data(true);        
    }
    
    public void formatChanged()
    {        
        update_targets();        
        
        // not really a format change, but this is how we detected 
        // load() opetations
        alignment.select(Database.global_alignment);        
    }
    
    // rebuilds target arrays
    private void update_targets()
    {                
        final int len = Database.regions.size();
        target_names = new String[2 + len];
        target_names[0] = "Display";
        target_names[1] = "Template";
        for(int i = 0; i < len; i++)
            target_names[i+2] = Database.regions.get(i);
        
        
        for(Choice c : targets)  {
            c.removeAll();
            for(String s : target_names)
                c.add(s);
        }
    }
    
    
    private void dataChanged()
    {                
        Database.global_alignment = alignment.getSelectedIndex();
        
        if(copy_data(false)) {
            msg.setText("");
            mw.propertyChanged();            
        } else {
            msg.setText("ERROR!");                
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
                    update_if_changed(texts[i], "" + rd.values[i]);
                    update_if_changed(targets[i], rd.targets[i]);
                    update_if_changed(types[i], rd.types[i]);
                }
                update_if_changed(texts[4], "" + rd.flags);
            } else {
                for(int i = 0; i < 4; i++) {
                    rd.values[i] = Integer.parseInt(texts[i].getText());
                    rd.targets[i] = targets[i].getSelectedIndex();
                    rd.types[i] = types[i].getSelectedIndex();                    
                }
                rd.flags = Integer.parseInt(texts[4].getText());
            } 
            
            dx.setText("" + (rd.values[2] - rd.values[0]) );
            dy.setText("" + (rd.values[3] - rd.values[1]) );
            
        } catch(Exception e) {
            return false;
        }
        return true;
    } 
    
    // these will avoid extra update events
    private void update_if_changed(TextField tf, String data)
    {
        if(!data.equals(tf.getText()))
            tf.setText(data);           
    }
    
    private void update_if_changed(Choice c, int n)
    {
        if(c.getSelectedIndex() != n)
            c.select(n);
    }
    
}
