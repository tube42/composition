package se.tube42.editor.compo;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.ui.*;
import se.tube42.lib.compo.*;

public class TestWindow extends Frame
{
    private TestCanvas canvas;
    
    public TestWindow(Composition c)
    {
        super("[Composition test]");                
        add(canvas = new TestCanvas(c));
        
        // Frame stuff        
        final WindowAdapter wc = new WindowAdapter() {
            public void windowClosing(WindowEvent e) { 
                setVisible(false);
            }
        };
        
        addWindowListener(wc);                        
        setSize(500, 500);
        setVisible(true);
    }
    
    public void update(Graphics g)
    {
        paint(g);
    }
}
