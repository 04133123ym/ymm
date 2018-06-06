import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.apache.http.util.Args.notBlank;

/**
 * Created by mengmeng.yan on 2018/4/8.
 */
public class Copy {

    public static void main(String[] args) {
//        File root = new File(File.separatorChar + args[0]);
//        File dest = new File(File.separatorChar + args[1]);
        File root = new File(File.separatorChar + "C:/dev/copy/one");
        File dest = new File(File.separatorChar + "C:/dev/copy/two");

//        System.out.println(args[0] + "-------" + args[1]);

//        try {
//            Collection<File> files = FileUtils.listFiles(root, new String[]{
//                    "properties", "xml", "ico", "png", "vm"
//            }, true);
//
//            for (File file : files) {
//                copy(file, dest, root.getPath().length());
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {

            List<FilePath> filePaths = new CopyFilePath("file-path-src.properties").getMappingPool();
            for (FilePath filePath : filePaths) {
                File file = new File(root + filePath.path);
                copy(file, dest, root.getPath().length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copy(File source, File dest, int length)
            throws IOException {

        File end = new File(dest + source.getPath().substring(length));
        FileUtils.copyFile(source, end);

        System.out.println(end.getAbsolutePath() + " is copied!");
    }


    static class CopyFilePath {
        private static Logger logger = LoggerFactory.getLogger(CopyFilePath.class);
        private final List<FilePath> mappingPool;

        public CopyFilePath(String fileName) {
            Properties properties = new Properties();
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);

            //load file-path mapping
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                logger.error("Cannot load file-path mapping file from {}", fileName, e);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }

            List<FilePath> list = Lists.newArrayList();
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String file = (String) entry.getKey();
                String path = (String) entry.getValue();
                FilePath filePath = new FilePath(file, path);
                list.add(filePath);
            }

            mappingPool = ImmutableList.copyOf(list);
        }

        public List<FilePath> getMappingPool() {
            return mappingPool;
        }
    }

    private static class FilePath {
        private String path;
        private String file;

        public FilePath(String file, String path) {
            notBlank(path, "Path is not blank!");
            notBlank(file, "File is not blank!");

            this.path = path;
            this.file = file;
        }

    }
}
