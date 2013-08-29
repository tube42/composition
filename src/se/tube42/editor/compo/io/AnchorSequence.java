package se.tube42.editor.compo.io;

import java.io.*;
import java.util.*;

import se.tube42.lib.compo.*;
import se.tube42.editor.compo.data.*;


class AnchorData
{
    public int index, target, value, diff;
    public int order = -1;    
    public RegionData region = null;
    
    public AnchorData right = null;
    public AnchorData down = null;
    
          
    /** comparator for anchor data */
    public static final Comparator<AnchorData> comparator = 
          new Comparator<AnchorData>() 
    {
        public int compare(AnchorData a, AnchorData b) 
        {
            return a.order - b.order;
        }
    };
    
    
}

/**
 * Assignment order based generator / loader
 */
public class AnchorSequence
{
    
    private AnchorData [] data = null;
    
    public void build(Format f, ArrayList<String> regions)
          throws IOException
    {        
        final int nregion = regions.size();        
        
        data = new AnchorData[(nregion  + 2) * 4];        
        for(int i = 0; i < data.length; i++) {            
            data[i] = new AnchorData();
            data[i].target = i;
            data[i].index  = i;
        }
        
        // add screen & format
        data[0].value = data[0+4].value = 0;
        data[1].value = data[1+4].value = 0;
        data[2].value = data[2+4].value = f.w;
        data[3].value = data[3+4].value = f.h;
        
        for(int i = 0; i < nregion; i++) {
            final RegionData child = f.getRegion( regions.get(i));
            for(int j = 0; j < 4; j++) {                                
                final int index1 = 8 + 4 * i + j;                
                final int target = child.targets[j];
                final int type = child.types[j];                
                final int index2 = 4 * target + (j & 1) 
                      + (type == RegionTemplate.TYPE_P0 ? 0 : 2);
                
                if(index1 == index2)
                    throw new IOException("Bad anchor in " + child.getName() );
                
                data[index1].value = child.values[j];
                data[index1].region = child;                                
                add_dep(data[index2], data[index1]);
            }                  
        }
        
        
        for(int i = 0; i < 8; i++) 
            add_order_rec(data[i], 1);
        
        for(int i = 0; i < 8; i++) 
            compute_diff_rec(data[i], 0);
        
        // sort after assignment order
        Arrays.sort(data, 8, data.length, AnchorData.comparator);        
    }
        
    public void save(Format f, DataOutputStream os)
          throws IOException
    {
        for(int i = 8; i < data.length; i++) {
            os.writeShort( data[i].index);
            os.writeShort( data[i].target);
            os.writeShort( data[i].diff);
        }
    }
    
    // ---------------------------------------------------
    
    public void load(DataInputStream is, int count)
          throws IOException
    {
        final int n = (count + 2) * 4;
        this.data = new AnchorData[n];
        
        for(int i = 0; i < 8; i++) {
            data[i] = new AnchorData();
            data[i].index = i;
        }
        
        for(int i = 8; i < data.length; i++) {
            AnchorData tmp = new AnchorData();
            tmp.index = is.readShort();
            tmp.target = is.readShort();
            tmp.diff = is.readShort();            
            data[i] = tmp;
        }
    }
    
    
    public void populate(Format f, ArrayList<String> rnames)
    {
        final int n = rnames.size();
        
        // insert frame
        data[0].value = data[0+4].value = 0;
        data[1].value = data[1+4].value = 0;
        data[2].value = data[2+4].value = f.w;
        data[3].value = data[3+4].value = f.h;
        
        // get the regions
        final RegionData [] regions = new RegionData[n];        
        for(int i = 0; i < n; i++)
            regions[i] = f.getRegion( rnames.get(i));
        
        // get the asignment reverse map
        int [] map = new int[data.length];
        for(int i = 0; i < data.length; i++)
            map[ data[i].index] = i;
        
        
        // do the assigning...
        for(int i = 8; i < data.length; i++)  {
            final int target = map[data[i].target];
            data[i].value = data[i].diff + data[ target].value;
        }
        
        // build the format
        for(int i = 8; i < data.length; i++) {
            final int index = data[i].index;
            final int rn = (index - 8) / 4;
            final int vn = (index) & 3;
            
            final RegionData r = regions[rn];            
            r.values[vn] = data[i].value;
            r.targets[vn] = data[i].target / 4;
            r.types[vn] = (data[i].target & 2) / 2;            
        }
    }
    
    // ---------------------------------------------------
    private void add_order_rec(AnchorData parent, int order)
          throws IOException
    {
        if(parent.order != -1) 
            throw new IOException("Cyclic dependency");
        
        parent.order = order;
        
        for(AnchorData child = parent.down; child != null; child = child.right) {
            add_order_rec(child, order+1);
        }
    }
    
    private void compute_diff_rec(AnchorData anchor, int ref)
    {
        anchor.diff = anchor.value - ref;
        for(AnchorData child = anchor.down; child != null; child = child.right) {
            compute_diff_rec(child, anchor.value);            
        }        
    }
    
    private void add_dep(AnchorData parent, AnchorData dep)
    {
        AnchorData tmp = parent.down;
        
        dep.target = parent.index;
        
        parent.down = dep;
        
        if(tmp != null) {
            while(dep.right != null)
                dep = dep.right;
            dep.right = tmp;
        }
    }
}
