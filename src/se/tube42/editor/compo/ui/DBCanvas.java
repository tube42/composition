package se.tube42.editor.compo.ui;

import java.awt.*;

/**
 * double buffered canvas, for your flicker free enjoyment
 */

public abstract class DBCanvas extends Canvas
{
    private int w, h;
    private Graphics g;
    private Image image;
    
    public DBCanvas()
    {
        this.image = null;
        this.g = null;
        this.w = -1;
        this.h = -1;
    }
    
    /** 
     * implement this instead of paint() for
     * some double-buffering magic. 
     * 
     * Gosling sure knew his shit when he designed Java...
     */
    public abstract void bufferedPaint(Graphics g);
    
    public final void update(Graphics g)
    {
        paint(g);
    }
    
    public final void paint(Graphics g_)
    {
        final int w_ = getWidth();
        final int h_ = getHeight();
        
        if(image == null || w_ != w || h_ != h)
            create_buffer(w_, h_);
        
        bufferedPaint(g);
        g_.drawImage(image, 0, 0, this);
    }
    
    
    private void create_buffer(int w, int h)
    {
        
        if(g != null) {
            g.dispose();
            g = null;
        }
        
        if(image != null) {
            // image.dispose();
            image = null;
        }
        
        this.w = w;
        this.h = h;
        this.image = createImage(w, h);
        this.g = image.getGraphics();
    }    
}
