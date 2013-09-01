package se.tube42.editor.compo.ui.editor;

import java.awt.*;

import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.ui.*;

public abstract class EditorControl
{
    protected static RegionData region;
    protected static int x0, y0, w0, h0;
    
    public abstract void paint(Graphics g, int w, int h);
    public abstract boolean touch(int x, int h, boolean down, boolean drag);
    
    
    public int snap(int point)
    {
        if(Database.grid_enable) {
            final int g1 = Database.grid_size;
            final int g2 = g1 / 2;
            point = ((point + g2) / g1) * g1;
        }
        return point;
    }
        
}
