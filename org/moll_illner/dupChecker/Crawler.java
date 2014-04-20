package org.moll_illner.dupChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Crawler {
    private List<StartingPoint> _startingPoints;
    
    public Crawler(List<StartingPoint> startingPoints) {
        _startingPoints = startingPoints;
    }
    
    public Map<Long, List<FileRef>> crawl() {
        Map<Long, List<FileRef>> duplicates = new HashMap<Long, List<FileRef>>();
        if (_startingPoints != null) {
            for (StartingPoint sp: _startingPoints) {
                crawl(sp, duplicates);
            }
        }
        removeNonDuplicateEntries(duplicates);
        return duplicates;
    }
    
    /*
     * TODO Recursively traverse directory
     */
    private void crawl(StartingPoint startingPoint, Map<Long, List<FileRef>> filesBySizeMap) {
        if ((startingPoint != null) && (filesBySizeMap != null)) {
            File f = new File(startingPoint.getPath());
            if (f.isDirectory()) {
                File[] files = f.listFiles();
                for (File s: files) {
                    if (!s.isDirectory()) {
                        FileRef fr = new FileRef(s);
                        if (!filesBySizeMap.containsKey(s.length()))
                        {
                            List<FileRef> newSizeList = new ArrayList<FileRef>();
                            filesBySizeMap.put(s.length(), newSizeList);
                        }
                        filesBySizeMap.get(s.length()).add(fr);
                    }
                }
            }
        }
    }
    
    private void removeNonDuplicateEntries(Map<Long, List<FileRef>> filesBySizeMap) {
        if (filesBySizeMap != null) {
            Iterator<Map.Entry<Long, List<FileRef>>> iter = filesBySizeMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Long, List<FileRef>> entry = iter.next();
                List<FileRef> fileList = entry.getValue();
                if ((fileList == null) || (fileList.size() < 2))
                    iter.remove();
                }
            }
    }
}
