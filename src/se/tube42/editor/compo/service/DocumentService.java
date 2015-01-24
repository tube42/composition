package se.tube42.editor.compo.service;

import java.io.*;
import se.tube42.lib.compo.*;
import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.io.*;

/* package */ class DocumentService
{
    /* package */ static void newDocument()
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


    /* package */ static void run()
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
}
