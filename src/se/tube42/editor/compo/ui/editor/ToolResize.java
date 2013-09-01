package se.tube42.editor.compo.ui.editor;

import java.awt.*;

import se.tube42.editor.compo.data.*;

public class ToolResize extends EditorControl
{
    
    private boolean active = false;
    private int xc, yc, x1, y1;
    
    public void paint(Graphics g, int w, int h)
    {
        if(region != null) {        
            final int xc = x0 + (region.values[0] + region.values[2]) / 2;
            final int yc = y0 + (region.values[1] + region.values[3]) / 2;
            
            g.setColor(Color.BLACK);
            g.drawLine(xc - 10, yc - 10, xc + 10, yc + 10);
            g.drawLine(xc + 10, yc - 10, xc - 10, yc + 10);
        }        
        
    }
    
    public boolean touch(int x, int y, boolean down, boolean drag)
    {
        if(region == null) return false;
        
        if(down && ! drag) {
            active = region.hit(x - x0, y - y0);
            xc = (region.values[0] + region.values[2]) / 2;
            yc = (region.values[1] + region.values[3]) / 2;
            x1 = x;
            y1 = y;
            return active;
        } else if(!active) {
            return false;
        }
        
        final int dx = Math.max(6, Math.abs( x - x0 - xc));
        final int dy = Math.max(6, Math.abs( y - y0 - yc));
        
        region.values[0] = xc - dx;
        region.values[1] = yc - dy;
        region.values[2] = xc + dx;
        region.values[3] = yc + dy;
        
        return true;
    }

}
