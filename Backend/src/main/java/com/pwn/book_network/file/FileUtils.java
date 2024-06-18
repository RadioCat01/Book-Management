package com.pwn.book_network.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static byte[] readFilesFromLocation(String fileUrl) {

        if(StringUtils.isBlank(fileUrl)){
            return null; //check if the file url is correct
        }
        try{
            Path filePath = new File(fileUrl).toPath(); // create a path object with file

            return Files.readAllBytes(filePath); //return file path
        }catch (IOException e){
            log.warn("File not found {}", fileUrl);
        }
        return null;
    }
}
