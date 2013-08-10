package se.tube42.editor.compo.data;

import java.util.*;

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
    
    
    public RegionData getRegion(String name)
    {
        RegionData rd = regions.get(name);
        if(rd == null) {           
            RegionData tmp = get_similar_region(name);
            
            rd = new RegionData(name);
            regions.put(name, rd);
            
            if(tmp != null) {
                rd.copy(tmp);            
            } else {
                rd.values[0] = Database.rnd.nextInt(w / 2);
                rd.values[1] = Database.rnd.nextInt(h / 2);
                rd.values[2] = w / 2 + Database.rnd.nextInt(w / 2);
                rd.values[3] = h / 2 + Database.rnd.nextInt(h / 2);
            }
        }
        return rd;
    }
    
    // see if anyone else has a region in this name:
    private RegionData get_similar_region(String name)
    {
        for(Format f : Database.FORMATS) {
            if(f == this) continue;
            RegionData rd = f.regions.get(name);
            if(rd != null) return rd;
        }
        return null;
    }
}
