
package se.tube42.lib.compo;

public final class Region
{
    /* package */ int x, y, w, h, flags;
    private String name;
    
    /* package */ Region(String name)
    {
        this.name = name;
    }
        
    public int getX() { return x; }
    public int getY() { return y; }
    public int getW() { return w; }
    public int getH() { return h; }
    
    public int getFlags() { return flags; }

    public String getName() { return name; }    
}
