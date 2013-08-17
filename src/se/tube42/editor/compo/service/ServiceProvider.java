package se.tube42.editor.compo.service;

import java.io.*;
import se.tube42.lib.compo.*;
import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.io.*;

public class ServiceProvider
{    
    // doc
    public static void newDocument()
    {
        boolean enabled = true;
        for(Format f : Database.FORMATS) {
            f.enabled = enabled;
            f.regions.clear();
            enabled = false;
        }
        Database.regions.clear();
        Database.current_format = Database.FORMATS[0];
        Database.current_region = null;
    }
    
    // IO
    public static boolean save(String filename)
    {
        OutputStream os = null;        
        try {
            os = new FileOutputStream(filename);            
            CompositionWriter cw = new CompositionWriter(os);
            cw.write();
        } catch(Exception e) {
            System.out.println("SAVE ERROR: " + e);
            return false;
        }  finally {
            try {
                if(os != null) {
                    os.flush();
                    os.close();
                }
            } catch(IOException e) { }
        }
        
        Database.filename = filename;
        return true;        
    }
    
    public static boolean load(String filename)
    {
        InputStream is = null;
        try {
            is = new FileInputStream(filename);            
            CompositionReader2 c2 = new CompositionReader2(is);
            c2.read();
        } catch(Exception e) {
            System.out.println("LOAD ERROR: " + e);
            return false;
        } finally {
            try {
                if(is != null) is.close();
            } catch(IOException e) { }                
        }
        
        Database.filename = filename;        
        return true;        
    }
    
    public static void run()
    {        
        try {
            // save to memory
            ByteArrayOutputStream os = new ByteArrayOutputStream();            
            CompositionWriter cw = new CompositionWriter(os);
            cw.write();
            
            // load from memory
            byte [] data = os.toByteArray();            
            ByteArrayInputStream is = new ByteArrayInputStream(data);            
            Composition c = Composition.load(is);
            
            // show it!
            if(c != null) 
                new TestWindow(c);
            
        } catch(Exception e) {
            System.err.println("ERROR: " + e);
        }                    
    }
    
    // FORMAT
    public static void hideCurrentFormat()
    {
        if(Database.current_format != null)
            Database.current_format.enabled ^= true;            
    }
    
    public static void clearCurrentFormat()
    {
        if(Database.current_format != null)
            Database.current_format.regions.clear();
    }
    
    public static RegionData createNewRegion(Format f, String rname)
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
    
    public static int getCurrentFormatIndex()
    {
        for(int i = 0; i < Database.FORMATS.length; i++)
            if(Database.FORMATS[i] == Database.current_format)
                return i;
        return -1;
    }    
    
    // REGION
    public static void centerCurrentRegion(boolean h, boolean v)
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
    
    public static void hideCurrentRegion()
    {
        final int len = Math.min(32, Database.regions.size());
        for(int i = 0; i < len ; i++) {
            if(Database.regions.get(i).equals(Database.current_region))
                Database.regions_hidden ^= (1 << i);
        }        
    }
    
    public static void removeCurrentRegion()
    {
        Database.regions.remove(Database.current_region);
        Database.current_region = 
              Database.regions.size() > 0 ? 
              Database.regions.get(0) : null;        
    }
    
    public static int getCurrentRegionIndex()
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
    
}
