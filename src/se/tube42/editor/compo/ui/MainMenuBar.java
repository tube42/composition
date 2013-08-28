package se.tube42.editor.compo.ui;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

import se.tube42.lib.compo.*;
import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.io.*;
import se.tube42.editor.compo.service.*;

public class MainMenuBar
extends MenuBar
implements ActionListener
{    
    private MainWindow mw;
    
    private Menu mformat, mregion;
    private MenuItem new_, save, save_as, load, exit;
    private MenuItem fhide, fclear;
    private MenuItem eremove, ehide, ecenter_v, ecenter_h;
    private MenuItem rrun;
    
    public MainMenuBar(MainWindow mw)
    {
        this.mw = mw;
        
        Menu m0 = add_menu("File");
        new_ = add_item(m0, "New");
        m0.addSeparator();
        load = add_item(m0, "Load ...");
        save = add_item(m0, "Save");
        save_as = add_item(m0, "Save as ...");        
        m0.addSeparator();        
        exit = add_item(m0, "Exit");        
        
        
        mformat = add_menu("Format");
        fhide = add_item(mformat, "Show/hide");
        fclear = add_item(mformat, "Clear");
        
        mregion = add_menu("Region");
        
        ehide = add_item(mregion, "Show/hide");
        mregion.addSeparator();                
        eremove = add_item(mregion, "Remove region");
        mregion.addSeparator();                
        
        ecenter_h = add_item(mregion, "Horizontal center");
        ecenter_v = add_item(mregion, "Vertical center");
        
        
        Menu m1 = add_menu("Run");               
        rrun = add_item(m1, "Run");
                        
        stateChanged();
    }
    
    private Menu add_menu(String name)
    {
        Menu m = new Menu(name);
        add(m);
        return m;
    }
    
    private MenuItem add_item(Menu m, String name)
    {
        MenuItem mi = new MenuItem(name);
        m.add(mi);
        mi.addActionListener(this);
        return mi;
    }
    
    // 
    public void actionPerformed(ActionEvent e)
    {
        final Object src = e.getSource();        
        
        
        if(src == new_) {
            ServiceProvider.newDocument();
        } else if(src == save_as) {
            String filename = Database.filename;
            if(filename == null)
                filename = get_filename(true);            
            ServiceProvider.save(filename);
        } else if(src == save) {
            ServiceProvider.save(Database.filename);     
        } else if(src == load) {
            String filename = get_filename(false);                        
            if(filename != null)
                ServiceProvider.load(filename);                
        } else if(src == exit) {
            System.exit(0);
        } else if(src == fhide) {
            ServiceProvider.toggleCurrentFormat();                        
        } else if(src == fclear) {
            ServiceProvider.clearCurrentFormat();                        
        } else if(src == ehide) {
            ServiceProvider.toggleCurrentRegion();            
        } else if(src == eremove) {
            ServiceProvider.removeCurrentRegion();
        } else if(src == ecenter_v) {
            ServiceProvider.centerCurrentRegion(false, true);
        } else if(src == ecenter_h) {
            ServiceProvider.centerCurrentRegion(true, false);            
        } else if(src == rrun) {
            ServiceProvider.run();
        }        
        
        mw.everythingChanged();            
    }
    
    // 
    public void stateChanged()
    {
        save.setEnabled( Database.filename != null);        
        mformat.setEnabled(Database.current_format != null);
        mregion.setEnabled(Database.current_region != null);
    }
    
    
    private String get_filename(boolean save)
    {
        FileDialog fd = new FileDialog(mw, 
                  save ? "Save as..." :  "Load ...",
                  save ? FileDialog.SAVE : FileDialog.LOAD);
        fd.show();
        return fd.getDirectory() + File.separator + fd.getFile();
    }        
}
