package se.tube42.editor.compo.ui.editor;

import java.awt.*;

import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.service.*;

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

        final int len = Database.regions.size();
        for(int i = 0; i < len; i++) {
            if(ServiceProvider.regionIsHidden(i))
                continue;

            final String s = Database.regions.get(i);
            final RegionData rd = Database.current_format.getRegion(s);
            if(rd.hit(x, y)) {
                Database.current_region = s;
                return true;
            }
        }
        return false;
    }

}
