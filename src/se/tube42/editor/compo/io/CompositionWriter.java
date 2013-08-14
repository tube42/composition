package se.tube42.editor.compo.io;

import java.io.*;
import java.util.*;

import se.tube42.lib.compo.*;
import se.tube42.editor.compo.data.*;


/**
 * This class creates a binary compo bundle.
 * Rhe corresponding reader class is in the main library
 */
public class CompositionWriter
{
    private OutputStream os;
    public CompositionWriter(OutputStream os)
    {
        this.os = os;
    }
    
    
    public void write()
          throws IOException
    {        
        try {
            final ArrayList<Format> formats = get_enabled_formats();
            final ArrayList<String> regions = Database.regions;
            
            if(formats.size() == 0)
                throw new IOException("Nothing to export :(");
            
            // write header
            os.write(CompositionReader.FILE_HEADER);
            writeShort(CompositionReader.FILE_VERSION);
            
            // write sizes and names
            writeShort(formats.size());        
            writeShort(regions.size());
            
            writeInt(Database.regions_hidden);
            writeInt(0); // reserver
            
            for(Format f : formats) writeString(f.name);
            for(String s : regions) writeString(s);
            
            // now save the data
            for(Format f : formats) save_format(f, regions);
        } finally {
            os.flush();
            os.close();
        }
    }
    
    private void writeShort(int n)
          throws IOException
    {
        os.write( (n >> 8) & 0xFF);
        os.write( (n >> 0) & 0xFF);
    }
    private void writeInt(int n)
          throws IOException
    {
        os.write( (n >> 24) & 0xFF);
        os.write( (n >> 16) & 0xFF);
        os.write( (n >>  8) & 0xFF);
        os.write( (n >>  0) & 0xFF);
    }
    
    private void writeString(String s)
          throws IOException
    {
        final int len = s.length();
        writeShort(len);
        for(int i = 0; i < len; i++)
            os.write( (int)s.charAt(i));
    }
    
    private ArrayList<Format> get_enabled_formats()
    {
        ArrayList<Format> ret = new ArrayList<Format>();
        for(Format f : Database.FORMATS)
            if(f.enabled)
                ret.add(f);
        return ret;
    }
    
    private void save_format(final Format f, final ArrayList<String> rs)
          throws IOException
    {
        // writeString(f.name);
        writeShort(f.w);
        writeShort(f.h);
        for(String name : rs) {
            final RegionData r = f.getRegion(name);
            save_region( r);
        }
    }
    private void save_region(final RegionData r)
          throws IOException
    {
        int types = 0;
        for(int i = 0; i < 4; i++)
            types |= r.types[i] << (i * 4);
        
        for(int i = 0; i < 4; i++)
            writeShort( r.values[i]);
        
        writeInt(types);
        writeInt(r.flags);
    }    
}
