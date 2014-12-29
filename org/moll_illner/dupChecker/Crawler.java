package org.moll_illner.dupChecker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.log4j.Logger;

public class Crawler {
    private static Logger log = Logger.getLogger(Crawler.class);
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
     * Recursively traverse directory
     */
    private void crawl(StartingPoint startingPoint, Map<Long, List<FileRef>> filesBySizeMap) {
        if ((startingPoint != null) && (filesBySizeMap != null)) {
            log.info("Crawling " + startingPoint.getPath());
            File f = new File(startingPoint.getPath());
            if (f.isDirectory()) {
                Queue<File> directories = new LinkedList<File>();
                directories.add(f);
                File currentDir;
                while ((currentDir = directories.poll()) != null) {
                    log.info("Crawling " + currentDir.getAbsolutePath());
                    File[] files = currentDir.listFiles();
                    if (files == null) {
                        log.warn("This is not a directory");
                    }
                    else {
                        log.debug("Directory contains " + files.length + " entries");
                        for (File s: files) {
                            if (s.isDirectory()) {
                                directories.add(s);
                            }
                            else {
                                processFile(s, filesBySizeMap, startingPoint);
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    private void processFile(File file, Map<Long, List<FileRef>> filesBySizeMap, StartingPoint startingPoint) {
        log.debug("Processing " + file.getAbsolutePath());
        FileRef fr = new FileRef(file);
    	if ((startingPoint.getMinSize() != null) && (fr.length() < startingPoint.getMinSize())) {
    	    log.debug("File ignored because it is too small");
    		return;
    	}
    	if ((startingPoint.getMaxSize() != null) && (fr.length() > startingPoint.getMaxSize())) {
            log.debug("File ignored because it is too large");
    		return;
    	}
    	recordFile(fr, filesBySizeMap);
    }
    
    
    private void recordFile(FileRef fileRef, Map<Long, List<FileRef>> filesBySizeMap) {
        if (!filesBySizeMap.containsKey(fileRef.length()))
        {
            List<FileRef> newSizeList = new ArrayList<FileRef>();
            filesBySizeMap.put(fileRef.length(), newSizeList);
        }
        filesBySizeMap.get(fileRef.length()).add(fileRef);
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
