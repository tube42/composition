package se.tube42.editor.compo.service;

import java.io.*;
import se.tube42.lib.compo.*;
import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.io.*;

/* package */ class IOService
{

    // IO
    /* package */ static boolean save(String filename)
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

    /* package */ static boolean load(String filename)
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
}
