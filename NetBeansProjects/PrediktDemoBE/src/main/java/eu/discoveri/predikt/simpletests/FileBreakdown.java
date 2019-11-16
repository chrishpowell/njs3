/*
 */
package eu.discoveri.predikt.simpletests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class FileBreakdown
{
    public static void main(String[] args)
            throws IOException
    {
        System.out.println("java.io.File breakdown:");
        File file = new File("/home/chrispowell/dir1/outfile.txt");
        System.out.println("Absolute path..> " +file.getAbsolutePath());
        System.out.println("Canonical path..> " +file.getCanonicalPath());
        System.out.println("Path..> " +file.getPath());
        System.out.println("Name..> " +file.getName());
        System.out.println("Parent..> " +file.getParent());
        
        System.out.println("\r\njava.nio.Paths/Path breakdown:");
        Path path = Paths.get("/home/chrispowell/dir1/outfile.txt");
        System.out.println("Path string..> " +path.toString());
        System.out.println("URI->URL->String..> " +path.toUri().toURL().toString());
        System.out.println("Filename..> " +path.getFileName());
        System.out.println("Root..> " +path.getRoot());
        System.out.println("Parent..> " +path.getParent());
        System.out.println("Name0..> " +path.getName(0));
        System.out.println("Name1..> " +path.getName(1));
        System.out.println("  etc...");
        System.out.println("File system..> " +path.getFileSystem());
    }
}
