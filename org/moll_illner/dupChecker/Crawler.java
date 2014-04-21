package org.moll_illner.dupChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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
                Queue<File> directories = new LinkedList<File>();
                directories.add(f);
                File currentDir;
                while ((currentDir = directories.poll()) != null) {
                    File[] files = currentDir.listFiles();
                    for (File s: files) {
                        if (s.isDirectory()) {
                            directories.add(s);
                        }
                        else {
                            recordFile(s, filesBySizeMap);
                        }
                    }
                }
            }
        }
    }
    
    
    private void recordFile(File file, Map<Long, List<FileRef>> filesBySizeMap) {
        FileRef fr = new FileRef(file);
        if (!filesBySizeMap.containsKey(file.length()))
        {
            List<FileRef> newSizeList = new ArrayList<FileRef>();
            filesBySizeMap.put(file.length(), newSizeList);
        }
        filesBySizeMap.get(file.length()).add(fr);
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
