
package se.tube42.lib.compo;

public class RegionTemplate
{
    public static final int
          TYPE_PLAIN = 0,
          TYPE_RATIO = 1,
          TYPE_RELATIVE_SCREEN_1 = 2,
          TYPE_RELATIVE_SCREEN_2 = 3,
          TYPE_RELATIVE_END = 4
          ;
          
    public int x, y, w, h, flags;
    public int types;
    public String id;
    
    public void build(int rw, int rh, Region r)
    {  
        // original numbers, without any changes
        int x0 = x, y0 = y, x1 = x + w, y1 = y + h;
        
        // how much extra room did we get?
        final int dw = (rw - w) / 2;
        final int dh = (rh - h) / 2;          
        
        // extrat types
        final int tx0 = ( types >> 0 ) & 0xF;
        final int ty0 = ( types >> 4 ) & 0xF;
        final int tx1 = ( types >> 8 ) & 0xF;
        final int ty1 = ( types >> 12) & 0xF;
        
        // TODO: adjust the build region from template given the screen size deltas
        
        
        // write results to Regin
        r.flags = flags;
        r.x = x0;
        r.y = y0;
        r.w = x1 - x0;
        r.h = y1 - y0;
        
    }
}
