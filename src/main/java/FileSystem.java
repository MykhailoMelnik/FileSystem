import java.util.ArrayList;
import java.util.List;

enum Category {
    MUSIC, VIDEO, DOCUMENTS, ZIP, ROOT
}

class File {
    private String name;
    private long id;
    private Category category;

    public File(String name, long id, Category category) {
        this.name = name;
        this.id = id;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }
}

class Directory {
    private String name;
    private long id;
    private Category category;
     List<File> files;
     List<Directory> directories;

    public Directory(String name, long id, Category category) {
        this.name = name;
        this.id = id;
        this.category = category;
        this.files = new ArrayList<>();
        this.directories = new ArrayList<>();
    }

    public void addFile(File file) {
        files.add(file);
    }

    public void addDirectory(Directory directory) {
        directories.add(directory);
    }

    public void removeFile(long fileId) {
        files.removeIf(file -> file.getId() == fileId);
    }

    public void removeDirectory(long directoryId) {
        directories.removeIf(directory -> directory.getId() == directoryId);
    }

    public long getId() {
        return id;
    }

    public void print(int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            System.out.print("\t");
        }
        System.out.println(name);

        for (File file : files) {
            for (int i = 0; i < indentLevel + 1; i++) {
                System.out.print("\t");
            }
            System.out.println(file.getName());
        }

        for (Directory directory : directories) {
            directory.print(indentLevel + 1);
        }
    }
}

public class FileSystem {
    private Directory root;

    public FileSystem() {
        this.root = new Directory("root", 0, Category.ROOT);
    }

    public Directory getDirectoryById(long directoryId) {
        return getDirectoryById(root, directoryId);
    }

    private Directory getDirectoryById(Directory directory, long directoryId) {
        if (directory.getId() == directoryId) {
            return directory;
        }

        for (Directory subDirectory : directory.directories) {
            Directory foundDirectory = getDirectoryById(subDirectory, directoryId);
            if (foundDirectory != null) {
                return foundDirectory;
            }
        }

        return null;
    }

    public void addDirectory(String name, long directoryId, Category category, long parentDirectoryId) {
        Directory parentDirectory = getDirectoryById(parentDirectoryId);
        Directory newDirectory = new Directory(name, directoryId, category);
        parentDirectory.addDirectory(newDirectory);
    }

    public void addFile(String name, long id, Category category, long parentDirectoryId) {
        Directory parentDirectory = getDirectoryById(parentDirectoryId);
        File newFile = new File(name, id, category);
        parentDirectory.addFile(newFile);
    }

    public void removeDirectory(long directoryId) {
        Directory directoryToRemove = getDirectoryById(directoryId);
        directoryToRemove.removeDirectory(directoryId);
    }

    public void removeFile(long fileId) {
        Directory directoryContainingFile = findDirectoryContainingFile(root, fileId);
        if (directoryContainingFile != null) {
            directoryContainingFile.removeFile(fileId);
        }
    }

    private Directory findDirectoryContainingFile(Directory directory, long fileId) {
        for (File file : directory.files) {
            if (file.getId() == fileId) {
                return directory;
            }
        }

        for (Directory subDirectory : directory.directories) {
            Directory foundDirectory = findDirectoryContainingFile(subDirectory, fileId);
            if (foundDirectory != null) {
                return foundDirectory;
            }
        }

        return null;
    }

    public void showAll() {
        root.print(0);
    }

    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();

        fileSystem.addDirectory("Directory1", 1, Category.DOCUMENTS, 0);
        fileSystem.addFile("FileX", 2, Category.DOCUMENTS, 1);
        fileSystem.addDirectory("Directory11", 3, Category.DOCUMENTS, 1);
        fileSystem.addFile("File1", 4, Category.DOCUMENTS, 3);
        fileSystem.addFile("File2", 5, Category.DOCUMENTS, 3);
        fileSystem.addDirectory("Directory12", 6, Category.DOCUMENTS, 1);
        fileSystem.addDirectory("Directory121", 7, Category.DOCUMENTS, 6);
        fileSystem.addFile("File3", 8, Category.DOCUMENTS, 7);
        fileSystem.addFile("File4", 9, Category.DOCUMENTS, 7);
        fileSystem.addFile("File122", 10, Category.DOCUMENTS, 6);
        fileSystem.addDirectory("Directory2", 11, Category.DOCUMENTS, 0);
        fileSystem.addFile("File5", 12, Category.DOCUMENTS, 11);
        fileSystem.addFile("File6", 13, Category.DOCUMENTS, 11);

        fileSystem.showAll();
    }
}
