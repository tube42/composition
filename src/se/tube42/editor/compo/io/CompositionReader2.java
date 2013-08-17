package se.tube42.editor.compo.io;

import java.io.*;
import java.util.*;

import se.tube42.lib.compo.*;
import se.tube42.editor.compo.data.*;


/**
 * Load binary compo from binary format.
 * While CompositionReader creates a Composition, CompositionReader2 updates the Database
 */
public class CompositionReader2
{
    private InputStream is;
    
    public CompositionReader2(InputStream is)
    {
        this.is = is;
    }
    
    
    public void read()
          throws IOException
    {
        // read header:
        for(byte b : CompositionReader.FILE_HEADER) {
            byte c = (byte) is.read();
            if(b != c) throw new IOException("Format error (header)");
        }
        
        if(readShort() != CompositionReader.FILE_VERSION) 
            throw new IOException("Format error (version)");
        
        // read names
        final int fcnt = readShort();        
        final int rcnt = readShort();
        
        // other data
        Database.regions_hidden = readInt();
        final int reserved = readInt(); // not used
        
        // get the strings
        ArrayList<String> fnames = new ArrayList<String>();
        ArrayList<String> rnames = new ArrayList<String>();
        for(int i = 0; i < fcnt; i++) fnames.add(readString());
        for(int i = 0; i < rcnt; i++) rnames.add(readString());
        
        // ...
        HashMap<String, Format> fmap = new HashMap<String, Format>();        
        
        Database.regions.clear();        
        for(String rn : rnames) Database.regions.add( rn);
        
        for(Format f : Database.FORMATS) {
            fmap.put(f.name, f);
            f.enabled = false;                          
        }
        
        for(String s : fnames) {
            Format f = fmap.get(s);
            if(f == null) new Format("dummy", 1, 1, true); // avoid crash            
            f.enabled = true;
            
            int w = readShort();
            int h = readShort();
            
            for(String m : rnames) {
                RegionData r = f.getRegion(m);
                
                for(int k = 0; k < 4; k++) r.values[k] = readShort();
                int t = readInt();
                for(int k = 0; k < 4; k++) {
                    r.types[k] = t & 0xF;
                    t >>= 4;
                }
                r.flags = readInt();                
            }
        }
    }
    
    // ----------------------------------------------
    private int readShort()
          throws IOException
    {
        int ret = 0;
        for(int i = 0; i < 2; i++)
            ret = (ret << 8) | (is.read() & 0xFF);
        
        // sign extend?
        if( (ret & 0x8000) != 0) ret |= 0xFFFF0000;        
        return ret;        
    }
    private int readInt()
          throws IOException          
    {
        int ret = 0;
        for(int i = 0; i < 4; i++)
            ret = (ret << 8) | (is.read() & 0xFF);
        
        return ret;        
    }    
    private String readString()
          throws IOException          
    {
        int n = readShort();
        byte [] x = new byte[n];
        
        is.read(x);
        String s = new String(x);
        
        return s;
    }
}
