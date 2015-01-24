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
    private DataOutputStream os;

    public CompositionWriter(OutputStream os)
    {
        this.os = new DataOutputStream(os);
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
            os.writeShort(CompositionReader.FILE_VERSION);

            // write sizes and names
            os.writeShort(formats.size());
            os.writeShort(regions.size());

            os.writeInt(Database.regions_hidden);
            os.writeInt(
                      (Database.global_alignment & 7)
                      );

            for(Format f : formats) os.writeUTF(f.name);
            for(String s : regions) os.writeUTF(s);

            // now save the data
            for(Format f : formats) {
                os.writeShort(f.w);
                os.writeShort(f.h);

                for(String r : regions) {
                    os.writeInt( f.getRegion(r).flags);
                    os.writeInt(0); // reserved
                }

                AnchorSequence as = new AnchorSequence();
                as.build(f, regions);
                as.save(f, os);
            }
        } finally {
            os.flush();
            os.close();
        }
    }

    private ArrayList<Format> get_enabled_formats()
    {
        ArrayList<Format> ret = new ArrayList<Format>();
        for(Format f : Database.FORMATS)
            if(f.enabled)
                ret.add(f);
        return ret;
    }
}
