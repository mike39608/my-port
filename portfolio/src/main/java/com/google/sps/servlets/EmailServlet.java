package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/emailReceiver")
public class EmailServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    // Get the value entered in the form.
    String emailValue = request.getParameter("email-input");

    // Print the value so you can see it in the server logs.
    System.out.println("Here is an email: " + emailValue);

    // Write the value to the response so the user can see it.
    response.getWriter().println("You have submitted the email: " + emailValue);
    //response.sendRedirect("https://google.com");
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    datastore.put(emailValue);

  }

}