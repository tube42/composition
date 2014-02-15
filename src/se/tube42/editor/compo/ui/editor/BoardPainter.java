package se.tube42.editor.compo.ui.editor;

import java.awt.*;

import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.ui.*;
import se.tube42.editor.compo.service.*;

public class BoardPainter extends EditorControl
{
    
    private final int [] tmp = new int[4];
    
    public boolean touch(int x, int h, boolean down, boolean drag)
    {
        return false;
    }
    
    public void paint(Graphics g, int w, int h)
    {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        
        final Format format = Database.current_format;
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
        draw_region(g, Database.rscreen, 0);
        
        
        g.setColor(Color.BLACK);        
        draw_region(g, Database.rformat, 0);
        
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
                
                // draw the types 
                g.setFont(UI.TYPE_FONT);
                g.setColor(Color.BLACK);
                
                // draw anchors
                draw_anchor_h(g, r, w, h, 0, x0 + x1, y0 + y1, x2 - x1, y2 - y1); 
                draw_anchor_v(g, r, w, h, 1, x0 + x1, y0 + y1, x2 - x1, y2 - y1);            
                draw_anchor_h(g, r, w, h, 2, x0 + x2, y0 + y1, x2 - x1, y2 - y1);            
                draw_anchor_v(g, r, w, h, 3, x0 + x1, y0 + y2, x2 - x1, y2 - y1);            
                                
            } else {
                for(int j = -1; j < 2; j++)
                    draw_region(g, r, j);
            }
            
        }        
    }
    
    
    // --------------------------------
    private void draw_region(Graphics g, RegionData r, int d)
    {        
        g.drawRect(x0 + r.values[0] - d, y0 + r.values[1] - d,
                  r.values[2] - r.values[0] + d * 2,
                  r.values[3] - r.values[1] + d * 2);
    }

    private void get_anchor_position(int target, int w_, int h_, int [] points)
    {
        int x, y, w, h;
        
        RegionData r1 = ServiceProvider.getRegion(target);
        if(r1 == null) return;
        
        final int dx = (w_ - Database.current_format.w) / 2;
        final int dy = (h_ - Database.current_format.h) / 2;
        points[0] = r1.values[0] + dx;
        points[1] = r1.values[1] + dy;
        points[2] = r1.values[2] - r1.values[0];
        points[3] = r1.values[3] - r1.values[1];   
    }    
    
    private void draw_anchor_v(Graphics g, RegionData r, 
              int sw, int sh,
              int index,  int x, int y, int w, int h)
    {
        final int target = r.targets[index];
        final int type = r.types[index];
        
        get_anchor_position(target, sw, sh, tmp);        
        
        int x0 = x + w / 2 + (index == 3 ? + 2 : -2);
        int y1 = (type == RegionData.TYPE_P1) ? tmp[1] + tmp[3] : tmp[1];
        
        g.drawLine( x0, y, x0, y1);
        
        g.fillOval(x0-3, y -3, 6, 6);
        g.fillOval(x0-3, y1-3, 6, 6);        
    }    
    
    private void draw_anchor_h(Graphics g, RegionData r, 
              int sw, int sh,              
              int index, int x, int y, int w, int h)
    {
        final int target = r.targets[index];
        final int type = r.types[index];
        
        get_anchor_position(target, sw, sh, tmp);        
        
        int y0 = y + h / 2 + (index == 0 ? + 2 : -2);
        int x1 = (type == 1) ? tmp[0] + tmp[2] : tmp[0];
        g.drawLine( x, y0, x1, y0);
        
        g.fillOval(x -3, y0-3, 6, 6);
        g.fillOval(x1-3, y0-3, 6, 6);
        
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
