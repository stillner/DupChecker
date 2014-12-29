package org.moll_illner.dupChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.moll_illner.dupChecker.ui.MainWindow;

public class DupChecker {
    private static Logger log = Logger.getLogger(DupChecker.class);

    public static void main(String[] args) {
        try {
            List<StartingPoint> startingPoints = new ArrayList<StartingPoint>();
            // StartingPoint sp = new StartingPoint("d:\\p\\DupChecker");
            StartingPoint sp = new StartingPoint("/home/stillner/dupdir");
            startingPoints.add(sp);
            FilterCrit filterCrit = new FilterCrit();
            filterCrit.setMinSize(1l);
            Crawler crawler = new Crawler(startingPoints, filterCrit);
            Map<Long, List<FileRef>> duplicateFilesBySize = crawler.crawl();
            Map<Long, Map<String, List<FileRef>>> duplicateFilesBySizeAndCRC = crawler.groupByCRC(duplicateFilesBySize);

            //        if (duplicateFilesBySize != null) {
            //            for (Map.Entry<Long, List<FileRef>> entry: duplicateFilesBySize.entrySet()) {
            //                System.out.println();
            //                System.out.println(entry.getKey());
            //                List<FileRef> files = entry.getValue();
            //                if (files != null) {
            //                    for (FileRef f: files) {
            //                        System.out.println(" " + f.getPath());
            //                    }
            //                }
            //            }
            //        }

            MainWindow w = new MainWindow();
            w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //w.displayFiles(duplicateFilesBySize);
            w.displayFilesBySizeAndCRC(duplicateFilesBySizeAndCRC);
            w.setVisible(true);
        }
        catch (Exception ex) {
            log.error("Fehler", ex);
        }
    }

}
