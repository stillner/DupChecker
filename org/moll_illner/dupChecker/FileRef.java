package org.moll_illner.dupChecker;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

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
    
    public void calculateMD5() throws IOException, FileNotFoundException {
    	InputStream inputStream = new BufferedInputStream(new FileInputStream(getPath()));
    	CRC32 crc = new CRC32();
    	int cnt;
    	while ((cnt = inputStream.read()) != -1) {
    		crc.update(cnt);
    	}
    	inputStream.close();
    	_md5 = Long.toHexString(crc.getValue());
    }
}
