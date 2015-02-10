package sfs;

import static sfs.Utilities.*;
import java.awt.Component;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wl72166
 */
public class TextResourcesUtil {

    
    public static final String IDIOMA_ES = "es";
    public static final String IDIOMA_EN = "en";
    
    private static List<String> loadedResources = new ArrayList<String>();
    private static Properties textProperties;
    private static List<Component> registredComponents= new ArrayList<Component>();
    private static String lang="es";

 
    public static Properties getPropertiesObject(){
        
        if (textProperties == null) {

            textProperties = new Properties() {
                @Override
                public String getProperty(String key) {
                    
                    if(key==null) return null;
                    
                    String s = super.getProperty(lang + "-" + key);
                    if(s==null){
                        s = super.getProperty(key);
                    }
                    return s; 
                }
                
                public Object get(Object obj){
                    if(obj instanceof String){
                        return super.get(lang + "-" + (String)obj);
                    }
                    return super.get(obj);
                }
                
            };
            try {
                loadResource(TextResourcesUtil.class, "/sfs/resources/text.properties");
                loadResource(TextResourcesUtil.class, "/sfs/resources/serviceoptionsnames.properties");
                loadResource(TextResourcesUtil.class, "/sfs/resources/queries.sql");
            } catch (Exception e) {
                printToLog("Error al cargar los archivos de texto", null, LOG_LEVEL_ERROR);
            }
        }        
        
        return textProperties;
    }


    public static String getText(String key){
        return getPropertiesObject().getProperty(key);
    }

    public static void loadResource(Class forLoad,String res) throws Exception{
        loadResources(forLoad.getResourceAsStream(res), res);
    }
    
    private static void loadResources(InputStream is, String resourceName) throws IOException{
        if(!loadedResources.contains(resourceName)){
            if(resourceName.toLowerCase().endsWith(".sql")){
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Utilities.copyData(is, baos);
                    String str = new String(baos.toByteArray());
                    String[] datos = str.split("[;][;][;]");
                    if (datos != null) {
                        for (String d : datos) {
                            String[] v = d.split("[:][:][:]");
                            textProperties.setProperty(v[0].trim(), v[1].trim());
                        }
                    }
                } catch (Exception e) {
                     throw new IOException("Cannot load resource: "+resourceName+" Format Error", e);
                }
            }else{
                getPropertiesObject().load(is);
            }
            loadedResources.add(resourceName);
        }
    }
    
    public static void loadResource(String filePath) throws IOException{
        loadResources(new FileInputStream(filePath), filePath);
    }


    public static void cambiarIdioma(String idioma) throws Exception{
        if(!esIdiomaValido(idioma)){
            throw new Exception("No es un idioma válido");
        }
        lang = idioma;
        for(Component c : registredComponents){
            setTextsInComponent(c);
        }
    }


    public static void registrarComponente(Component cmp) throws IllegalAccessException{
        if(!registredComponents.contains(cmp)) registredComponents.add(cmp);
        setTextsInComponent(cmp);
    }

    private static void setTextsInComponent(Component cmp) throws IllegalAccessException{
        String name = cmp.getClass().getSimpleName();
        Field[] listFields = cmp.getClass().getDeclaredFields();

        for(Field f : listFields){

            f.setAccessible(true);

            Object obj = f.get(cmp);

            if(obj instanceof JComponent){
                String keyToolTipText = name +"."+f.getName()+".toolTipText";
                String toolTipText = getText(keyToolTipText);
                if(toolTipText!=null){
                    ((JComponent)obj).setToolTipText(toolTipText);
                }

                try{
                    String keyText = name + "." + f.getName() + ".text";
                    String text = getText(keyText);
                    if (text != null) {
                        Method mSetText = Utilities.getMethod(f.getType(),"setText", String.class);
                        if (mSetText != null) {
                            mSetText.invoke(obj, text);
                        }else{
                             Utilities.printToLog("Cant find method. setText", null, Utilities.LOG_LEVEL_ERROR);
                        }
                    }
                }catch(Exception e){
                    Utilities.printToLog("Error al setear el texto. "+obj, e, Utilities.LOG_LEVEL_ERROR);
                }

                //checking if border text
                try{
                    String keyText = name + "." + f.getName() + ".border.text";
                    String text = getText(keyText);
                    if (text != null) {

                        
                        Border b = ((JComponent)obj).getBorder();
                        if(b!= null  && (b instanceof TitledBorder)){
                            ((JComponent)obj).setBorder(BorderFactory.createTitledBorder(text));
                        }
                    }
                }catch(Exception e ){
                    Utilities.printToLog("Error al setear el texto. "+obj, e, Utilities.LOG_LEVEL_ERROR);
                }
            }
        }
    }

    public static boolean esIdiomaValido(String idioma){
        if(idioma==null){
            return false;
        }
        if(idioma.equals(IDIOMA_ES) || idioma.equals(IDIOMA_EN)){
            return true;
        }
        return false;
    }
    
    


    

}
