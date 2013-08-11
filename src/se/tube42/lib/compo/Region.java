
package se.tube42.lib.compo;

public class Region
{
    private int x, y, w, h, flags;
    private String name;
    
    /* package */ Region(String name)
    {
        this.name = name;
    }
    
    /* package */ void set(int [] values, int flags)
    {
        this.x = Math.min(values[0] , values[2]);
        this.y = Math.min(values[1] , values[3]);
        this.w = Math.abs(values[0] - values[2]);
        this.h = Math.abs(values[1] - values[3]);        
        this.flags = flags;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getW() { return w; }
    public int getH() { return h; }
    
    public int getFlags() { return flags; }

    public String getName() { return name; }    
}
