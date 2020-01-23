package roksard.util;

import java.io.File;

public class FileName {
    private String fullFileName;
    private String name;
    private String ext;
    public FileName(String fullFileName) {
        this.fullFileName = fullFileName;
        update();
    }

    private void update() {
        int extPos = fullFileName.lastIndexOf(".");
        if (extPos != -1) {
            name = fullFileName.substring(0, extPos);
            ext = fullFileName.substring(extPos);
        } else {
            name = fullFileName;
            ext = "";
        }
    }

    public static String getNextNotExistingFileName(String fileName) {
        File f;
        int suffix = 0;
        FileName fn = new FileName(fileName);
        String originalName = fn.getName();
        do {
            f = new File(fn.getFullFileName());
            System.out.println(fn.getFullFileName());
            fn.setName(originalName + "_" + suffix);
            suffix++;
        } while (f.exists());
        return f.getAbsolutePath();
    }

    public String getFullFileName() {
        return name + ext;
    }

    public void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
        update();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public static void test(String fullFileName) {
        FileName fn = new FileName(fullFileName);
        System.out.println("fullFileName: "+ fn.getFullFileName());
        System.out.println("name: "+ fn.getName());
        System.out.println("ext: "+ fn.getExt());
    }
}
