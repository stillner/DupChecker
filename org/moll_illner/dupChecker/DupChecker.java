package org.moll_illner.dupChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.moll_illner.dupChecker.ui.MainWindow;

public class DupChecker {

    public static void main(String[] args) {
        List<StartingPoint> startingPoints = new ArrayList<StartingPoint>();
        startingPoints.add(new StartingPoint("/home/stillner/Anja"));
        Crawler crawler = new Crawler(startingPoints);
        Map<Long, List<FileRef>> duplicateFilesBySize = crawler.crawl();
        if (duplicateFilesBySize != null) {
            for (Map.Entry<Long, List<FileRef>> entry: duplicateFilesBySize.entrySet()) {
                System.out.println();
                System.out.println(entry.getKey());
                List<FileRef> files = entry.getValue();
                if (files != null) {
                    for (FileRef f: files) {
                        System.out.println(" " + f.getPath());
                    }
                }
            }
        }
        
        MainWindow w = new MainWindow();
        w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        w.displayFiles(duplicateFilesBySize);
        w.setVisible(true);
    }

}
