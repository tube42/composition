package se.tube42.editor.compo.service;

import se.tube42.editor.compo.data.*;

/**
 * ServiceProvider hides implementation logic from you.
 * Why? Because you are weak and there is a dangerous world out there.
 */
public class ServiceProvider
{    
    
    // IO
    public static boolean save(String filename)
    {
        return IOService.save(filename);
    }
    
    public static boolean load(String filename)
    {
        return IOService.load(filename);
    }
    
    // DOCUMENT
    public static void newDocument()
    {
        DocumentService.newDocument();
    }
        
    public static void run()
    {        
        DocumentService.run();
    }
    
    // FORMAT
    public static void toggleCurrentFormat()
    {
        FormatService.toggleCurrentFormat();
    }
    
    public static void clearCurrentFormat()
    {
        FormatService.clearCurrentFormat();
    }
    
   public static int getCurrentFormatIndex()
    {
        return FormatService.getCurrentFormatIndex();
    }    
    
    // REGION
    public static RegionData createNewRegion(Format f, String rname)
    {
        return RegionService.createNewRegion(f, rname);
    }
    
    public static void centerCurrentRegion(boolean h, boolean v)
    {
        RegionService.centerCurrentRegion(h, v);
    }        
    
    public static void toggleCurrentRegion()
    {
        RegionService.toggleCurrentRegion();
    }
        
    public static void removeCurrentRegion()
    {
        RegionService.removeCurrentRegion();
    }
    
    public static int getCurrentRegionIndex()
    {
        return RegionService.getCurrentRegionIndex();
    }
    
    public static RegionData getRegion(int index)
    {
        return RegionService.getRegion(index);
    }
    
}
