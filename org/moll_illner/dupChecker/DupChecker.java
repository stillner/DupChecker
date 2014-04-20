package org.moll_illner.dupChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DupChecker {

    public static void main(String[] args) {
        List<StartingPoint> startingPoints = new ArrayList<StartingPoint>();
        startingPoints.add(new StartingPoint("/home/stillner/dupdir"));
        Crawler crawler = new Crawler(startingPoints);
        Map<Long, List<FileRef>> duplicates = crawler.crawl();
        if (duplicates != null) {
            for (Map.Entry<Long, List<FileRef>> entry: duplicates.entrySet()) {
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
    }

}
