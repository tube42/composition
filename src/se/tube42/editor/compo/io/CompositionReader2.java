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
    private DataInputStream is;

    public CompositionReader2(InputStream is)
    {
        this.is = new DataInputStream(is);
    }


    public void read()
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
        final int fcnt = is.readShort();
        final int rcnt = is.readShort();

        // other data
        Database.regions_hidden = is.readInt();
        final int global_flags = is.readInt();
        Database.global_alignment = global_flags & 7;

        // get the strings
        ArrayList<String> fnames = new ArrayList<String>();
        ArrayList<String> rnames = new ArrayList<String>();
        for(int i = 0; i < fcnt; i++) fnames.add(is.readUTF());
        for(int i = 0; i < rcnt; i++) rnames.add(is.readUTF());

        // ...
        HashMap<String, Format> fmap = new HashMap<String, Format>();

        Database.regions = rnames;

        for(Format f : Database.FORMATS) {
            fmap.put(f.name, f);
            f.enabled = false;
        }

        for(String s : fnames) {
            Format f = fmap.get(s);
            if(f == null) new Format("dummy", 1, 1, true); // avoid crash
            f.enabled = true;

            int w = is.readShort();
            int h = is.readShort();

            for(String r : rnames) {
                f.getRegion(r).flags = is.readInt();
                is.readInt(); // reserved
            }

            AnchorSequence as = new AnchorSequence();
            as.load(is, rcnt);
            as.populate(f, rnames);
        }
    }
}
