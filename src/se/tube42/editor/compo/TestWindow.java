package se.tube42.editor.compo;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.ui.*;
import se.tube42.lib.compo.*;

public class TestWindow 
extends Frame
implements ItemListener
{
    private TestCanvas canvas;
    private Checkbox flipv, fliph;
    private Composition compo;
    
    public TestWindow(Composition c)
    {
        super("[Composition test]");                
        
        this.compo = c;
        
        add(canvas = new TestCanvas(c), BorderLayout.CENTER);
        
        Panel p = new Panel();
        add(p, BorderLayout.NORTH);
        p.add(fliph = new Checkbox("Flip horizontal"));
        p.add(flipv = new Checkbox("Flip vertical"));
        
        fliph.addItemListener(this);
        flipv.addItemListener(this);
        
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
    
    public void itemStateChanged(ItemEvent e)
    {        
        compo.flip(fliph.getState(), flipv.getState());
        compo.resize(canvas.getWidth(), canvas.getHeight());
        canvas.repaint();
    }
    
}
