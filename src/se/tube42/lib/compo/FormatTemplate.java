
package se.tube42.lib.compo;

/* package */ final class FormatTemplate
{
    private int w, h, rw, rh;    
    private RegionTemplate [] regions;
    private String name;
    
    public FormatTemplate(String name, int w, int h, RegionTemplate [] regions)
    {
        this.name = name;
        this.w = w;
        this.h = h;
        this.regions = regions;
    }
    
    public int getWidth() { return w; }
    public int getHeight() { return h; }
    public String getName() { return name; }
    
    /* package */ void build(Region [] regions_, int rw, int rh, int scale)
    {
        this.rw = rw;
        this.rh = rh;
        
        for(int i = 0; i < regions.length; i++)
            regions[i].build(regions_[i], w, h, rw, rh, scale);       
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
