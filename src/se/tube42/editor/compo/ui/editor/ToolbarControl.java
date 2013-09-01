package se.tube42.editor.compo.ui.editor;

import java.awt.*;
import java.net.*;
import javax.imageio.*;


public class ToolbarControl extends EditorControl
{
    private static final int
          COUNT = 4,
          SIZE = 32,
          X0 = 16,
          Y0 = 16
          ;
    
    private int tool = 0;
    private int sel = -1;
    private boolean active = false;
    private Image image;
    
    public ToolbarControl()
    {
        try {
            URL url = getClass().getResource("/toolbar.png");
            this.image = ImageIO.read( url);
        } catch(Exception exx) {
            exx.printStackTrace();
        }
    }
    
    public int get()
    {
        return tool;
    }
    
    public void paint(Graphics g, int w, int h)
    {
        
        for(int i = 0; i < COUNT; i++) {
            int x = X0 + SIZE * i;
            int y = Y0;
            int size = SIZE;
            
            if(i != sel) {
                x++;
                y++;
                size -= 2;
            }
            
            if(i == tool) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(x, y, size, size);
            } else {
                g.setColor(Color.BLACK);
                g.drawRect(x, y, size, size);
            }
        }
        
        if(image != null) {
            g.drawImage( image, X0, Y0, null);
        }
    }
    
    public boolean touch(int x, int y, boolean down, boolean drag)
    {
        int get = lookup(x, y);
        if(down && !drag) {
            sel = get;
            active = sel != -1;
        }  
        
        if(!active) return false;
        
        if(sel != get) 
            sel = -1;
        
        if(!down) {
            if(sel != -1) {
                tool = sel;
                sel = -1;
                return true;
            }
        }
        
        return active;  
    }
    
    // ---------------------------------------------
    private int lookup(int x, int y)
    {
        if(y < Y0 || y > Y0 + SIZE) return -1;        
        
        final int n = (x - X0) / SIZE;
        return (n < 0 || n >= COUNT) ? -1 : n;        
    }
}
