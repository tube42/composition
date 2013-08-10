package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.event.*;


public class StackLayout implements LayoutManager
{
    private int vgap, hgap;
    
    public StackLayout(int vgap, int hgap)
    {
        this.vgap = vgap;
        this.hgap = hgap;
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
            final Dimension d = m.getPreferredSize();
            w = Math.max(w, d.width);
            h += d.height;
        }
        return new Dimension(2 * hgap + w, h + len * vgap);
    }
    
    public Dimension minimumLayoutSize(Container parent)
    {
        return preferredLayoutSize(parent);
    }
    
    public void layoutContainer(Container parent)
    {
        final int len = parent.getComponentCount();
        final int w = parent.getWidth() - 2 * hgap;
        int y = vgap;

        for(int i = 0 ; i < len ; i++) {
            final Component m = parent.getComponent(i);
            if(!m.isVisible()) continue;
            
            final Dimension d = m.getPreferredSize();
            m.setSize(w, d.height);        
            m.setLocation(hgap, y);
            
            y += d.height + vgap;
        }
    }

}
