/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import sfs.catalog.EntidadEstado;
import sfs.persistence.objects.Faq;

/**
 *
 * @author WL72166
 */
public class Utilities {
    
    public static final int LOG_LEVEL_ERROR = 1;
    public static final int LOG_LEVEL_WARNING = 2;
    public static final int LOG_LEVEL_INFO = 3;
    public static final int LOG_LEVEL_DEBUG = 4;
    
    public static int logLevel = 1;
    
    private static PrintWriter logPrinter = null;
    
    public static List<Class> getClasses(ClassLoader cl,String pack) throws IOException{
        
        String dottedPackage = pack.replaceAll("[/]", ".");
        List<Class> classes = new ArrayList<Class>();
        URL upackage = cl.getResource(pack);
        
        DataInputStream dis = new DataInputStream((InputStream) upackage.getContent());
        String line = null;
        while ((line = dis.readLine()) != null) {
            if(line.endsWith(".class")){
                try{
                    classes.add(Class.forName(dottedPackage+"."+line.substring(0, line.lastIndexOf('.'))));
                }catch(Exception e){}
            }
        }
        return classes;
    }
    
    public static boolean isInstance(Class child, Class parent){
        return extendsOf(child, parent);
    }
    
    
    private static boolean extendsOf(Class child, Class parent){
        Class current = child;
        while(current!=null){
            if(current.getClass().equals(parent)){
                return true;
            }
            
            if(implementsOf(current, parent)){
                return true;
            }
            
            current = current.getSuperclass();
            
        }
        return false;
    }
    
    private static boolean implementsOf(Class child, Class iparent){
        Class current = child;
        
        if(child.equals(iparent)){
            return true;
        }
        
        if(current.getInterfaces()!=null){
            for(Class c: current.getInterfaces()){
                if(implementsOf(c, iparent)){
                    return true;
                }
            }
        }
        
        return false;
        
    }

    

    static public File extractFolder(String zipFile) throws ZipException, IOException {
        int BUFFER = 2048;
        File file = new File(zipFile);

        ZipFile zip = new ZipFile(file);
        String newPath = zipFile.substring(0, zipFile.length() - 4)+"_tmp"+((int)(Math.random()*1000   ));
        File newFile = new File(newPath);
        newFile.mkdir();
        Enumeration zipFileEntries = zip.entries();

        while (zipFileEntries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(newPath, currentEntry);
            File destinationParent = destFile.getParentFile();

            destinationParent.mkdirs();

            if (!entry.isDirectory()) {
                BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
                int currentByte;
                byte data[] = new byte[BUFFER];

                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream dest = new BufferedOutputStream(fos,
                        BUFFER);
                while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();
            }

            if (currentEntry.endsWith(".zip")) {
                extractFolder(destFile.getAbsolutePath());
            }
        }

        return newFile;
    }

    static public void deleteDirectory(File path) {
        if (path!=null && path.exists()) {
            if (path.isDirectory()) {
                for (File f : path.listFiles()) {
                    deleteDirectory(f);
                }
            }
            path.delete();
        }
    }

    public static void moveDirectory(File file, File folderDestino){
        if (file!=null && file.exists()) {
            File newname = new File(folderDestino.getAbsolutePath()+File.separator+file.getName());
//            System.out.println("renaming: "+newname);
//            file.renameTo(newname);
//            if (file.isDirectory()) {
//                for (File f : file.listFiles()) {
//                    moveDirectory(f,newname);
//                }
//            }
            file.renameTo(newname);
        }
    }

    public static void copyDirectory(File file, File folderDestino) throws IOException{
        if (file!=null && file.exists()) {
            File newFile = new File(folderDestino.getAbsolutePath()+File.separator+file.getName());
            if (file.isDirectory()) {
                newFile.mkdir();
                for (File f : file.listFiles()) {
                    copyDirectory(f,newFile);
                }
            }else{
                copyFile(file, newFile);
            }
        }
    }

    public static void copyFile(File orig, File dest) throws IOException {
        FileInputStream fis = new FileInputStream(orig);
        FileOutputStream fos = new FileOutputStream(dest);
        copyData(fis, fos);
        
    }
    
    public static void copyData(InputStream fis, OutputStream fos) throws IOException{
        try {
            byte[] buff = new byte[50000];
            int readed = -1;
            while ((readed = fis.read(buff)) > 0) {
                fos.write(buff, 0, readed);
            }
        } finally {
            fis.close();
            fos.close();
        }
    }

    public static String colorToHex(Color color){
        String hex = Integer.toHexString(color.getRGB() & 0xffffff);
        if (hex.length() < 6) {
            hex = "0" + hex;
        }
        hex = "#" + hex;
        return hex;
    }

