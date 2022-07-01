package com.google.sps.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.FullEntity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.sps.data.Email;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

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

    String email = Jsoup.clean(request.getParameter("email"), Whitelist.none());
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyForData = datastore.newKeyFactory().setKind("Email");
    FullEntity emailEntity =
        Entity.newBuilder(keyForData.newKey())
        .set("email", emailValue)
        .build();
    datastore.put(emailEntity);

    response.sendRedirect("/index.html");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    Query<Entity> query =
        Query.newEntityQueryBuilder().setKind("Email").build();
    QueryResults<Entity> results = datastore.run(query);
    
    List<Email> emails = new ArrayList<>();
    while (results.hasNext()) {
    Entity entity = results.next();

    long id = entity.getKey().getId();
    String email = entity.getString("email");

    Email emailTask = new Email(id, email);
    emails.add(emailTask);
    }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(emails));
  }

}