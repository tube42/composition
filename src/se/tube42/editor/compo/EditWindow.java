package se.tube42.editor.compo;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.ui.*;

public class EditWindow extends Frame
{
    private MainWindow mw;
    private EditCanvas canvas;
    
    public EditWindow(MainWindow mw)
    {        
        super("[Composition edit]");
        
        this.mw = mw;
        
        add(canvas = new EditCanvas(mw), BorderLayout.CENTER);
        
        setSize(350, 250);
        setVisible(true);
    }
    
    public void update(Graphics g)
    {
        paint(g);
    }
    
    public void regionChanged()
    {
        canvas.repaint();
    }
}
