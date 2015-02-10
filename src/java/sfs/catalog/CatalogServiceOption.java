/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sfs.catalog;

import sfs.service.AdminFaqService;

/**
 *
 * @author WL72166
 */
public class CatalogServiceOption {
    
    public static final String GET_USER = "getUser";
    public static final String ADD_USER = "addUser";
    public static final String DELETE_USER = "deleteUser";
    public static final String MODIFY_USER = "modifyUser";
    public static final String GET_PERMISSION_LIST = "getPermissionList";
    public static final String ADD_PERMISSION = "addPermission";
    public static final String DELETE_PERMISSION = "deletePermission";
    /**
     * Get a faq by ID<br/>
     * <b>Arguments:</b> faqId Integer
     * @see AdminFaqService
     */
    public static final String GET_FAQ = "getFaq";
    /**
     * Get a topic by ID<br/>
     * <b>Arguments:</b> topicId Integer
     */
    public static final String GET_TOPIC = "getFaqTopic";
    
    /**
     * Obtiene todos los FAQS de un topico incluyendo sus subtopicos
     * <b>Arguments:</b> topicId Integer
     * <b>return</b> List<Faq>
     */
    public static final String GET_TOPIC_FAQS_RECURSIVE = "getTopicFaqsRecursive";
    
    
    public static final String LIST_FAQ = "listFaqs";
    /**
     * Agrega una entidad de tipo Faq
     * <b>Arguments:</b> faq Faq
     * @see AdminFaqService
     */
    public static final String ADD_FAQ = "addFaq";
    public static final String DELETE_FAQ = "deleteFaq";
    public static final String MODIFY_FAQ = "modifyFaq";
    public static final String GET_ROOT_FAQ_TOPICS = "getRootFaqTopics";
    public static final String ADD_FAQ_TOPIC = "addFaqTopic";
    public static final String DELETE_FAQ_TOPIC = "deleteFaqTopic";
    /**
     * Add the faq score added for a user<br/>
     * <b>Arguments:</b> <br/>
     * 1: user User <br/>
     */
    public static final String ADD_FAQ_SCORE = "addFaqScore";
    /**
     * Get the faq score added for a user<br/>
     * <b>Arguments:</b> <br/>
     * 1: user User <br/>
     * 2: faq  Faq
     * @see AdminFaqService.addFaqScore
     */
    public static final String GET_FAQ_SCORE = "getFaqScore";
    /**
     * Remove from database the specified FaqScore
     * <b>Arguments:</b> <br/>
     * 1: fs FaqScore 
     */
    public static final String DELETE_FAQ_SCORE = "deleteFaqScore";
    public static final String ADD_FAQ_COMMENT = "addFaqComment";
    public static final String DELETE_FAQ_COMMENT = "deleteFaqComment";
    public static final String MODIFY_FAQ_COMMENT = "modifyFaqComment";
    /**
     * Agrega una nueva entidad UploadedFile
     * 
     * * <b>Arguments:</b> <br/>
     * 1: uf UploadedFile <br/>
     */
    public static final String ADD_UPLOADED_FILE =  "addUploadedFile";
    /**
     * Busca los FAQs que contengan en su titulo y descripcion corta o larga el texto clave enviado
     * * * <b>Arguments:</b> <br/>
     * 1: textKey String (Clave de busqueda) <br/>
     */
    public static final String FIND_FAQ =  "findFaq";
    
    /**
     * Obtiene los datos de los FAQ mejor valorados<br/>
     * Ningún argumento.<br/>
     * return List<FaqCalculatedRanking><br/>
     * 
     */
    public static final String GET_FAQ_TOP_RANKED = "getFaqTopRanked";
    /**
     * Obtiene los FAQ  mas visitados<br/>
     * return List<Faq><br/>
     */
    public static final String GET_FAQ_TOP_VISITED = "getFaqTopVisited";
    /**
     * Obtiene los FAQ creados más recientemente<br/>
     * return List<Faq><br/>
     */
    public static final String GET_LAST_NEW_FAQS = "getLastNewFaqs";
    
    /**
     * Agrega una nueva entidad de tipo FAQ a la base de datos<br/>
     * * * <b>Arguments:</b> <br/>
     * 1: faqContact FaqContact <br/>
     * return Void
     */
    public static final String ADD_FAQ_CONTACT = "addFaqContact";
    /**
     * Elimina una entidad de tipo FAQ a la base de datos<br/>
     * * * <b>Arguments:</b> <br/>
     * 1: faqContact FaqContact <br/>
     * return Void
     */
    public static final String DELETE_FAQ_CONTACT = "deleteFaqContact";
    /**
     * Agrega un dato al contador de visitas del faq
     * <b>Arguments:</b> <br/>
     * 1: faq Faq <br/>
     * return Void
     * 
     */
    public static final String ADD_VISIT_TO_FAQ = "addVisitToFaq";
    
    
    /**
     * Cuenta los faqs que estan asociados a un topico y sus hijos
     * <b>Arguments:</b> <br/>
     * 1: faqTopic FaqTopic <br/>
     * return Long
     */
    public static final String GET_FAQ_COUNT_RECURSIVE = "getFaqCountRecursive";
    
}
