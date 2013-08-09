package se.tube42.editor.compo.data;

/**
 * this class represent common screen dimensions
 */
public class Format
{    
    public int w, h;
    public String name;
    public boolean enabled;
    
    public Format(String name, int w, int h, boolean landscape)
    {
        final int a = Math.min(w, h);
        final int b = Math.max(w, h);
        
        this.name = name + (landscape ? "-l" : "-p");
        this.w = landscape ? a : b;
        this.h = landscape ? b : a;
        this.enabled = true;
    }
}
