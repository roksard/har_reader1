package roksard;

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HttpStatus;
import roksard.model.ContentType;
import roksard.util.FileName;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class Main {
    public static String harFileLocation = "c:\\Users\\ruf\\Downloads\\nevseoboi.com.ua.har";
    public static String filesOutputDirectory = "c:\\Users\\ruf\\Downloads\\2020-01-23 nature wallpapers2";

    public static boolean writeBase64String(String fileName, String base64content) {
        boolean result = false;
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64content);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        fileName = FileName.getNextNotExistingFileName(fileName);
        File parentDir = new File(fileName).getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                System.err.println("Could not create directories: " + parentDir.getAbsolutePath());
            }
        }
        try (FileOutputStream os = new FileOutputStream(fileName)) {
            os.write(decoded);
            result = true;
        } catch (IOException e) {
            System.err.println("Could not write base64 content: " + fileName);
            e.printStackTrace();
            new File(fileName).delete();
        }
        return result;
    }

    public static void main(String[] args) throws HarReaderException {
        if (args.length >= 2) {
            harFileLocation = args[0];
            filesOutputDirectory = args[1];
        } else {
            System.out.println("Har Reader will get all images from an .har (HTTP archive file).  \n" +
                    "Usage: har_reader har_file_location files_output_directory");
        }

        HarReader harReader = new HarReader();
        Har har = harReader.readFromFile(new File(harFileLocation));

        Set<String> uniqueUrlList = new HashSet<>();

        List<HarEntry> contents = har.getLog().getEntries()
                .stream()
                .filter(content -> {
                    boolean isContentFits = ContentType.IMAGE_JPEG.getType().equals(content.getResponse().getContent().getMimeType())
                            || ContentType.IMAGE_GIF.getType().equals(content.getResponse().getContent().getMimeType())
                            || ContentType.IMAGE_PNG.getType().equals(content.getResponse().getContent().getMimeType())
                            || ContentType.IMAGE_WEBP.getType().equals(content.getResponse().getContent().getMimeType())
                            || ContentType.IMAGE_X_ICON.getType().equals(content.getResponse().getContent().getMimeType());
                    boolean isSizeFits = content.getResponse().getBodySize() > 500000;
                    boolean isNotDublicate = !uniqueUrlList.contains(content.getRequest().getUrl());
                    boolean isStatusOk = true || HttpStatus.OK.getCode() == content.getResponse().getStatus();
                    boolean result = isStatusOk
                            && isContentFits
                            && isSizeFits
                            && isNotDublicate;
                    if (result) {
                        uniqueUrlList.add(content.getRequest().getUrl());
                    }
                    return result;
                }).collect(Collectors.toList());

        System.out.println("Found content entries: "+ contents.size());
        contents.stream().forEach(content -> {
            String base64content = content.getResponse().getContent().getText();
            String fileName =  content.getRequest().getUrl();
            int paramPos = fileName.indexOf("?");
            if (paramPos != -1) {
                fileName = fileName.substring(1, paramPos);
            }
            int fileNameStart = fileName.lastIndexOf("/");
            fileName =  fileName.substring(fileNameStart+1);
            String fullFileName = (filesOutputDirectory + "\\" + fileName).replaceAll("\\\\+", "\\\\"); //replace double backslash with single
            writeBase64String(fullFileName, base64content);
        });
    }
}
