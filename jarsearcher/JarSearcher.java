/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jarsearcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class JarSearcher {

    private static List<String> jars = new ArrayList<String>();
    private static String SEARCHFILE[] = {"package.subpackage.classname"};
//	private static String BASEFOLDER[] = {"C:/location1","C:/location2","C:/location3"};
    private static String BASEFOLDER[] = {"C:/location"};

    public static void main(String[] args) {
//		File file = new File(BASEFOLDER);
        System.out.println("---- Fetching children and filling jars list -----");
        for (String BASEFOLDER1 : BASEFOLDER) {
            getChildren(new File(BASEFOLDER1));
        }

        System.out.println("---- Searching jars for the wanted file -----");
        searchJars();
        System.out.println("---- Done ----");

    }

    private static void searchJars() {
        jars.forEach((jar) -> {
            //System.out.println("---- Searching in " + jar);
            try {
                java.util.jar.JarFile jf = new java.util.jar.JarFile(jar);
                Enumeration resources = jf.entries();
                while (resources.hasMoreElements()) {
                    java.util.jar.JarEntry je = (java.util.jar.JarEntry) resources.nextElement();
                    if (containsFilenames(je.getName(), SEARCHFILE)) {
                        StringBuilder output = new StringBuilder();
                        for (int i = 0; i < SEARCHFILE.length - 1; i++) {
                            output.append(SEARCHFILE[i]).append(" and ");
                        }
                        output.append(SEARCHFILE[SEARCHFILE.length - 1]);
                        output.append(" found in ").append(jar).append(": " ).append( je.getName());
                        System.out.println(output.toString());
                    }
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        });

    }

    private static boolean containsFilenames(String name, String[] searchFile) {
        boolean contain = false;
        for (String searchFile1 : searchFile) {
            if (name.contains(searchFile1.replace(".", "/"))) {
                contain = true;
            } else {
                return false;
            }
        }
        return contain;
    }

    private static void getChildren(File file) {

        for (File child : file.listFiles()) {
            if (child.isDirectory()) {
                getChildren(child);
            } else {
                if (getExtension(child.getName()).equals("jar")) {
//                  System.out.println("---- Adding jar " + child.getAbsolutePath());
                    jars.add(child.getAbsolutePath());
                }
            }
        }

    }

    private static String getExtension(String name) {
        int i = name.lastIndexOf('.');
        String ext = "";
        if (i > 0 && i < name.length() - 1) {
            ext = name.substring(i + 1).toLowerCase();
        }
//		System.out.println(ext);
        return ext;
    }
}
