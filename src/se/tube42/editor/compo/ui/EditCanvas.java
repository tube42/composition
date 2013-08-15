package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;


public class EditCanvas 
extends Canvas
implements MouseListener, MouseMotionListener
{
    private static final int HANDLE = 5;
    
    private MainWindow mw;
    private int x0, y0;
    
    private int selected, xa, ya;        
    private RegionData region;
    private Format format;
    
    public EditCanvas(MainWindow mw)
    {
        this.mw = mw;        
        this.region = null;
        this.format = null;
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    public void update(Graphics g)
    {
        paint(g);
    }
    
    public void paint(Graphics g)
    {
        final int w = getWidth();
        final int h = getHeight();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        
        format = Database.current_format;
        if(format == null) return;
        x0 = (w - format.w) / 2;
        y0 = (h - format.h) / 2;
        
        
        // draw the grid?
        if(Database.grid_show) {
            final int gsize = Database.grid_size;
            g.setColor(Color.GRAY);
            
            for(int x = 0; x < format.w; x += gsize)
                g.drawLine(x0 + x, y0, x0 + x, y0 + format.h);
            
            for(int y = 0; y < format.h; y += gsize)
                g.drawLine(x0, y0 + y, x0 + format.w, y0 + y);
        }
                
        // draw a rect around all and one around the screen
        g.setColor(Color.YELLOW);
        g.drawRect(0, 0, w-1, h-1);
        
        g.setColor(Color.BLACK);
        g.drawRect(x0, y0, format.w, format.h);
        
        
        
        final String rname = Database.current_region;        
        if(rname == null) return;
        region = format.getRegion(rname);
        
        
        final int len = Database.regions.size();
        for(int i = 0; i < len; i++) {
            
            if( i < 32 && (Database.regions_hidden & (1 << i)) != 0) 
                continue;
            
            final String name = Database.regions.get(i);
            final RegionData r = format.getRegion(name);
            
            
            int x1 = r.values[0];
            int y1 = r.values[1];
            int x2 = r.values[2];
            int y2 = r.values[3];
            
            g.setColor( UI.REGION_COLORS[i % UI.REGION_COLORS.length]);
            if(r == region) {
                g.fillRect(x0 + x1, y0 + y1, x2 - x1, y2 - y1);
                
                // draw the handles
                g.setColor(Color.BLACK);
                g.drawRect(x0 + x1, y0 + y1, x2 - x1, y2 - y1);
                g.drawOval( x0 + x1 - HANDLE , y0 + y1 - HANDLE, HANDLE * 2, HANDLE * 2);
                g.drawOval( x0 + x2 - HANDLE, y0 + y2 - HANDLE, HANDLE * 2, HANDLE * 2);
            } else {
                for(int j = -1; j < 2; j++)
                    g.drawRect(x0 + x1 + j, y0 + y1 + j, x2 - x1 - j * 2, y2 - y1 - j * 2);
            }
        }        
    }
    
    
    // 
    public void mousePressed(MouseEvent e)
    {
        selected = -1;        
        if(format == null || region == null) return;
            
        final int x = e.getX() - x0;
        final int y = e.getY() - y0;
        
        if(Math.abs(region.values[0] - x) < HANDLE && Math.abs(region.values[1] - y) < HANDLE) {
            selected = 0;
        } else if(Math.abs(region.values[2] - x) < HANDLE && Math.abs(region.values[3] - y) < HANDLE) {
            selected = 1;
        }       
    }
    
    public void mouseReleased(MouseEvent e)
    {
        // see if we can select another one        
        if(selected == -1 && format != null && region != null) {
            final int x = e.getX() - x0;
            final int y = e.getY() - y0;
            
            if(region.hit(x, y)) return;
            
            for(final String s : Database.regions) {
                RegionData rd = format.getRegion(s);
                if(rd.hit(x, y)) {
                    Database.current_region = s;
                    mw.everythingChanged();
                    return;
                }
            }                        
        }        
    }
    
    public void mouseDragged(MouseEvent e) 
    { 
        if(selected == -1 || format == null || region == null) return;
        
        int x = e.getX() - x0;
        int y = e.getY() - y0;
        
        if(Database.grid_enable) {
            final int g1 = Database.grid_size;
            final int g2 = g1 / 2;
            x = ((x + g2) / g1) * g1;
            y = ((y + g2) / g1) * g1;
        }
        
        if(selected == 0) {
            region.values[0] = x;
            region.values[1] = y;
        } else if(selected == 1) {
            region.values[2] = x;
            region.values[3] = y;
        }
        mw.regionChanged();        
    }
    
    public void mouseClicked(MouseEvent e) { }        
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }    
    public void mouseMoved(MouseEvent e) { }
}
