package se.tube42.editor.compo;

import se.tube42.editor.compo.data.*;

public class Main
{
    public static void main(String [] args)
    {
        // DEBUG:
        Database.regions.add("base");
        Database.regions.add("menu_area");
        
        MainWindow mw = new MainWindow();        
    }
}
