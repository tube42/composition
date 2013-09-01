package se.tube42.editor.compo.ui.editor;

import java.awt.*;

import se.tube42.editor.compo.data.*;

public class SelectControl extends EditorControl
{
        
    public void paint(Graphics g, int w, int h)
    {
    }
    
    public boolean touch(int x, int y, boolean down, boolean drag)
    {
        if(down) return false;
        
        if(region == null) return false;
        
        x -= x0;
        y -= y0;
        if(region.hit(x, y)) return false;        
        
        for(final String s : Database.regions) {
            RegionData rd = Database.current_format.getRegion(s);
            if(rd.hit(x, y)) {
                Database.current_region = s;
                return true;
            }        
        }
        return false;
    }

}
