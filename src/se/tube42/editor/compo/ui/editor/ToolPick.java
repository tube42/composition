package se.tube42.editor.compo.ui.editor;

import java.awt.*;

import se.tube42.editor.compo.data.*;

public class ToolPick extends EditorControl
{
    public static final int HANDLE = 8;        
    
    private int selected = -1;
        
    public void paint(Graphics g, int w, int h)
    {
        if(region != null) {                        
            final int xa = x0 + region.values[0];
            final int xb = x0 + region.values[2];
            final int ya = y0 + region.values[1];
            final int yb = y0 + region.values[3];
            
            g.setColor(Color.BLACK);
            
            g.drawRect( xa - HANDLE, ya - HANDLE, HANDLE * 2, HANDLE * 2);
            g.drawRect( xb - HANDLE, yb - HANDLE, HANDLE * 2, HANDLE * 2);
        }
    }
    
    public boolean touch(int x, int y, boolean down, boolean drag)
    {
        
        if(region == null) return false;
        
        int x1 = x - x0;
        int y1 = y - y0;
        
        if(down && !drag) {
            if(Math.abs(region.values[0] - x1) < HANDLE && 
               Math.abs(region.values[1] - y1) < HANDLE) {
                selected = 0;
                return true;                    
            } else if(Math.abs(region.values[2] - x1) < HANDLE && 
                      Math.abs(region.values[3] - y1) < HANDLE) {
                selected = 1;
                return true;
                
            } else {
                selected = -1;                                    
                return false;        
            }
        } else if(down && drag) {
            if(selected == -1) return false;
            
            x1 = snap(x1);
            y1 = snap(y1);
            
            if(selected == 0) {
                region.values[0] = x1;
                region.values[1] = y1;
            } else if(selected == 1) {
                    region.values[2] = x1;
                region.values[3] = y1;
            }
            return true;
        }  else {  
            if(selected == -1) return false;
            selected = -1;
            return true;
        }        
    }

}
