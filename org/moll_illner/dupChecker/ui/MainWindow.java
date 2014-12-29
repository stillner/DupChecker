package org.moll_illner.dupChecker.ui;

import java.awt.BorderLayout;
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
import javax.swing.tree.TreePath;

import org.moll_illner.dupChecker.FileRef;

public class MainWindow extends JFrame implements ActionListener {
    private JMenuItem _quitMenuItem;
    private DefaultMutableTreeNode _rootNode;
    private JTree _tree;


    public MainWindow() {
        createUI();
    }


    private void createUI() {
        setLayout(new BorderLayout());
        setSize(600, 400);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JPanel mainPanel = new JPanel(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);

        _rootNode = new DefaultMutableTreeNode("Root node");
        _tree = new JTree(_rootNode);
        JScrollPane scrollPane = new JScrollPane(_tree);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        _quitMenuItem = new JMenuItem("Quit");
        fileMenu.add(_quitMenuItem);

        _quitMenuItem.addActionListener(this);
        _tree.addMouseListener(new TreeContextMenu());
    }


    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == _quitMenuItem) {
            System.exit(0);
        }
    }


    public void displayFiles(Map<Long, List<FileRef>> filesBySize) {
        _rootNode.removeAllChildren();
        if (filesBySize != null) {
            for (Map.Entry<Long, List<FileRef>> entry: filesBySize.entrySet()) {
                List<FileRef> files = entry.getValue();
                if ((files != null) && files.size() > 0) {
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


    public void displayFilesBySizeAndCRC(Map<Long, Map<String, List<FileRef>>> filesBySizeAndCRC) {
        _rootNode.removeAllChildren();
        if (filesBySizeAndCRC != null) {
            for (Map.Entry<Long, Map<String, List<FileRef>>> entry: filesBySizeAndCRC.entrySet()) {
                Map<String, List<FileRef>> filesByCRC = entry.getValue();
                if ((filesByCRC != null) && filesByCRC.size() > 0) {
                    DefaultMutableTreeNode sizeNode = 
                            new DefaultMutableTreeNode(entry.getKey().toString());
                    _rootNode.add(sizeNode);
                    _tree.expandPath(new TreePath(sizeNode.getPath()));
                    for (Map.Entry<String, List<FileRef>> fileEntry: filesByCRC.entrySet()) {
                        List<FileRef> files = fileEntry.getValue();
                        if ((files != null) && files.size() > 0) {
                            DefaultMutableTreeNode crcNode = 
                                    new DefaultMutableTreeNode(fileEntry.getKey().toString() + " (" + files.size() + ")");

                            for (FileRef f: files) {
                                DefaultMutableTreeNode fileNode = new DefaultMutableTreeNode(f.getPath());

                                crcNode.add(fileNode);
                            }
                            sizeNode.add(crcNode);
                            _tree.expandPath(new TreePath(crcNode.getPath()));
                        }
                    }
                }
            }
        }
        _tree.expandPath(new TreePath(_rootNode));
    }

}
