package org.moll_illner.dupChecker;

public class StartingPoint {
    private String _path;
    private Long _minSize;
    private Long _maxSize;
    
    public StartingPoint(String path) {
        _path = path;
    }

    public String getPath() {
        return _path;
    }
    
    public void setPath(String path) {
        _path = path;
    }
    
    public Long getMinSize() {
    	return _minSize;
    }
    
    public void setMinSize(Long minSize) {
    	_minSize = minSize;
    }
    
    public Long getMaxSize() {
    	return _maxSize;
    }
    
    public void setMaxSize(Long maxSize) {
    	_maxSize = maxSize;
    }
}
