package org.moll_illner.dupChecker;

import java.io.File;

public class FileRef {

    private File _file;
    private String _md5;
    
    public FileRef(File file) {
        assert(file != null);
        _file = file;
    }
    
    public String getPath() {
        return _file.getPath();
    }
    
    public long length() {
        return _file.length();
    }
    
    public String getMD5()
    {
        return _md5;
    }
    
    private void calculateMD5() {
        
    }
}
