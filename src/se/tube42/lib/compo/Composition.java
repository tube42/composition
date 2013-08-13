package se.tube42.lib.compo;

import java.util.*;
import java.io.*;

public class Composition
{
    private int w, h, scale;
    
    private FormatTemplate [] format_templates;
    private Region [] regions;
    private FormatTemplate current_format;        
    private HashMap<String, Region> region_map;
    
    /** load a composition from file */
    public static Composition load(InputStream is)
    {
        try {
            CompositionReader cr = new CompositionReader(is);
            return cr.read();
        } catch(Exception e) {
            System.err.println("Error: " + e);
            return null;
        }
    }
    
    /* package */ Composition(FormatTemplate [] formats, String [] region_names)
    {
        this.current_format = null;
        this.region_map = new HashMap<String,Region>();
        this.w = -1;
        this.h = -1;
        this.scale = 1;
        
        this.format_templates = formats;
        this.regions = new Region[region_names.length];
        for(int i = 0; i < regions.length; i++) {
            regions[i] = new Region(region_names[i]);
            region_map.put(region_names[i], regions[i]);
        }
    }
    
    // 
    public Region [] getAll()
    {
        return regions;
    }
    
    public Region get(String name)
    {
        return region_map.get(name);
    }
    public int getW() { return w; }
    public int getH() { return h; }
    public int getScale() { return scale; }
    
    /**
     * returns name of current template
     */
    public String getName() 
    {
        return current_format == null ? null : current_format.getName();
    }
    
    /**
     * manually choose a template.
     * returns false if not found
     */
    public boolean setManually(String name, int scale)
    {
        for(FormatTemplate ft : format_templates) {
            if(ft.getName().equals(name)) {
                set_format(ft, scale);
                return true;
            }
        }
        return false;
    }
    
    /**
     * choose a template based on the screen size
     */
    public boolean resize(int w, int h)
    {
        if(this.w == w && this.h == h) return true;        
        
        FormatTemplate best_format = null;
        int best_err = 0;
        int best_scale = 1;
        for(int scale = 1; scale < 8; scale ++) {
            for(FormatTemplate st : format_templates) {                
                int err = st.cost(w, h, scale) + scale;
                if(best_format == null || err < best_err) {
                    best_format = st;
                    best_err = err;
                    best_scale = scale;
                }
            }
        }
        
        if(best_format == null) 
            return false;
                
        this.w = w;
        this.h = h;        
        set_format(best_format, best_scale);        
        return true;
    }    
    
    private void set_format(FormatTemplate f, int scale)
    {
        this.scale = scale;
        current_format = f;        
        current_format.build(regions, this.w, this.h, scale);        
        
        
    }
}
