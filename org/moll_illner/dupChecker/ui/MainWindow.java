package org.moll_illner.dupChecker.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.moll_illner.dupChecker.FileRef;

public class MainWindow extends JFrame implements ActionListener {
    
    private JMenuItem _quitMenuItem;
    private DefaultMutableTreeNode _rootNode;
    
    public MainWindow() {
        
        setSize(600, 400);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        JPanel mainPanel = new JPanel();
        this.add(mainPanel);
        
        _rootNode = new DefaultMutableTreeNode("Root node");
        JTree tree = new JTree(_rootNode);
        JScrollPane scrollPane = new JScrollPane(tree);
        
        mainPanel.add(scrollPane);
        
        _quitMenuItem = new JMenuItem("Quit");
        fileMenu.add(_quitMenuItem);
        
        _quitMenuItem.addActionListener(this);
    }

    
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == _quitMenuItem) {
            System.exit(0);
        }
    }

    
    public void displayFiles(Map<Long, List<FileRef>> filesBySize) {
        if (filesBySize != null) {
            for (Map.Entry<Long, List<FileRef>> entry: filesBySize.entrySet()) {
                List<FileRef> files = entry.getValue();
                if (files != null) {
                	DefaultMutableTreeNode sizeNode = 
                		new DefaultMutableTreeNode(entry.getKey().toString() + " (" + files.size() + ")");
                
                    for (FileRef f: files) {
                        DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(f.getPath());
                        sizeNode.add(fileNode);
                    }
                    _rootNode.add(sizeNode);
                }
            }
        }

    }

}
