
package se.tube42.lib.compo;

public class RegionTemplate
{
    public static final int
          TYPE_PLAIN = 0,
          TYPE_RATIO = 1,
          TYPE_LOCK = 2,
          TYPE_LOCK_SCALED  = 3          
          ;
    
    private static int [] tmp = new int[4];
    private int [] values;
    private int types, flags;
    
    /* package */ RegionTemplate()
    {
        values = new int[4];
    }
    
    /* package */ void set(int [] values, int types, int flags)
    {
        this.types = types;
        this.flags = flags;
        
        for(int i = 0; i < 4; i++) 
            this.values[i] = values[i];        
    }
    
    /* package */ void build(Region r, int w, int h, 
              int rw, int rh, int scale)
    {  
        
        // initial values without any changes
        final int dw = (rw - w * scale) / 2;
        final int dh = (rh - h * scale) / 2;
        
        final int t0 = (types >>  0) & 0xF;
        final int t1 = (types >>  4) & 0xF;
        final int t2 = (types >>  8) & 0xF;
        final int t3 = (types >> 12) & 0xF;
        
        
        // X0
        if(t0 == TYPE_RATIO) tmp[0] = values[0] * rw / w;            
        else if(t0 == TYPE_LOCK) tmp[0] = values[0];
        else if(t0 == TYPE_LOCK_SCALED) tmp[0] = values[0] * scale;
        else tmp[0] = values[0] * scale + dw;
        
        // Y0
        if(t1 == TYPE_RATIO) tmp[1] = values[1] * rh / h;
        else if(t1 == TYPE_LOCK) tmp[1] = values[1];
        else if(t1 == TYPE_LOCK_SCALED) tmp[1] = values[1] * scale;        
        else tmp[1] = values[1] * scale + dh;
        
        // X1
        if(t2 == TYPE_RATIO) tmp[2] = values[2] * rw / w;
        else if(t2 == TYPE_LOCK) tmp[2] = rw - (w - values[2]);
        else if(t2 == TYPE_LOCK_SCALED) tmp[2] = rw - (w - values[2]) * scale;
        else tmp[2] = values[2] * scale + dw;
        
        // Y1
        if(t3 == TYPE_RATIO) tmp[3] = values[3] * rh / h;
        else if(t3 == TYPE_LOCK) tmp[3] = rh - (h - values[3]);
        else if(t3 == TYPE_LOCK_SCALED) tmp[3] = rh - (h - values[3]) * scale;
        else tmp[3] = values[3] * scale + dh;
        
        
        
        
        // write results to Region
        r.set(tmp, flags);
    }
    
    private int get_point(int p0, int p1, int scale, int type,
              int max, int real_max)
    {
        // TODO
        return p0 * scale;
    }
            
}
