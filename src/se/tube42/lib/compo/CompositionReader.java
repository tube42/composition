package se.tube42.lib.compo;

import java.util.*;
import java.io.*;

/**
 * This class creates a composition from a binary file.
 * Rhe corresponding writer class is in the editor.
 */
public class CompositionReader
{
    // common data for the reader/writer
    public static final int FILE_VERSION = 1;
    public static final byte [] FILE_HEADER = { 
        (byte)'C', (byte) 'O', (byte) 'M',
        (byte)'P', (byte) 'O', (byte) '$'        
    };
    
    
    private InputStream is;
    
    /* package */ CompositionReader(InputStream is)
    {
        this.is = is;
    }
    
    /* package */ Composition read()
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
        final int formats_cnt = readShort();        
        final int regions_cnt = readShort();
        
        final int dummy1 = readInt(); // not used
        final int dummy2 = readInt(); // not used
        
        final String [] formats_names = new String[formats_cnt];
        final String [] regions_names = new String[regions_cnt];
        for(int i = 0; i < formats_cnt; i++) formats_names[i] = readString();        
        for(int i = 0; i < regions_cnt; i++) regions_names[i] = readString();
        
        // read values etc
        FormatTemplate [] formats = new FormatTemplate[formats_cnt];
        int [] tmp = new int[4];
        for(int i = 0; i < formats_cnt; i++) {
            int w = readShort();
            int h = readShort();
            
            RegionTemplate [] regions = new RegionTemplate[regions_cnt];
            for(int j = 0; j < regions_cnt; j++) {
                regions[j] = new RegionTemplate();
                
                for(int k = 0; k < 4; k++) tmp[k] = readShort();
                int t = readInt();
                int f = readInt();                
                regions[j].set(tmp,  t, f);
            }
            
            formats[i] = new FormatTemplate( formats_names[i], w, h, regions);
        }
        
        Composition comp = new Composition(formats, regions_names);        
        return comp;
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
