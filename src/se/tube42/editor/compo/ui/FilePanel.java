package se.tube42.editor.compo.ui;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

import se.tube42.lib.compo.*;
import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.io.*;

public class FilePanel 
extends Panel 
implements ActionListener
{    
    private MainWindow mw;
    private Button save, test;
    
    public FilePanel(MainWindow mw)
    {
        super(new FlowLayout(FlowLayout.LEFT));
        this.mw = mw;
        
        add(save = new Button("Save"));
        add(test = new Button("Test"));
        
        save.addActionListener(this);
        test.addActionListener(this);
    }
    
    // 
    public void actionPerformed(ActionEvent e)
    {
        final Object src = e.getSource();        
        if(src == save) {
            String filename = Database.filename;
            if(filename == null)
                filename = get_filename();
            
            if(save(filename)) 
                Database.filename = filename;
            
        } else if(src == test) {
            test(Database.filename);
        }        
    }
    
    // 
    private void update()
    {
        test.setEnabled( Database.filename != null);
    }
    
    
    private String get_filename()
    {
        FileDialog fd = new FileDialog(mw, "Save as...", FileDialog.SAVE);
        fd.show();
        return fd.getFile();
    }
    
    private boolean save(String filename)
    {
        try {
            OutputStream os = new FileOutputStream(filename);
            
            CompositionWriter cw = new CompositionWriter(os);
            cw.write();
            os.flush();
        } catch(Exception e) {
            System.out.println("SAVE ERROR: " + e);
            return false;
        }
        return true;
    }
    
    private void test(String filename)
    {
        
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();            
            CompositionWriter cw = new CompositionWriter(os);
            cw.write();
            
            
            byte [] data = os.toByteArray();
            System.out.println("OUTPUT SIZE = " + data.length);
            
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            
            Composition c = Composition.load(is);
            
            if(c != null) {
                new TestWindow(c);
            }
            
        } catch(Exception e) {
            System.err.println("ERROR: " + e);
        }            
    }
    
}
