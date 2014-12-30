package org.moll_illner.dupChecker.ui;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileTreeNode extends DefaultMutableTreeNode {
    
    private String _filepath;

    public FileTreeNode(String filepath) {
        super(filepath);
        _filepath = filepath;
    }
    
    
    public String getFilepath() {
        return _filepath;
    }

}
