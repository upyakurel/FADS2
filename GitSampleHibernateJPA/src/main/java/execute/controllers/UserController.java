package execute.controllers;

import execute.models.User;
import execute.models.UserDao;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * A class to test interactions with the MySQL database using the UserDao class.
 *
 * @author netgloo
 */
@RestController
public class UserController {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * /create  --> Create a new user and save it in the database.
   * 
   * @param email User's email
   * @param name User's name
   * @return A string describing if the user is succesfully created or not.
   */
  @RequestMapping(value = "/create", method = RequestMethod.POST)
  @ResponseBody
  public String create(@RequestBody Map<String, String> emp) {
	  
	  String email = emp.get("email");
	  String name = emp.get("name");	  
  		
  		User user = null;
  	    try {
  	      user = new User(email, name);
  	      userDao.save(user);
  	    }
  	    catch (Exception ex) {
  	    	return "Failure: " + ex.getMessage();
  	    }
  	  return "Success: New User at: " + user.getId();
  	  
  }
  
  /**
   * /delete  --> Delete the user having the passed id.
   * 
   * @param id The id of the user to delete
   * @return A string describing if the user is succesfully deleted or not.
   */
  @RequestMapping("/delete")
  @ResponseBody
  public Map<String, String> delete(@RequestBody long id) {
    try {
      User user = new User(id);
      userDao.delete(user);
    }
    catch (Exception ex) {
      return Collections.singletonMap("response", "User not deleted, error: " + ex.toString());
    }
    return Collections.singletonMap("response", "User: " + id + " successfully deleted.");
  }
  
  /**
   * /get-by-email  --> Return the id for the user having the passed email.
   * 
   * @param email The email to search in the database.
   * @return The user id or a message error if the user is not found.
   */
  @RequestMapping(value = "/findEmail", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, String> getByEmail(@RequestBody Map<String, String> mapE) {
	  System.out.println(mapE.toString());
	  System.out.println("Email from request: " + mapE.get("email"));
	  Map<String, String> response = new HashMap<String, String>();
    String userId;
    try {
    	String email = mapE.get("email");
    	List<User> users = new ArrayList<User>();
    	users.add(0, userDao.findByEmail(email)); 
    	
      User user = users.get(0);
      userId = String.valueOf(user.getId());
      System.out.println("User ID: " + userId);
    }
    catch (Exception ex) {
    	response.put("failure", "NoMatch" + ex.toString());
      return response;
    }
     response.put("success", "User ID: " + userId);
    return response;
    
  }
  
  /**
   * /update  --> Update the email and the name for the user in the database 
   * having the passed id.
   * 
   * @param id The id for the user to update.
   * @param email The new email.
   * @param name The new name.
   * @return A string describing if the user is succesfully updated or not.
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, String> updateUser(@RequestBody Map<String, String> updateUser) {
	  String id = updateUser.get("id");
	  long idLong = Long.parseLong(id);
	  String email = updateUser.get("email");
	  String name = updateUser.get("name");
    try {
      User user = userDao.findOne(idLong);
      user.setEmail(email);
      user.setName(name);
      userDao.save(user);
    }
    catch (Exception ex) {
      return Collections.singletonMap("response", "Error updating the user: " + ex.toString());
    }
    return Collections.singletonMap("response", "UserId: " + id + " with name " + name + " successfully updated.");
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private UserDao userDao;
  
} // class UserController


