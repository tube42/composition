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
    private int [] font_add_x = null, font_add_y = null;
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
    
    
    public void update(Graphics g)
    {
        paint(g);
    }
    
    public void paint(Graphics g)
    {
        
        calc_font_sizes(g);
        
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
                
                // draw align lines
                g.setColor(Color.ORANGE);
                if(align_horizontal(format, r, x1)) g.drawLine(x0 + x1, 0, x0 + x1, h);                
                if(align_horizontal(format, r, x2)) g.drawLine(x0 + x2, 0, x0 + x2, h);                
                if(align_vertical(format, r, y1)) g.drawLine(0, y0 + y1, w, y0 + y1);
                if(align_vertical(format, r, y2)) g.drawLine(0, y0 + y2, w, y0 + y2);
                
                // draw the box
                g.fillRect(x0 + x1, y0 + y1, x2 - x1, y2 - y1);                
                
                // draw the handles
                g.setColor(Color.BLACK);
                g.drawRect(x0 + x1, y0 + y1, x2 - x1, y2 - y1);
                g.drawOval( x0 + x1 - HANDLE , y0 + y1 - HANDLE, HANDLE * 2, HANDLE * 2);
                g.drawOval( x0 + x2 - HANDLE, y0 + y2 - HANDLE, HANDLE * 2, HANDLE * 2);
                
                // draw the types 
                g.setFont(UI.TYPE_FONT);
                g.setColor(Color.BLACK);
                for(int j = 0; j < 4; j++)
                    draw_type(g, j, r.types[j], x0 + x1, y0 + y1, x2 - x1, y2 - y1);            
                
            } else {
                for(int j = -1; j < 2; j++)
                    g.drawRect(x0 + x1 + j, y0 + y1 + j, x2 - x1 - j * 2, y2 - y1 - j * 2);
            }
            
        }        
    }
        
    private void draw_type(Graphics g, int n, int type, int x, int y, int w, int h)
    {
        x -= font_add_x[type];
        y += font_add_y[type];
        
        if(n == 0 || n == 2) y += h / 2;
        if(n == 1 || n == 3) x += w / 2;
        if(n == 2) x += w;
        if(n == 3) y += h;                
        
        g.drawString( Database.TYPES[type], x, y);
    }    
    
    private void calc_font_sizes(Graphics g)
    {
        // do we have the correct font metrics?
        if(font_add_x == null || font_add_y == null) {
            final FontMetrics m = g.getFontMetrics(UI.TYPE_FONT);            
            final int len = Database.TYPES.length;            
            font_add_x = new int[len];
            font_add_y = new int[len];                                                
            for(int i = 0; i < len; i++) {
                font_add_y[i] = m.getHeight() / 2;
                font_add_x[i] = m.stringWidth(Database.TYPES[i]) / 2;
            }
        }        
    }
    
    private boolean align_horizontal(Format f, RegionData r, int x)
    {        
        for(final String r2name : Database.regions) {
            final RegionData r2 = f.getRegion(r2name);
            if(r == r2) continue;
            if(r2.values[0] == x || r2.values[2] == x)
                return true;
        }
        return false;
    }
    
    private boolean align_vertical(Format f, RegionData r, int y)
    {        
        for(final String r2name : Database.regions) {
            final RegionData r2 = f.getRegion(r2name);
            if(r == r2) continue;
            if(r2.values[1] == y || r2.values[3] == y)
                return true;
        }
        return false;
    }    
}
