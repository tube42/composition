package se.tube42.lib.compo;

import java.util.*;

public class Composition
{
    private int w, h;
    private ArrayList<ScreenTemplate> templates;
    private ScreenTemplate current;    
    private HashMap<String, Region> region_map;
    
    public Composition()
    {
        this.current = null;
        this.templates = new ArrayList<ScreenTemplate>();
        this.region_map = new HashMap<String,Region>();
    }
    
    public Region get(String name)
    {
        return region_map.get(name);
    }
    public int getW() { return w; }
    public int getH() { return h; }
    
    // 
    public void resize(int w, int h)
    {
        if(this.w == w && this.h == h) return;
        this.w = w;
        this.h = h;
        
        
        this.current = null;
        int best_err = 0;
        int best_scale = 1;
        for(int scale = 1; scale < 16; scale ++) {
            for(ScreenTemplate st : templates) {
                int err = error(st, w * scale, h * scale) * scale;
                if(current == null || err < best_err) {
                    current = st;
                    best_err = err;
                    best_scale = scale;
                }
            }
        }
        
        current.build(this);        
        System.out.println("BEST: " + current + " " + best_err + " " + best_scale);
    }
    
    private int error(ScreenTemplate st, int w, int h)
    {
        if(st.w > w || st.h > h) return Integer.MAX_VALUE;
        
        final int dx = w - st.w;
        final int dy = h - st.h;
        return dx * dx + dy * dy;
    }
}
