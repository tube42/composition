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
    
    
    private DataInputStream is;
    
    /* package */ CompositionReader(InputStream is)
    {
        this.is = new DataInputStream(is);
    }
    
    /* package */ Composition read()
          throws IOException
    {
        
        // read header:
        for(byte b : CompositionReader.FILE_HEADER) {
            byte c = (byte) is.read();
            if(b != c) throw new IOException("Format error (header)");
        }
        if(is.readShort() != CompositionReader.FILE_VERSION) 
            throw new IOException("Format error (version)");
        
        // read names
        final int formats_cnt = is.readShort();        
        final int regions_cnt = is.readShort();
        
        final int dummy1 = is.readInt(); // not used
        final int dummy2 = is.readInt(); // not used
        
        final String [] formats_names = new String[formats_cnt];
        final String [] regions_names = new String[regions_cnt];
        for(int i = 0; i < formats_cnt; i++) formats_names[i] = is.readUTF();        
        for(int i = 0; i < regions_cnt; i++) regions_names[i] = is.readUTF();
        
        // read values etc
        FormatTemplate [] formats = new FormatTemplate[formats_cnt];
        int [] tmp = new int[4];
        for(int i = 0; i < formats_cnt; i++) {
            int w = is.readShort();
            int h = is.readShort();
            
            // get flags
            int [] rflags = new int[regions_cnt];
            for(int j = 0; j < rflags.length; j++) {
                rflags[j] = is.readInt();
                is.readInt(); // reserved                
            }
            
            // get assignment list
            int [] rassign = new int[(regions_cnt + 2) * 4 * 4]; // <index, target, diff, value>
            for(int j = 8 * 4; j < rassign.length; j += 4) {
                rassign[j + 0] = (int) is.readShort();
                rassign[j + 1] = (int) is.readShort();
                rassign[j + 2] = (int) is.readShort();
            }
                        
            formats[i] = new FormatTemplate( formats_names[i], w, h, rflags, rassign);
        }
        
        Composition comp = new Composition(formats, regions_names);        
        return comp;
    }
    
}
