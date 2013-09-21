package se.tube42.editor.compo.data;

import java.util.*;

/**
 * Data goes here
 */
public class Database
{
    /** a random number generator for evryone to share */
    public static final Random rnd = new Random();
    
    /** list of all available formats */
    public static final Format [] FORMATS = 
    {
        new Format("2600", 192, 160, true),
        new Format("fruit/4", 120, 80, true),
        new Format("fruit/4", 120, 80, false),        
        new Format("fruit/2", 240, 160, true),
        new Format("fruit/2", 240, 160, false),
        new Format("ZX", 256, 192, true),        
        new Format("NES", 256, 240, true),        
        new Format("QVGA", 240, 320, true),
        new Format("QVGA", 240, 320, false),        
        new Format("QVGA", 240, 320, true),
        new Format("QVGA", 240, 320, false),
        new Format("HVGA", 320, 480, true),
        new Format("HVGA", 320, 480, false),
        new Format("VGA", 480, 640, true),
        new Format("VGA", 480, 640, false),
        new Format("qHD", 540, 960, true),
        new Format("qHD", 540, 960, false),
        new Format("WVGA800", 480, 800, true),
        new Format("WVGA800", 480, 800, false),        
        new Format("WXVGA", 800, 1280, true),
        new Format("WXVGA", 800, 1280, false)        
    };
    
    /** name of types */
    public static final String [] TYPES = {
        "Point 1", "Point 2"
        /*
        "PLAIN", "RATIO", 
        "LOCK0-u", "LOCK1-u", "LOCK0-s", "LOCK1-s",
           "SAME", "SAME-c", "MIN", "MIN-C", "MAX", "MAX-c"
         */
    };
    
    
    /** list of all Regions */
    public static ArrayList<String> regions = new ArrayList<String>();
    
    public static RegionData rscreen = new RegionData("_screen");
    public static RegionData rformat = new RegionData("_format");
    
    public static Format current_format = null;
    public static String current_region = null;
    
    // global flags:
    public static int regions_hidden  = 0;
    public static int global_alignment = 1;
    
    // editor grid format
    public static int grid_size = 12;
    public static boolean grid_enable = false, grid_show = true;
    
    // saving:
    public static String filename = null;
}
