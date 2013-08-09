package se.tube42.editor.compo.data;

/**
 * Data goes here
 */
public class Database
{
    /** list of all available formats */
    
    /** list of all formats */
    public static final Format [] FORMATS = 
    {
        new Format("QVGA", 240, 320, true),
        new Format("QVGA", 240, 320, false),
        new Format("HVGA", 320, 480, true),
        new Format("HVGA", 320, 480, false),
        new Format("VGA", 480, 640, true),
        new Format("VGA", 480, 640, false),
        new Format("WVGA800", 480, 800, true),
        new Format("WVGA800", 480, 800, false),        
        new Format("WXVGA", 800, 1280, true),
        new Format("WXVGA", 800, 1280, false)        
    };


}