    public static boolean equalsOrNull(Object val1,Object val2){
        if(val1 == null && val2 == null){
            return true;
        }

        if(val1==null || val2 == null){
            return false;
        }
        return val1.equals(val2);
    }

    public static List<TextPosition> searchInText(String source,String searchText,boolean matchCase,boolean completeWord, boolean ignorarTildes){

        int foundInSearch=0;
        int MAX_SEARCH_RESULTS=1000;
        int lineas=0;
        int buff=0;
        boolean agregar = false;
        List<TextPosition> encontrados = new ArrayList<TextPosition>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < source.length(); i++) {
            agregar =false;
            char c = source.charAt(i);

            if(c == '\n'){
                lineas++;
                sb.delete(0, sb.length());
            }else{
                sb.append(c);
            }

            if (comparaCaracter(c,searchText.charAt(buff),matchCase,ignorarTildes)) {
                buff++;
            }else{
                buff=0;
            }

            if (buff == searchText.length()) {
                if (completeWord) {
                    int nextIndex = i + 1;
                    if (nextIndex < source.length()) {
                        char next = source.charAt(nextIndex);
                        if (isScapeCaracter(next)) {
                            agregar =true;
                        }
                    } else {
                        agregar = true;
                    }
                } else {
                    agregar = true;
                }
                buff = 0;
            }

            if (agregar) {

                if(foundInSearch > MAX_SEARCH_RESULTS){
                    break;
                }

                for(int x=1;x<=10;x++){
                    if((i+x)>=(source.length())){
                        break;
                    }
                    sb.append(source.charAt(i+x));
                }

                foundInSearch++;
                TextPosition tp = new TextPosition();
                tp.setLinea(lineas+1);
                tp.setEnd(i+1);
                tp.setStart(i - searchText.length()+1);
                tp.setTextContext(sb.toString());
                encontrados.add(tp);
            }

        }

