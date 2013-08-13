
package se.tube42.lib.compo;

public class RegionTemplate
{
    public static final int
          TYPE_PLAIN = 0,
          TYPE_RATIO = 1,
          TYPE_LOCK0_U = 2,
          TYPE_LOCK1_U = 3,
          TYPE_LOCK0_S = 4,
          TYPE_LOCK1_S = 5
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
        
        
        tmp[0] = get_one(t0, values[0], scale, w, rw);
        tmp[1] = get_one(t1, values[1], scale, h, rh);
        tmp[2] = get_one(t2, values[2], scale, w, rw);
        tmp[3] = get_one(t3, values[3], scale, h, rh);
        
        
        // write results to Region
        r.set(tmp, flags);
    }
    
    private int get_one(int type, int value, int scale, int top_temp, int top_real)
    {
        switch(type) {
        case TYPE_RATIO: return value * top_real / top_temp;            
        case TYPE_LOCK0_U: return value;
        case TYPE_LOCK0_S: return value * scale;
        case TYPE_LOCK1_U: return top_real - (top_temp - value);
        case TYPE_LOCK1_S: return top_real - (top_temp - value) * scale;        
        default: // TYPE_PLAIN
            final int dw = (top_real - top_temp * scale) / 2;            
            return value * scale + dw;
        }        
    }

            
}
