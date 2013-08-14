package se.tube42.editor.compo.data;

/**
 * editable version of Region
 */
public class RegionData
{   
    public int []values;
    public int []types;
    public int flags;
    
    private String name;
    
    public RegionData(String name)
    {
        this.values = new int[] { 10, 10, 20, 20};
        this.types = new int[] {0, 0, 0, 0};
        this.flags = 0;
        this.name = name;
    }
    
    public String getName() { return name; }
    
    /** copy data from another instance */
    public void copy(RegionData rd)
    {
        for(int i = 0; i < values.length; i++) values[i] = rd.values[i];
        for(int i = 0; i < types.length; i++) types[i] = rd.types[i];
        flags = rd.flags;
    }
    
    /** point is inside this region? */
    public boolean hit(int x, int y)
    {
        if(Math.min(values[0], values[2]) > x) return false;
        if(Math.max(values[0], values[2]) < x) return false;
        if(Math.min(values[1], values[3]) > y) return false;
        if(Math.max(values[1], values[3]) < y) return false;        
        return true;
    }
}
