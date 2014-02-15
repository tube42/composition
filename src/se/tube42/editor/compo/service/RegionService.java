package se.tube42.editor.compo.service;

import java.io.*;
import se.tube42.lib.compo.*;
import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.io.*;

/* package */ class RegionService
{    
    
    /* package */ static RegionData getRegion(int index)
    {        
        if(index == 0) return Database.rscreen;
        if(index == 1) return Database.rformat;
        
        if(Database.current_format == null) return null;
        return Database.current_format.getRegion(
                  Database.regions.get(index -2)
                  );        
    }
    
    /* package */ static RegionData createNewRegion(Format f, String rname)
    {
        if(f.contains(rname)) return null;
        
        // find the best region mathing this
        int best_score = Integer.MAX_VALUE;
        Format best_format = null;
        for(Format f2 : Database.FORMATS) {
            if(f2 == f) continue;
            if(!f2.contains(rname)) continue;
            int score = Math.abs(f2.w - f.w) + Math.abs(f2.h - f.h);
            if(score < best_score) {
                best_score = score;
                best_format = f2;
            }
        }
        
        final RegionData r = new RegionData(rname);
        if(best_format == null) {
            r.values[0] = Database.rnd.nextInt(f.w / 2);
            r.values[1] = Database.rnd.nextInt(f.h / 2);
            r.values[2] = f.w / 2 + Database.rnd.nextInt(f.w / 2);
            r.values[3] = f.h / 2 + Database.rnd.nextInt(f.h / 2);            
        } else {
            final RegionData r2 = best_format.getRegion(rname);
            r.copy( r2);
            
            // shrink/streach to new size
            r.values[0] = r.values[0] * f.w / best_format.w;
            r.values[1] = r.values[1] * f.h / best_format.h;
            r.values[2] = r.values[2] * f.w / best_format.w;
            r.values[3] = r.values[3] * f.h / best_format.h;
        }
        f.regions.put(rname, r);
        return r;
    }

    /* package */ static void centerCurrentRegion(boolean h, boolean v)
    {
        final Format f = Database.current_format;
        if( f != null && Database.current_region != null) {
            RegionData r = f.getRegion(Database.current_region);
            
            if(h) {
                final int w = r.values[2] - r.values[0];
                r.values[0] = (f.w - w) / 2;
                r.values[2] = r.values[0] + w;
            }
            
            if(v) {
                final int h2 = r.values[3] - r.values[1];
                r.values[1] = (f.h - h2) / 2;
                r.values[3] = r.values[1] + h2;                
            }                        
        }
    }
    
    /* package */ static void toggleCurrentRegion()
    {
        final int len = Math.min(32, Database.regions.size());
        for(int i = 0; i < len ; i++) {
            if(Database.regions.get(i).equals(Database.current_region))
                Database.regions_hidden ^= (1 << i);
        }        
    }
    
    // this indicates that 
    /* package */ static void removeAnchorTarget(String name, int index)
    {
        for(Format f : Database.FORMATS) {
            f.removeAnchorTarget(name, index);
        }
    }
    
    /* package */ static void removeCurrentRegion()
    {
        int n = getCurrentRegionIndex();
        if(n == -1) return;
        
        // replace the 
        removeAnchorTarget(Database.current_region, n);
        
        Database.regions.remove(Database.current_region);
        Database.current_region = 
              Database.regions.size() > 0 ? 
              Database.regions.get(0) : null;        
    }
    
    /* package */ static int getCurrentRegionIndex()
    {
        final String str = Database.current_region;
        if(str == null) return -1;
        
        int i = 0;
        for(String str2 : Database.regions) {
            if(str.equals(str2))
                return i;
            i++;
        }
        
        return -1;
    }
    
    /* package */ static void selectFirstRegion()
    {
        if(Database.regions.size() > 0)
            Database.current_region = Database.regions.get(0);
    }
    
    
}
