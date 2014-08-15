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
    FilterCrit _filterCrit;
    
    public Crawler(List<StartingPoint> startingPoints, FilterCrit filterCrit) {
        _startingPoints = startingPoints;
        _filterCrit = filterCrit;
    }
    
    public Map<Long, List<FileRef>> crawl() {
        Map<Long, List<FileRef>> duplicates = new HashMap<Long, List<FileRef>>();
        if (_startingPoints != null) {
            for (StartingPoint sp: _startingPoints) {
                crawl(sp, duplicates, _filterCrit);
            }
        }
        removeNonDuplicateEntries(duplicates);
        return duplicates;
    }
    
    /*
     * Recursively traverse directory
     */
    private void crawl(StartingPoint startingPoint, Map<Long, List<FileRef>> filesBySizeMap,
    		FilterCrit filterCrit) {
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
                            processFile(s, filesBySizeMap, startingPoint, filterCrit);
                        }
                    }
                }
            }
        }
    }
    
    
    private void processFile(File file, Map<Long, List<FileRef>> filesBySizeMap, 
    		StartingPoint startingPoint, FilterCrit filterCrit) {
        FileRef fr = new FileRef(file);
    	if ((filterCrit.getMinSize() != null) && (fr.length() < filterCrit.getMinSize())) {
    		return;
    	}
    	if ((filterCrit.getMaxSize() != null) && (fr.length() > filterCrit.getMaxSize())) {
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
                if ((fileList == null) || (fileList.size() < 2)) {
                    iter.remove();
                }
            }
        }
    }
    
    
    public Map<Long, Map<String, List<FileRef>>> groupByCRC(Map<Long, List<FileRef>> filesBySizeMap) {
    	Map<Long, Map<String, List<FileRef>>> retval = new HashMap<Long, Map<String, List<FileRef>>>();
        if (filesBySizeMap != null) {
            Iterator<Map.Entry<Long, List<FileRef>>> iter = filesBySizeMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Long, List<FileRef>> entry = iter.next();
                List<FileRef> fileList = entry.getValue();
                Map<String, List<FileRef>> filesByCRC = new HashMap<String, List<FileRef>>();
                if (fileList != null) {
                	// group by CRC
                	for (FileRef fileRef : fileList) {
						try {
							fileRef.calculateMD5();
							if (!filesByCRC.containsKey(fileRef.getMD5())) {
								filesByCRC.put(fileRef.getMD5(), new ArrayList<FileRef>());
							}
							filesByCRC.get(fileRef.getMD5()).add(fileRef);
						}
						catch (Exception ex) {
							// Log.Error
						}
					}

                	// remove groups with only one entry
                	Iterator<Map.Entry<String, List<FileRef>>> crcGroupIterator = filesByCRC.entrySet().iterator();
                	while (crcGroupIterator.hasNext()) {
                		Map.Entry<String, List<FileRef>> crcGroupEntry = crcGroupIterator.next();
                		if (crcGroupEntry.getValue().size() < 2) {
                			crcGroupIterator.remove();
                		}
                	}
                	
                	if (filesByCRC.size() > 0) {
                		Long fileSize = entry.getKey();
                		retval.put(fileSize, filesByCRC);
                	}

                }
            }
        }
        return retval;
    }
}
