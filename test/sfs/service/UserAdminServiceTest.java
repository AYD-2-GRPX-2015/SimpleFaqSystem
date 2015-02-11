/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sfs.service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sfs.catalog.CatalogServiceOption;
import sfs.persistence.objects.User;
import sfs.service.common.Service;

/**
 *
 * @author williams
 */
public class UserAdminServiceTest {
    
    public UserAdminServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getUser method, of class UserAdminService.
     */
    @Test
    public void testGetUser() throws Exception {
        System.out.println("getUser");
        String user = "admin";
        String password = "admin";
        UserAdminService instance = new UserAdminService();
        User expResult = new User("admin");
        
        //User result = instance.getUser(user, password);
        User result = (User)Service.exec(expResult, CatalogServiceOption.GET_USER, user,password);
        
        if(result == null){
            fail("Must return admin user");
        }else{
            System.out.println("EJECUCION EXITOSA");
        }   
    }

//    /**
//     * Test of getPermissionList method, of class UserAdminService.
//     */
//    @Test
//    public void testGetPermissionList() throws Exception {
//        System.out.println("getPermissionList");
//        User user = null;
//        UserAdminService instance = new UserAdminService();
//        List<UserPermission> expResult = null;
//        List<UserPermission> result = instance.getPermissionList(user);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addUser method, of class UserAdminService.
//     */
//    @Test
//    public void testAddUser() throws Exception {
//        System.out.println("addUser");
//        User usr = null;
//        String passwordConfirm = "";
//        UserAdminService instance = new UserAdminService();
//        instance.addUser(usr, passwordConfirm);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of deleteUser method, of class UserAdminService.
//     */
//    @Test
//    public void testDeleteUser() throws Exception {
//        System.out.println("deleteUser");
//        User usr = null;
//        UserAdminService instance = new UserAdminService();
//        instance.deleteUser(usr);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of modifyUser method, of class UserAdminService.
//     */
//    @Test
//    public void testModifyUser() throws Exception {
//        System.out.println("modifyUser");
//        User usr = null;
//        UserAdminService instance = new UserAdminService();
//        instance.modifyUser(usr);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of addPermission method, of class UserAdminService.
//     */
//    @Test
//    public void testAddPermission() {
//        System.out.println("addPermission");
//        UserPermission spo = null;
//        UserAdminService instance = new UserAdminService();
//        instance.addPermission(spo);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of deletePermission method, of class UserAdminService.
//     */
//    @Test
//    public void testDeletePermission() {
//        System.out.println("deletePermission");
//        UserPermission spo = null;
//        UserAdminService instance = new UserAdminService();
//        instance.deletePermission(spo);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getServiceName method, of class UserAdminService.
//     */
//    @Test
//    public void testGetServiceName() {
//        System.out.println("getServiceName");
//        UserAdminService instance = new UserAdminService();
//        String expResult = "";
//        String result = instance.getServiceName();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//    
}
