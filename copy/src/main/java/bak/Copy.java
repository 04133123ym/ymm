package bak;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by mengmeng.yan on 2018/4/8.
 */
public class Copy {

    public static void main(String[] args) {

        File root = new File(File.separatorChar + args[0]);
        File dest = new File(File.separatorChar + args[1]);

        System.out.println(args[0] + "-------" + args[1]);

        try {
            Collection<File> files = FileUtils.listFiles(root, new String[]{
                    "properties", "xml", "ico", "png", "vm"
            }, true);

            for (File file : files) {
                copy(file, dest, root.getPath().length());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copy(File source, File dest, int length)
            throws IOException {

        File end = new File(dest + source.getPath().substring(length));
        FileUtils.copyFile(source, end);

        System.out.println(end.getAbsolutePath() + " is copied!");
    }
}
