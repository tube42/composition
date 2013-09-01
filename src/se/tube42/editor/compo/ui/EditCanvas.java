package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.service.*;
import se.tube42.editor.compo.ui.editor.*;


public class EditCanvas 
extends DBCanvas
implements MouseListener, MouseMotionListener
{
    
    private MainWindow mw;
    private int x0, y0;
    
    private int selected, xa, ya;        
    private RegionData region;
    private Format format;
    
    private EditorControl paint_board = new BoardPainter();
    private EditorControl ctrl_select = new SelectControl();
    private ToolbarControl ctrl_toolbar = new ToolbarControl();
    private EditorControl [] controls = { 
        new ToolPick(), new ToolMove(), new ToolResize(),
        new ToolConnect()
    };
    
    public EditCanvas(MainWindow mw)
    {
        this.mw = mw;        
        this.region = null;
        this.format = null;
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    

    // 
    public void mousePressed(MouseEvent e)
    {
        final int x = e.getX();
        final int y = e.getY();
        
        if(ctrl_toolbar.touch(x, y, true, false)) {
            repaint();
            return;
        }
        
        if(controls[ctrl_toolbar.get()].touch(x, y, true, false)) {
            repaint();
            return;            
        }
        
    }
        
    public void mouseDragged(MouseEvent e) 
    { 
        final int x = e.getX();
        final int y = e.getY();
        
        if(ctrl_toolbar.touch(x, y, true, true)) {
            repaint();
            return;
        }
        
        if(controls[ctrl_toolbar.get()].touch(x, y, true, true)) {
            repaint();
            return;            
        }
                    

    }
    
    public void mouseReleased(MouseEvent e)
    {
        final int x = e.getX();
        final int y = e.getY();
        
        if(ctrl_toolbar.touch(x, y, false, false)) {
            repaint();
            return;
        }
        
        if(controls[ctrl_toolbar.get()].touch(x, y, false, false)) {            
            mw.propertyChanged();
            repaint();
            return;            
        }
        
        
        if(ctrl_select.touch(x, y, false, false)) {
            mw.everythingChanged();
            repaint();
            return;                        
        }
    }
    
    // ----------------------------------------
    
    public void mouseClicked(MouseEvent e) { }        
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }    
    public void mouseMoved(MouseEvent e) { }
    
    
    public void bufferedPaint(Graphics g)
    {                
        final int w = getWidth();
        final int h = getHeight();
        
        
        format = Database.current_format;
        if(format == null) return;
        
        x0 = (w - format.w) / 2;
        y0 = (h - format.h) / 2;
        
        // position the format and screen regions
        Database.rformat.values[0] = 0;
        Database.rformat.values[1] = 0;
        Database.rformat.values[2] = format.w;
        Database.rformat.values[3] = format.h;
        
        Database.rscreen.values[0] = -Math.max(10, x0 / 2);
        Database.rscreen.values[1] = -Math.max(10, y0 / 2);
        Database.rscreen.values[2] = format.w + Math.max(10, x0 / 2);
        Database.rscreen.values[3] = format.h + Math.max(10, y0 / 2);
        
        paint_board.paint(g, w, h);
        ctrl_toolbar.paint(g, w, h);
        controls[ctrl_toolbar.get()].paint(g, w, h);
        
                                                
        
        final String rname = Database.current_region;        
        if(rname == null) return;
        region = format.getRegion(rname);
        
    }
        
}
