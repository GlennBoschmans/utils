package jarsearcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class JarSearcher {

    private static List<String> jars = new ArrayList<String>();
    
    public String[] search(String[] basefolders, String searchfile) {
        System.out.println("---- Fetching children and filling jars list -----");
        for (String bf : basefolders) {
            getChildren(new File(bf));
        }

        System.out.println("---- Searching jars for the wanted file -----");
        String[] result = searchJars(searchfile);
        System.out.println("---- Done ----");
			
        return result;
    }

    private String[] searchJars(String searchfiles) {
        List<String> result = new ArrayList<>();
        jars.forEach((jar) -> {
            //System.out.println("---- Searching in " + jar);
            try {
                java.util.jar.JarFile jf = new java.util.jar.JarFile(jar);
                Enumeration resources = jf.entries();
                while (resources.hasMoreElements()) {
                    java.util.jar.JarEntry je = (java.util.jar.JarEntry) resources.nextElement();
                    if (containsFilename(je.getName(), searchfiles)) {
                        result.add(jar);
                    }
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });

        //filter out duplicates
		Set<String> set = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        set.addAll(result);
        return (new ArrayList<String>(set)).toArray(new String[0]);
    }

    private boolean containsFilename(String name, String searchFile) {
        return name.contains(searchFile.replace(".", "/"));
    }

    private void getChildren(File file) {

        for (File child : file.listFiles()) {
            if (child.isDirectory()) {
                getChildren(child);
            } else {
                if (getExtension(child.getName()).equals("jar")) {
                    jars.add(child.getAbsolutePath());
                }
            }
        }

    }

    private String getExtension(String name) {
        int i = name.lastIndexOf('.');
        String ext = "";
        if (i > 0 && i < name.length() - 1) {
            ext = name.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
