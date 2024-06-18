package com.pwn.book_network.file;

import jakarta.persistence.GeneratedValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j //logger log.-->
public class FileStorageService {

    @Value("@{application.file.upload.photos-output-path}")
    private String fileUploadPath;




    public String saveFile(
           @NotNull MultipartFile sourceFile,
           @NotNull Integer userId
    )
    {
       final String fileUploadSubPath = "users" + File.separator + userId; // create the sub path inside the file upload path
       /*
        "users" is a folder name where user files will be stored
        File.separator is a platform independent file separator such as " / " or " \ "
        userId is the id of the user --> unique directory of each user within the user folder
        */
        return uploadFile(sourceFile, fileUploadSubPath);
    }


    private String uploadFile(
           @NotNull MultipartFile sourceFile,
           @NotNull String fileUploadSubPath)
    {

        // complete file path where to upload the file
      final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;

      File targetFolder = new File(finalUploadPath); // find the target file to store files

      if(!targetFolder.exists()){ // if there's no such folder, create a file in the folder
          boolean folderCreated = targetFolder.mkdirs();
          if(!folderCreated){
              log.warn("Failed to create target folder");
          }
      }


      /*
      get file name extension from source file
      getOriginalFilename() is a defined method to get the full name of the source file
      then it's passed in to custom method to get the file name Extension part as a string
      */
      final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());


      /*
      here a new target filePath is created with unique name, adding current milliseconds as the file name
      a path something like,
                   ./uploads/users/1/4337492798423/.jpg
       */
      String targetFilePath = finalUploadPath + File.separator + System.currentTimeMillis() + "."+fileExtension;

      Path targetPath = Paths.get(targetFilePath); // targetPath object type of Path

        try {
            Files.write(targetPath, sourceFile.getBytes()); //write the file path

            log.info("File saved to" + targetFilePath);
            return targetFilePath;
        }catch(IOException e){
            log.error("File was not saved", e);
        }
        return null;
    }


    private String getFileExtension(String fileName) {
        if(fileName==null || fileName.isEmpty()){ // check file name is empty
            return "" ;
        }

        int lastDotIndex = fileName.lastIndexOf("."); //get the index of " . " in the file name

        if(lastDotIndex==-1){
            return "";
        }
        return fileName.substring(lastDotIndex+1).toLowerCase(); // return the file extension name
    }
}
