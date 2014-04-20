package org.moll_illner.dupChecker.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainWindow extends JFrame implements ActionListener {
    
    private JMenuItem _quitMenuItem;
    
    public MainWindow() {
        
        setSize(600, 400);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenuItem fileMenuItem = new JMenuItem("File");
        menuBar.add(fileMenuItem);
        
        _quitMenuItem = new JMenuItem("Quit");
        fileMenuItem.add(_quitMenuItem);
        
        _quitMenuItem.addActionListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == _quitMenuItem) {
            System.exit(0);
        }
        
    }
    
    

}
