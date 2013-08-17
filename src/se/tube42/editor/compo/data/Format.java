package se.tube42.editor.compo.data;

import java.util.*;

import se.tube42.editor.compo.service.*;

/**
 * this class represent common screen dimensions
 */
public class Format
{    
    public int w, h;
    public String name;
    public boolean enabled;
    
    /* list of regions */
    public HashMap<String, RegionData> regions;
    
    public Format(String name, int w, int h, boolean landscape)
    {
        final int a = Math.min(w, h);
        final int b = Math.max(w, h);
        
        this.name = name + (landscape ? "-l" : "-p");
        this.w = landscape ? a : b;
        this.h = landscape ? b : a;
        this.enabled = false;
        this.regions = new HashMap<String, RegionData>();
    }
    
    public boolean contains(String name)
    {
        return regions.get(name) != null;
    }
    
    public RegionData getRegion(String name)
    {
        RegionData rd = regions.get(name);
        if(rd == null)
            rd = ServiceProvider.createNewRegion(this, name);

        return rd;
    }
}
