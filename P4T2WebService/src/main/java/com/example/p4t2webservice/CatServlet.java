/**
 * Muhammad Asad Shoaib | mshoaib | Project 4 Task 2
 * This is the servlet to be deployed on codespaces.
 */
package com.example.p4t2webservice;

import java.io.*;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "CatServlet", urlPatterns = {"/cat-servlet", "/dashboard"})
public class CatServlet extends HttpServlet {
    WebGetCatModel wgc = null;

    public void init() {
        wgc = new WebGetCatModel();
    }

    /**
     * This is the code for doGet
     * @param request is the request recieved from phone
     * @param response is the response from API to be sent back to phone
     * @throws IOException
     * @throws ServletException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // determine what type of device our user is
        String ua = request.getHeader("User-Agent");

        MongoP4T2 mongo = new MongoP4T2();

        // if URL equals cat-servlet
        if (request.getServletPath().equals("/cat-servlet")) {
            String word = request.getParameter("name");
            long start = System.currentTimeMillis();
            String resp = wgc.fetchURL(word);
            long end = System.currentTimeMillis();

            // add to mongo Model
            mongo.setResponse(wgc.getJSONResp());
            mongo.setWord(word);
            mongo.setUa(ua);
            mongo.setTimeTaken(end - start);
            mongo.setDateTime(start);
//            mongo.setSentToPhone(resp);

            // sending to phone as JSON
            JsonObject json = new JsonObject();
            json.addProperty("pictureURL", resp);

            String jsonString = new Gson().toJson(json);
            mongo.setSentToPhone(jsonString);

            // write to MongoDB
            wgc.addMongoDB(mongo);

            // write to android app
            PrintWriter out = response.getWriter();
            System.out.println(mongo.getResponse());
            out.println(mongo.getSentToPhone());

        }
        // if URL equals getDashboard
        if (request.getServletPath().equals("/dashboard")) {
            // below taken from Project 1 Task 3 desktop version
            request.setAttribute("doctype", "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");

            // code for computing things in dashboard
            ArrayList<String> results = new ArrayList<>();
            results = wgc.dashboardResults();

            // getting the analytical values
            String totalRequests = results.get(0);
            String topCommonTerms = results.get(1);
            String avgTime = results.get(2);

            // setting the analytical values
            request.setAttribute("count", totalRequests);
            request.setAttribute("topTerm", topCommonTerms);
            request.setAttribute("avgTime", avgTime);

            // getting the log values
            String log = results.get(3);

            // setting the log values
            request.setAttribute("log", log);

            RequestDispatcher view = request.getRequestDispatcher("dashboard.jsp");
            view.forward(request, response);

        }
    }

    public void destroy() {
    }
}