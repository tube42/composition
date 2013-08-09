package se.tube42.editor.compo;


public class Main
{
    public static void main(String [] args)
    {
        EditWindow ew = new EditWindow();
        TestWindow tw = new TestWindow();
        MainWindow mw = new MainWindow();
        
        mw.setLocation(0, 0);
        ew.setLocation(mw.getWidth(), 0);
        tw.setLocation(mw.getWidth(), ew.getHeight());
    }
}
