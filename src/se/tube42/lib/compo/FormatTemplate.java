
package se.tube42.lib.compo;

/* package */ final class FormatTemplate
{
    private int w, h, rw, rh;    
    private String name;
    private int [] rflags, rassign, map;
    
    public FormatTemplate(String name, int w, int h, int [] rflags, int [] rassign)
    {
        this.name = name;
        this.w = w;
        this.h = h;
        this.rflags = rflags;
        this.rassign = rassign;
        
        // reverse map for indices
        this.map = new int[rassign.length / 4];
        for(int i = 0; i < 8; i++) 
            map[i] = i;
        for(int i = 8; i < map.length; i++)
            map[ rassign[ i * 4 + 0] ] = i;
    }
    
    public int getWidth() { return w; }
    public int getHeight() { return h; }
    public String getName() { return name; }
    
    /* package */ void build(Region [] regions_, int rw, int rh, int scale, boolean flip_h, boolean flip_v)
    {
        this.rw = rw;
        this.rh = rh;
        
        // insert the screen and format regions
        rassign[ 0 * 4 + 3] = 0; // sx
        rassign[ 1 * 4 + 3] = 0; // sy
        rassign[ 2 * 4 + 3] = rw;
        rassign[ 3 * 4 + 3] = rh;
        
        rassign[ 4 * 4 + 3] = (rw - w * scale) / 2;
        rassign[ 5 * 4 + 3] = (rh - h * scale) / 2;
        rassign[ 6 * 4 + 3] = (rw + w * scale) / 2;
        rassign[ 7 * 4 + 3] = (rh + h * scale) / 2;
        
        // assign sequence
        for(int i = 8 * 4; i < rassign.length; i += 4) {
            final int target = map[rassign[i + 1]];
            rassign[ i + 3] = rassign[ i + 2] + rassign[ target * 4 + 3];
        }        
                
        // write to Region
        for(int i = 8 * 4; i < rassign.length; i += 4) {
            final int value = rassign[i + 3];
            final int index = rassign[i + 0];
            final int rn = (index - 8) / 4;
            final int vn = (index) & 3;
            
            final Region r = regions_[rn];
            
            if(vn == 0) r.x = value;
            else if(vn == 1) r.y = value;
            else if(vn == 2) r.w = value;
            else r.h = value;
        }        
        
        // update size and set flags
        for(int i = 0; i < regions_.length; i++) {
            regions_[i].flags = rflags[i];
            regions_[i].w -= regions_[i].x; // from (x0, x1) to (x0, w)
            regions_[i].h -= regions_[i].y; // from (y0, y1) to (y0, h)
        }
    }
    
    
    /* package */ int cost(int w, int h, int scale)
    {
        final int dx = w - this.w * scale;
        final int dy = h - this.h * scale;
        
        if(dx >= 0 && dy >= 0) {
            final int ret = dx * dx + dy * dy;
            if(ret >= 0) return ret;
        }
        
        // the division is to avoid wrap-around error later
        // when we multiply the cost with some penalty :(
        return Integer.MAX_VALUE / 64;
    }    
}
