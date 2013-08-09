
package se.tube42.lib.compo;

public class Region
{
    /* package */ int x, y, w, h, flags;
    /* package */ String id;
        
    /* package */ void set(String id, int x0, int y0, int x1, int y1)
    {
        this.id = id;
        this.x = Math.min(x0, x1);
        this.y = Math.min(y0, y1);
        this.w = Math.abs(x0 - x1);
        this.h = Math.abs(y0 - y1);
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getW() { return w; }
    public int getH() { return h; }
    public String getId() { return id; }    
}
