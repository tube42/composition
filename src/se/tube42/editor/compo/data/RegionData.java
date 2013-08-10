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
}
