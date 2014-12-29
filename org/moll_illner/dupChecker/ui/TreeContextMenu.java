package org.moll_illner.dupChecker.ui;

import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.MouseInputAdapter;
import javax.swing.tree.TreePath;

public class TreeContextMenu extends MouseInputAdapter {
    
    private void myPopupEvent(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        JTree tree = (JTree)e.getSource();
        TreePath path = tree.getPathForLocation(x, y);
        if (path == null) {
            return; 
        }
        
        tree.setSelectionPath(path);
        String obj = path.getLastPathComponent().getClass().toString();
        String label = "The object is a " + obj; //.getTreeLabel();
        JPopupMenu popup = new JPopupMenu();
        popup.add(new JMenuItem(label));
        popup.show(tree, x, y);
    }
    
    
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) myPopupEvent(e);
    }
    
    
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) myPopupEvent(e);
    }
}