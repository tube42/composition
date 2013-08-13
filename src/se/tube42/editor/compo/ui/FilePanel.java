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
    private Button save, load, test;
    
    public FilePanel(MainWindow mw)
    {
        super(new FlowLayout(FlowLayout.LEFT));
        this.mw = mw;
        
        add(save = new Button("Save ..."));
        add(load = new Button("Load ..."));
        add(test = new Button("Test"));
        
        save.addActionListener(this);
        load.addActionListener(this);
        test.addActionListener(this);
    }
    
    // 
    public void actionPerformed(ActionEvent e)
    {
        final Object src = e.getSource();        
        if(src == save) {
            String filename = Database.filename;
            if(filename == null)
                filename = get_filename(true);
            
            if(save(filename)) 
                Database.filename = filename;
        } else if(src == load) {
            String filename = get_filename(false);
            
            if(load(filename)) {
                Database.filename = filename;
                mw.everythingChanged();
            }
            
        } else if(src == test) {
            test(Database.filename);
        }        
    }
    
    // 
    private void update()
    {
        test.setEnabled( Database.filename != null);
    }
    
    
    private String get_filename(boolean save)
    {
        FileDialog fd = new FileDialog(mw, 
                  save ? "Save as..." :  "Load ...",
                  save ? FileDialog.SAVE : FileDialog.LOAD);
        fd.show();
        return fd.getFile();
    }
    
    private boolean save(String filename)
    {
        OutputStream os = null;        
        try {
            os = new FileOutputStream(filename);            
            CompositionWriter cw = new CompositionWriter(os);
            cw.write();
        } catch(Exception e) {
            System.out.println("SAVE ERROR: " + e);
            return false;
        }  finally {
            try {
                if(os != null) os.close();
            } catch(IOException e) { }
        }
        return true;
    }
    
    private boolean load(String filename)
    {
        InputStream is = null;
        try {
            is = new FileInputStream(filename);            
            CompositionReader2 c2 = new CompositionReader2(is);
            c2.read();
        } catch(Exception e) {
            System.out.println("SAVE ERROR: " + e);
            return false;
        } finally {
            try {
                if(is != null) is.close();
            } catch(IOException e) { }                
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
