package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;


public class StackLayout implements LayoutManager
{
    private int vgap, hgap, div;
    public StackLayout(int vgap, int hgap)
    {
        this.vgap = vgap;
        this.hgap = hgap;
        this.div = 1;
    }
    
    public StackLayout(int vgap, int hgap, int div)
    {
        this.vgap = vgap;
        this.hgap = hgap;
        this.div = Math.max(1, div);
    }
    
    // 
    public void addLayoutComponent(String name, Component comp)    
    {
    }
    
    public void removeLayoutComponent(Component comp)
    {
    }
        
    public Dimension preferredLayoutSize(Container parent)
    {
        final int len = parent.getComponentCount();
        int w = 0;
        int h = 0;
        
        for(int i = 0 ; i < len ; i++) {
            final Component m = parent.getComponent(i);
            if(!m.isVisible()) continue;
            
            final Dimension d = m.getPreferredSize();            
            w = Math.max(w, d.width);
            h += d.height;
        }
        return new Dimension((div + 1) * hgap + w * div, h + len * vgap);
    }
    
    public Dimension minimumLayoutSize(Container parent)
    {
        return preferredLayoutSize(parent);
    }
    
    public void layoutContainer(Container parent)
    {
        final int len = parent.getComponentCount();
        final int w = (parent.getWidth() - (1 + div) * hgap) / div;
        int y = vgap;
        int cnt = 0;
        
        
        for(int i = 0 ; i < len ; i++) {
            final Component m = parent.getComponent(i);
            if(!m.isVisible()) continue;
            
            final Dimension d = m.getPreferredSize();
            
            m.setSize(w , d.height);        
            m.setLocation((hgap + w) * cnt , y);
            cnt = (cnt + 1) % div;
            if(cnt == 0)
                y += d.height + vgap;
                
        }
    }

}
