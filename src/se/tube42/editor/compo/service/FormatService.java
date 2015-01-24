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

    /* package */ static void alignCurrentFormat()
    {
        final Format f = Database.current_format;
        if(f == null)
            return;

        final int align_size = 1 << Database.global_alignment;
        final int align_mask = align_size - 1;
        final int align_inv_mask = ~align_mask;

        for(String name : Database.regions) {
            RegionData rd = f.getRegion(name);

            for(int i = 2; i < 4; i++)
                rd.values[i] += align_mask;

            for(int i = 0; i < 4; i++)
                rd.values[i] &= align_inv_mask;
        }
    }

    /* package */ static void selectFirstFormat()
    {
        for(int i = 0; i < Database.FORMATS.length; i++) {
            if(Database.FORMATS[i].enabled) {
                Database.current_format = Database.FORMATS[i];
                return;
            }
        }
    }

}
