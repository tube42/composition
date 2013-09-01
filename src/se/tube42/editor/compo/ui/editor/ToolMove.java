package se.tube42.editor.compo.ui.editor;

import java.awt.*;

import se.tube42.editor.compo.data.*;

public class ToolMove extends EditorControl
{
    
    private boolean active = false;
    private int xa, ya;
    
    public void paint(Graphics g, int w, int h)
    {        
        if(region != null) {        
            final int xc = x0 + (region.values[0] + region.values[2]) / 2;
            final int yc = y0 + (region.values[1] + region.values[3]) / 2;
            
            g.setColor(Color.BLACK);
            g.drawLine(xc, yc - 10, xc, yc + 10);
            g.drawLine(xc - 10, yc, xc + 10, yc);
        }        
    }
    
    public boolean touch(int x, int y, boolean down, boolean drag)
    {   
        if(region == null) return false;
        
        x -= x0;
        y -= y0;
        
        if(down && !drag) {
            active = region.hit(x, y);
            xa = x;
            ya = y;
            return active;
        } else if(!active) {
            return false;            
        }
        
        final int dx = xa - x;
        final int dy = ya - y;
        xa = x;
        ya = y;
        
        // move with snap
        final int w = region.values[2] - region.values[0];
        final int h = region.values[3] - region.values[1];
        region.values[0] = snap(region.values[0] - dx);
        region.values[1] = snap(region.values[1] - dy);
        region.values[2] = region.values[0] + w;
        region.values[3] = region.values[1] + h;
        return true;
    }

}
