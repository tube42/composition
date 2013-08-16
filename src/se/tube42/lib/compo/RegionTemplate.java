
package se.tube42.lib.compo;

public class RegionTemplate
{
    public static final int
          TYPE_PLAIN = 0,
          TYPE_RATIO = 1,
          TYPE_LOCK0_U = 2,
          TYPE_LOCK1_U = 3,
          TYPE_LOCK0_S = 4,
          TYPE_LOCK1_S = 5,
          TYPE_SAME    = 6,
          TYPE_SAME_C  = 7,
          TYPE_MIN     = 8,
          TYPE_MIN_C   = 9,
          TYPE_MAX     = 10,
          TYPE_MAX_C   = 11
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
              int rw, int rh, int scale,
              boolean flip_h, boolean flip_v)
    {  
        
        // initial values without any changes
        final int dw = (rw - w * scale) / 2;
        final int dh = (rh - h * scale) / 2;
        
        final int t0 = (types >>  0) & 0xF;
        final int t1 = (types >>  4) & 0xF;
        final int t2 = (types >>  8) & 0xF;
        final int t3 = (types >> 12) & 0xF;
        
        
        // pass 1:
        tmp[0] = get_pass_one(t0, values[0], scale, w, rw);
        tmp[1] = get_pass_one(t1, values[1], scale, h, rh);
        tmp[2] = get_pass_one(t2, values[2], scale, w, rw);
        tmp[3] = get_pass_one(t3, values[3], scale, h, rh);
        
        // pass 2:
        get_pass_two(t0, 0, rw, tmp);
        get_pass_two(t1, 1, rh, tmp);
        get_pass_two(t2, 2, rw, tmp);
        get_pass_two(t3, 3, rh, tmp);        
        
        // flip it?
        if(flip_h) {
            tmp[0] = rw - tmp[0];
            tmp[2] = rw - tmp[2];
        }
        if(flip_v) {
            tmp[1] = rh - tmp[1];
            tmp[3] = rh - tmp[3];
        }
        
        // write results to Region
        r.set(tmp, flags);
    }
    
    private int get_pass_one(int type, int value, int scale, int top_temp, int top_real)
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
    
    private void get_pass_two(int type, int index, int top, int [] values)
    {
        final int index_other = (index + 2) & 3;
        final int me = values[index];
        final int other = values[ index_other];
        final int d0 = values[2] - values[0];
        final int d1 = values[3] - values[1];
        final int dmin = Math.min(d0, d1);
        final int dmax = Math.max(d0, d1);
        final int size_this  = (index == 0 || index == 2) ? d0 : d1;
        final int size_other = (index == 0 || index == 2) ? d1 : d0;
        
        
        switch(type) {
        case TYPE_SAME:
        case TYPE_SAME_C:
            values[index] = values[index_other] + ((index < index_other) ? -d1 : +d1);            
            break;
        case TYPE_MIN_C:
        case TYPE_MIN:
            values[index] = values[index_other] + ((index < index_other) ? -dmin : +dmax);
            break;            
            
        case TYPE_MAX_C:
        case TYPE_MAX:
            values[index] = values[index_other] + ((index < index_other) ? -dmax : +dmax);            
            break;
        }
        
        // CENTER IT!
        switch(type) {
        case TYPE_SAME_C:
        case TYPE_MIN_C:
        case TYPE_MAX_C:
            final int dnow = values[index_other] - values[index];
            values[index] = (top - dnow) / 2;
            values[index_other] = values[index] + dnow;
            break;

        }
        
            
        /*
        if(t0 == TYPE_MIN) tmp[0] = tmp[2] - (tmp[3] - tmp[1]);       
        if(t1 == TYPE_SAME) tmp[1] = tmp[3] - (tmp[2] - tmp[0]);
        if(t2 == TYPE_SAME) tmp[2] = tmp[0] + (tmp[3] - tmp[1]);
        if(t3 == TYPE_SAME) tmp[3] = tmp[1] + (tmp[2] - tmp[0]);
        
        
        if(t0 == TYPE_SAME) tmp[0] = tmp[2] - (tmp[3] - tmp[1]);       
        if(t1 == TYPE_SAME) tmp[1] = tmp[3] - (tmp[2] - tmp[0]);
        if(t2 == TYPE_SAME) tmp[2] = tmp[0] + (tmp[3] - tmp[1]);
        if(t3 == TYPE_SAME) tmp[3] = tmp[1] + (tmp[2] - tmp[0]);
        
        if(t0 == TYPE_SAME_C || t2 == TYPE_SAME_C) {
            final int h1 = tmp[3] - tmp[1];
            tmp[0] = (rw - h1) / 2;
            tmp[2] = tmp[0] + h1;
        }
        if(t1 == TYPE_SAME_C || t3 == TYPE_SAME_C) {
            final int w1 = tmp[2] - tmp[0];
            tmp[1] = (rh - w1) / 2;
            tmp[3] = tmp[1] + w1;
            
        }
*/        
    }
            
}
