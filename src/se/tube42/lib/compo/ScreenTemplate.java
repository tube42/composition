
package se.tube42.lib.compo;

public class ScreenTemplate
{
    public int w, h, rw, rh;
    public RegionTemplate [] regions;
    
    public ScreenTemplate(int w, int h, RegionTemplate [] regions)
    {
        this.w = w;
        this.h = h;
        this.regions = regions;
    }
    
    /* package */ void build(Composition compo)
    {
        this.rw = compo.getW();
        this.rh = compo.getH();        
        
        for(int i = 0; i < regions.length; i++) {
            final RegionTemplate rt = regions[i];
            final Region r = compo.get(rt.id);
            rt.build(rw, rh, r);
        }
    }
}
