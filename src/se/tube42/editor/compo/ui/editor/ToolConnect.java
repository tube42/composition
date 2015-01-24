package se.tube42.editor.compo.ui.editor;

import java.awt.*;

import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.service.*;

public class ToolConnect extends EditorControl
{

    public final static int HANDLE = 8;

    private boolean active = false;
    private int x1, x2, y1, y2;
    private int [] tmp = new int[4];
    private int anchor_start, anchor_end;

    public void paint(Graphics g, int w, int h)
    {

        if(region == null) return;

        g.setColor(Color.BLACK);

        if(active) {
            g.drawLine(x1, y1, x2, y2);
        }

        for(int i = 0; i < 4; i++) {
            get_pos_anchor(region, i, tmp);

            if(i == anchor_start && active)
                g.fillRect( x0 + tmp[0] - HANDLE, y0 + tmp[1] - HANDLE,
                          HANDLE * 2, HANDLE * 2);
            else
                g.drawRect( x0 + tmp[0] - HANDLE, y0 + tmp[1] - HANDLE,
                          HANDLE * 2, HANDLE * 2);
        }

    }

    public boolean touch(int x, int y, boolean down, boolean drag)
    {

        if(region == null) {
            active = false;
            return false;
        }

        if(down && ! drag) {
            anchor_start = get_nearby_anchor(region, x - x0, y - y0);
            if(anchor_start == -1) {
                active = false;
                return false;
            }
            get_pos_anchor(region, anchor_start, tmp);
            x1 = x2 = tmp[0] + x0;
            y1 = y2 = tmp[1] + y0;
            active = true;
            return true;
        }

        if(!active) return false;

        x2 = x;
        y2 = y;


        if(!down) {
            active = false;
            connect_to(x - x0, y - y0);
        }

        return true;
    }

    // --------------------------------------------------

    private boolean index_is_horiz(int index)
    {
        return index == 0 || index == 2;
    }

    private int get_nearby_anchor(RegionData r, int x, int y)
    {
        for(int i = 0; i < 4; i++) {
            get_pos_anchor(r, i, tmp);
            if(Math.max( Math.abs(tmp[0] - x), Math.abs(tmp[1] - y)) < HANDLE)
                return i;
        }
        return -1;
    }


    private int get_nearby_edge(RegionData r, int x, int y, boolean checkh, boolean checkv)
    {
        if( checkh && r.values[1] <= y && r.values[3] >= y) {
            if( Math.abs( r.values[0] - x) < HANDLE) return 0;
            if( Math.abs( r.values[2] - x) < HANDLE) return 2;
        }

        if(checkv & r.values[0] <= x && r.values[2] >= x) {
            if( Math.abs( r.values[1] - y) < HANDLE) return 1;
            if( Math.abs( r.values[3] - y) < HANDLE) return 3;
        }

        return -1;
    }

    private boolean connect_to(int x, int y)
    {
        if(Database.current_format == null) return false;

        boolean ish = index_is_horiz(anchor_start);
        int i = Database.regions.size() + 2;

        while(--i >= 0) {
            final RegionData r = ServiceProvider.getRegion(i);
            final int anchor_end = get_nearby_edge(r, x, y, ish, !ish);
            if(r == region && anchor_end == anchor_start) continue;

            if(anchor_end != -1) {
                region.targets[anchor_start] = i;
                region.types[anchor_start] = (anchor_end > 1) ? RegionData.TYPE_P1 : RegionData.TYPE_P0;
                return true;
            }
        }

        return false;
    }

    private void get_pos_anchor(RegionData r, int index, int [] ret)
    {
        switch(index) {
        case 0: ret[0] = r.values[0]; break;
        case 2: ret[0] = r.values[2]; break;
        default:
            ret[0] = (r.values[0] + r.values[2]) / 2;
        }

        switch(index) {
        case 1: ret[1] = r.values[1]; break;
        case 3: ret[1] = r.values[3]; break;
            default:
            ret[1] = (r.values[1] + r.values[3]) / 2;
        }

    }

}
