package se.tube42.editor.compo;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.ui.*;

public class EditWindow extends Frame
{
    private MainWindow mw;
    private EditCanvas canvas;
    private EditFormat format;
    
    public EditWindow(MainWindow mw)
    {        
        super("[Composition edit]");
        
        this.mw = mw;
        
        add(format = new EditFormat(mw), BorderLayout.NORTH);
        add(canvas = new EditCanvas(mw), BorderLayout.CENTER);
        
        setSize(500, 700);
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