        return encontrados;
    }

    private static boolean isScapeCaracter(char c){
        return !esCaracterAlfaNumerico(c);
    }

    private static boolean esCaracterAlfaNumerico(char c){
        switch(c){
            case 'A': case 'a': case 'Á': case 'á': case 'B': case 'b': case 'C': case 'c':
            case 'D': case 'd': case 'E': case 'e': case 'É': case 'é': case 'F': case 'f':
            case 'G': case 'g': case 'H': case 'h': case 'I': case 'i': case 'Í': case 'í':
            case 'J': case 'j': case 'K': case 'k': case 'L': case 'l': case 'M': case 'm':
            case 'N': case 'n': case 'O': case 'o': case 'Ó': case 'ó': case 'P': case 'p':
            case 'Q': case 'q': case 'R': case 'r': case 'S': case 's': case 'T': case 't':
            case 'U': case 'u': case 'Ú': case 'ú': case 'V': case 'v': case 'W': case 'w':
            case 'X': case 'x': case 'Y': case 'y': case 'Z': case 'z': case 'Ñ': case 'ñ':
            case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8':
            case '9': case '0':
                return true;
            default: return false;
        }
    }

    private static boolean comparaCaracter(char c1, char c2, boolean matchCase, boolean ignorarTildes){
        if(ignorarTildes){
            if(matchCase){
                switch(c1){
                    case 'A': case 'Á': return (c2 == 'A' || c2 == 'Á');
                    case 'E': case 'É': return (c2 == 'E' || c2 == 'É');
                    case 'I': case 'Í': return (c2 == 'I' || c2 == 'Í');
                    case 'O': case 'Ó': return (c2 == 'O' || c2 == 'Ó');
                    case 'U': case 'Ú': return (c2 == 'U' || c2 == 'Ú');
                    case 'a': case 'á': return (c2 == 'a' || c2 == 'á');
                    case 'e': case 'é': return (c2 == 'e' || c2 == 'é');
                    case 'i': case 'í': return (c2 == 'i' || c2 == 'í');
                    case 'o': case 'ó': return (c2 == 'o' || c2 == 'ó');
                    case 'u': case 'ú': return (c2 == 'u' || c2 == 'ú');
                    default: return c1 == c2;
                }
            }else{
                switch(c1){
                    case 'A': case 'a': case 'Á': case 'á': return (c2 == 'A' || c2 == 'a' || c2 == 'Á' || c2 == 'á');
                    case 'B': case 'b': return (c2 == 'B' || c2 == 'b');
                    case 'C': case 'c': return (c2 == 'C' || c2 == 'c');
                    case 'D': case 'd': return (c2 == 'D' || c2 == 'd');
                    case 'E': case 'e': case 'É': case 'é': return (c2 == 'E' || c2 == 'e' || c2 == 'É' || c2 == 'é');
                    case 'F': case 'f': return (c2 == 'F' || c2 == 'f');
                    case 'G': case 'g': return (c2 == 'G' || c2 == 'g');
                    case 'H': case 'h': return (c2 == 'H' || c2 == 'h');
                    case 'I': case 'i': case 'Í': case 'í': return (c2 == 'I' || c2 == 'i' || c2 == 'Í' || c2 == 'í');
                    case 'J': case 'j': return (c2 == 'J' || c2 == 'j');
                    case 'K': case 'k': return (c2 == 'K' || c2 == 'k');
                    case 'L': case 'l': return (c2 == 'L' || c2 == 'l');
                    case 'M': case 'm': return (c2 == 'M' || c2 == 'm');
                    case 'N': case 'n': return (c2 == 'N' || c2 == 'n');
                    case 'O': case 'o': case 'Ó': case 'ó': return (c2 == 'O' || c2 == 'o' || c2 == 'Ó' || c2 == 'ó');
                    case 'P': case 'p': return (c2 == 'P' || c2 == 'p');
                    case 'Q': case 'q': return (c2 == 'Q' || c2 == 'q');
                    case 'R': case 'r': return (c2 == 'R' || c2 == 'r');
                    case 'S': case 's': return (c2 == 'S' || c2 == 's');
                    case 'T': case 't': return (c2 == 'T' || c2 == 't');
                    case 'U': case 'u': case 'Ú': case 'ú': return (c2 == 'U' || c2 == 'u' || c2 == 'Ú' || c2 == 'ú');
                    case 'V': case 'v': return (c2 == 'V' || c2 == 'v');
                    case 'W': case 'w': return (c2 == 'W' || c2 == 'w');
                    case 'X': case 'x': return (c2 == 'X' || c2 == 'x');
                    case 'Y': case 'y': return (c2 == 'Y' || c2 == 'y');
                    case 'Z': case 'z': return (c2 == 'Z' || c2 == 'z');
                    case 'Ñ': case 'ñ': return (c2 == 'Ñ' || c2 == 'ñ' || c2 == 'N' || c2 == 'n' );
                    default: return c1 == c2;
            }
        }
        }else{
            if(matchCase){
                return c1 == c2;
            }else{
                switch(c1){
                    case 'A': case 'a': return (c2 == 'A' || c2 == 'a');
                    case 'B': case 'b': return (c2 == 'B' || c2 == 'b');
                    case 'C': case 'c': return (c2 == 'C' || c2 == 'c');
                    case 'D': case 'd': return (c2 == 'D' || c2 == 'd');
                    case 'E': case 'e': return (c2 == 'E' || c2 == 'e');
                    case 'F': case 'f': return (c2 == 'F' || c2 == 'f');
                    case 'G': case 'g': return (c2 == 'G' || c2 == 'g');
                    case 'H': case 'h': return (c2 == 'H' || c2 == 'h');
                    case 'I': case 'i': return (c2 == 'I' || c2 == 'i');
                    case 'J': case 'j': return (c2 == 'J' || c2 == 'j');
                    case 'K': case 'k': return (c2 == 'K' || c2 == 'k');
                    case 'L': case 'l': return (c2 == 'L' || c2 == 'l');
                    case 'M': case 'm': return (c2 == 'M' || c2 == 'm');
                    case 'N': case 'n': return (c2 == 'N' || c2 == 'n');
                    case 'O': case 'o': return (c2 == 'O' || c2 == 'o');
                    case 'P': case 'p': return (c2 == 'P' || c2 == 'p');
                    case 'Q': case 'q': return (c2 == 'Q' || c2 == 'q');
                    case 'R': case 'r': return (c2 == 'R' || c2 == 'r');
                    case 'S': case 's': return (c2 == 'S' || c2 == 's');
                    case 'T': case 't': return (c2 == 'T' || c2 == 't');
                    case 'U': case 'u': return (c2 == 'U' || c2 == 'u');
                    case 'V': case 'v': return (c2 == 'V' || c2 == 'v');
                    case 'W': case 'w': return (c2 == 'W' || c2 == 'w');
                    case 'X': case 'x': return (c2 == 'X' || c2 == 'x');
                    case 'Y': case 'y': return (c2 == 'Y' || c2 == 'y');
                    case 'Z': case 'z': return (c2 == 'Z' || c2 == 'z');
                    case 'Ñ': case 'ñ': return (c2 == 'Ñ' || c2 == 'ñ');
                    default: return c1 == c2;
                }
            }
        }
    }

    public static String truncateString(String str, int size){
        if(size <=0 || size > str.length()){
            return str;
        }

        return str.substring(0, size);

    }

    public static void printToLog(String str,Exception ex, int logLevel){
        if(logLevel >= logLevel){


            Date d = new Date();
            String time = (d.getYear()+1900)+"-"+(d.getMonth()+1)+"-"+d.getDate()+" "+d.getHours()+"."+d.getMinutes()+"."+d.getSeconds();
            try{

                String leyenda = "";

                switch (logLevel) {
                    case Utilities.LOG_LEVEL_DEBUG:
                        leyenda = "[DEBUG]";
                        break;
                    case Utilities.LOG_LEVEL_INFO:
                        leyenda = "[INFO]";
                        break;
                    case Utilities.LOG_LEVEL_WARNING:
                        leyenda = "[WARNING]";
                        break;
                    case Utilities.LOG_LEVEL_ERROR:
                        leyenda = "[ERROR]";
                        break;
                }

                File file = new File("app.log");
                if(file.length()>26214400){
                    logPrinter.close();
                    logPrinter = null;
                    if(!file.renameTo(new File(file.getAbsolutePath()+"."+time))){
                        throw new Exception("Can't rename file");
                    }
                }

                if (logPrinter == null) {
                    logPrinter = new PrintWriter(new File("app.log"));
                }

                str = str.replaceAll("\\r", "");
                str = str.replaceAll("\\n", "\n\t");

                logPrinter.append(time);
                logPrinter.append(" ");
                logPrinter.append(leyenda);
                logPrinter.append("[Thread:");
                logPrinter.append(Long.toString(Thread.currentThread().getId()));
                logPrinter.append("] ");
                logPrinter.append(str);
                logPrinter.append("\r\n");

                if(ex!=null){
                    ex.printStackTrace(logPrinter);
                }

                logPrinter.append("\r\n");

                //just for now
                System.out.println(str);
                if(ex!=null) ex.printStackTrace();


            }catch(Exception e){
                System.out.println("Error al escribir en el archivo de log");
                //e.printStackTrace();
            }finally{
                logPrinter.flush();
            }
        }
    }

    public static Method getMethod(Class clase,String name, Class... args){

        Method m = null;

        try {
            m = clase.getMethod(name, args);
        } catch (Exception e) {
        }

        if(m!=null){
            return m;
        }

        if(clase.getSuperclass()!=null){
              return getMethod(clase.getSuperclass(), name, args);
        }

        return null;

    }


    public static String fillWithCero(int n, int length){
        String s = Integer.toString(n);
        for(int i=s.length();i<length;i++){
            s = '0'+s;
        }
        return s;
    }

   

    public static String substring(String str,int offset,int length){

        if((offset+length)>str.length()){
            length = str.length()-offset;
        }

        return str.substring(offset, offset+length);
    }

    public static boolean areSimilar(String text1,String text2){
        int distance = Leveshtein.getSimilarity(text1, text2);
        int difference = text1.length()-text2.length();

        if(difference < 0){
            difference = difference * -1;
        }
        distance = distance - difference;


        if(difference>2) return false;

        return distance <= 2;

    }


    public static boolean isInteger(String txt){
        try {
            Integer.parseInt(txt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static byte[] readFile(File file) throws IOException{
        FileInputStream fis = new FileInputStream(file);
        byte[] buff = new byte[(int)file.length()];
        fis.read(buff);
        fis.close();
        return buff;
    }
    
    public static String get(String keyText){
        String str = TextResourcesUtil.getText(keyText);
        if(str==null){
            str = keyText;
        }
        return str;
    }
    
    
    public static String strToMd5(String str){
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(str.getBytes("UTF-8"));
           
           StringBuilder sb = new StringBuilder(2*hash.length);
           for(byte b : hash){
               sb.append(String.format("%02x", b&0xff));
           }
           digest = sb.toString();
        } catch (Exception ex) {
            printToLog("Error al generar el MD5", ex, LOG_LEVEL_ERROR);
        }
        return digest;
    }
    
    public static int getAprox(double v){
         int i = (int)v;
         double r = v-i;
         
         if(r==0 || r<0.5){
             return i;
         }
         return i+1;
    }
    
    public static List<Faq> getActivFaqs(List<Faq> faqs){
        if(faqs!=null){
            List<Faq> newfaqs = new ArrayList<Faq>();
            for(Faq f: faqs){
                if(f.getEstado()!=EntidadEstado.ACTIVO) continue;
                newfaqs.add(f);
            }
            return newfaqs;
        }
        return null;
    }
    
    public static void main(String[] args) {
        System.out.print(strToMd5("Ale_061180"));
    }


    
}
