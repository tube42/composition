package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

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
    private AffineTransform at1, at2;
    
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
        this.at1 = new AffineTransform();
        this.at2 = new AffineTransform();
        addMouseListener(this);
        addMouseMotionListener(this);                
        
    }
    
    public void displayChanged()
    {
        Format f = Database.current_format;       
        if(f != null) {
            setSize((int)(f.w * Database.scale * 1.2f), 
                    (int)(f.h * Database.scale * 1.2f));
            
            at1.setToIdentity();
            at1.scale(Database.scale, Database.scale);
        }
        clearAll();        
        repaint();
    }
    
    // 
    public void mousePressed(MouseEvent e)
    {
        final int x = e.getX();
        final int y = e.getY();
        final int xt = (int)(x / at1.getScaleX());
        final int yt = (int)(y / at1.getScaleY());
        
        if(ctrl_toolbar.touch(x, y, true, false)) {
            repaint();
            return;
        }
        
        
        if(controls[ctrl_toolbar.get()].touch(xt, yt, true, false)) {
            repaint();
            return;            
        }
        
    }
        
    public void mouseDragged(MouseEvent e) 
    { 
        final int x = e.getX();
        final int y = e.getY();
        final int xt = (int)(x / at1.getScaleX());
        final int yt = (int)(y / at1.getScaleY());
        
        if(ctrl_toolbar.touch(x, y, true, true)) {
            repaint();
            return;
        }
        
        if(controls[ctrl_toolbar.get()].touch(xt, yt, true, true)) {
            repaint();
            return;            
        }                    
    }
    
    public void mouseReleased(MouseEvent e)
    {
        final int x = e.getX();
        final int y = e.getY();
        final int xt = (int)(x / at1.getScaleX());
        final int yt = (int)(y / at1.getScaleY());
        
        if(ctrl_toolbar.touch(x, y, false, false)) {
            repaint();
            return;
        }
        
        if(controls[ctrl_toolbar.get()].touch(xt, yt, false, false)) {            
            mw.propertyChanged();
            repaint();
            return;            
        }
        
        
        if(ctrl_select.touch(xt, yt, false, false)) {
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
        final int w0 = getWidth();
        final int h0 = getHeight();        
        final int w = (int)(w0 / at1.getScaleX());
        final int h = (int)(h0 / at1.getScaleY());
                
        format = Database.current_format;
        if(format == null) return;
        
        x0 = (w0 - format.w) / 2;
        y0 = (h0 - format.h) / 2;
        
        // System.out.println(" " + w0 + " " + h0 + " - " + w + " " + h + " - " + x0 + " " + y0);
        
        // position the format and screen regions
        Database.rformat.values[0] = 0;
        Database.rformat.values[1] = 0;
        Database.rformat.values[2] = format.w;
        Database.rformat.values[3] = format.h;
        
        Database.rscreen.values[0] = -Math.max(10, x0 / 2);
        Database.rscreen.values[1] = -Math.max(10, y0 / 2);
        Database.rscreen.values[2] = format.w + Math.max(10, x0 / 2);
        Database.rscreen.values[3] = format.h + Math.max(10, y0 / 2);
        
        // scale and draw board
	Graphics2D g2d = (Graphics2D)g;
	g2d.setTransform(at1);        
        paint_board.paint(g, w, h);
        controls[ctrl_toolbar.get()].paint(g, w, h);
        
        // scale and draw controls
	g2d.setTransform(at2);
        ctrl_toolbar.paint(g, w0, h0); 
                                                                
        final String rname = Database.current_region;        
        if(rname == null) return;
        region = format.getRegion(rname);
        
    }
        
}
