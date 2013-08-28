package se.tube42.editor.compo.service;

import se.tube42.editor.compo.*;
import se.tube42.editor.compo.data.*;

/* package */ class FormatService
{    
        // FORMAT
    /* package */ static void toggleCurrentFormat()
    {
        if(Database.current_format != null)
            Database.current_format.enabled ^= true;            
    }
    
    /* package */ static void clearCurrentFormat()
    {
        if(Database.current_format != null)
            Database.current_format.regions.clear();
    }
    
    
    /* package */ static int getCurrentFormatIndex()
    {
        for(int i = 0; i < Database.FORMATS.length; i++)
            if(Database.FORMATS[i] == Database.current_format)
                return i;
        return -1;
    }        
}
