package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.service.*;


public class EditCanvas 
extends Canvas
implements MouseListener, MouseMotionListener
{
    private static final int HANDLE = 12;
    
    private MainWindow mw;
    private int x0, y0;
    
    private int selected, xa, ya;        
    private RegionData region;
    private Format format;
    private int [] font_add_x = null, font_add_y = null;
    private int [] tmp = new int[4];
    
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
        mw.propertyChanged();        
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
            
                
            // draw align lines            
            if(r == region) {
                g.setColor(Color.ORANGE);
                if(align_horizontal(format, r, x1)) g.drawLine(x0 + x1, 0, x0 + x1, h);                
                if(align_horizontal(format, r, x2)) g.drawLine(x0 + x2, 0, x0 + x2, h);                
                if(align_vertical(format, r, y1)) g.drawLine(0, y0 + y1, w, y0 + y1);
                if(align_vertical(format, r, y2)) g.drawLine(0, y0 + y2, w, y0 + y2);
            }
            
            g.setColor( UI.REGION_COLORS[i % UI.REGION_COLORS.length]);
            if(r == region) {
                
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
                
                // draw anchors
                draw_anchor_h(g, r, 0, x0 + x1, y0 + y1, x2 - x1, y2 - y1); 
                draw_anchor_v(g, r, 1, x0 + x1, y0 + y1, x2 - x1, y2 - y1);            
                draw_anchor_h(g, r, 2, x0 + x2, y0 + y1, x2 - x1, y2 - y1);            
                draw_anchor_v(g, r, 3, x0 + x1, y0 + y2, x2 - x1, y2 - y1);            
                
                
            } else {
                for(int j = -1; j < 2; j++)
                    g.drawRect(x0 + x1 + j, y0 + y1 + j, x2 - x1 - j * 2, y2 - y1 - j * 2);
            }
            
        }        
    }
    
    // --------------------------------
    private static void get_anchor_position(int target, int w_, int h_, int [] points)
    {
        int x, y, w, h;
        
        if(target == 0) {
            x = y = 1;
            w = w_-1;
            h = h_-1;
        } else if(target == 1) {
            w = Database.current_format.w;
            h = Database.current_format.h;
            x = (w_ - w) / 2;
            y = (h_ - h) / 2;                                    
        } else {
            final String name = Database.regions.get(target -2);
            final RegionData r1 = Database.current_format.getRegion(name);
            
            final int dx = (w_ - Database.current_format.w) / 2;
            final int dy = (h_ - Database.current_format.h) / 2;
            x = r1.values[0] + dx;
            y = r1.values[1] + dy;
            w = r1.values[2] - r1.values[0];
            h = r1.values[3] - r1.values[1];   
        }        
        
        points[0] = x;
        points[1] = y;
        points[2] = w;
        points[3] = h;                    
    }    
    
    private void draw_anchor_v(Graphics g, RegionData r, int index,  int x, int y, int w, int h)
    {
        final int sw = getWidth();
        final int sh = getHeight();        
        final int target = r.targets[index];
        final int type = r.types[index];
        
        get_anchor_position(target, sw, sh, tmp);        
        
        int x0 = x + w / 2 + (index == 3 ? + 2 : -2);
        int y1 = (type == 1) ? tmp[1] + tmp[3] : tmp[1];
        
        g.drawLine( x0, y, x0, y1);
        
        g.fillOval(x0-3, y -3, 6, 6);
        g.fillOval(x0-3, y1-3, 6, 6);
        
    }    
    private void draw_anchor_h(Graphics g, RegionData r, int index, int x, int y, int w, int h)
    {
        final int sw = getWidth();
        final int sh = getHeight();        
        final int target = r.targets[index];
        final int type = r.types[index];
        
        get_anchor_position(target, sw, sh, tmp);        
        
        int y0 = y + h / 2 + (index == 0 ? + 2 : -2);
        int x1 = (type == 1) ? tmp[0] + tmp[2] : tmp[0];
        g.drawLine( x, y0, x1, y0);
        
        g.fillOval(x -3, y0-3, 6, 6);
        g.fillOval(x1-3, y0-3, 6, 6);
        
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
