import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static final String ROOT_DIR = "/home/aleksandr/Game/savegame/";

    public static void main(String[] args) {

        List<GameProgress> saves = new ArrayList();

        saves.add(new GameProgress(40, 150, 1, 2.5));
        saves.add(new GameProgress(80, 1500, 2, 10.5));
        saves.add(new GameProgress(90, 100, 4, 145.4));

        for (int i = 0; i < saves.size(); i++) {
            String path = ROOT_DIR + "save" + (i + 1) + ".dat";
            saveGame(path, saves.get(i));
        }

        Set<String> fileNames = Stream.of(new File(ROOT_DIR).listFiles()).
                filter(file -> !file.isDirectory()).
                map(File::getAbsolutePath).collect(Collectors.toSet());

        System.out.println(fileNames);

        String zipPath = "/home/aleksandr/Game/savegame/save.zip";
        zipFiles(zipPath, fileNames);

        for (String fileName : fileNames) {
            File file = new File(fileName);
            if (file.exists()) file.delete();
        }

    }

    public static void saveGame(String path, GameProgress progress) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(progress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String zipPath, Set<String> fileNames) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (String fileName : fileNames) {
                try (FileInputStream fis = new FileInputStream(fileName)) {
                    ZipEntry entry = new ZipEntry(new File(fileName).getName());
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}