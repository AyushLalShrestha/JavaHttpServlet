/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leapfrog.servlettest.controller;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Dell
 */
public class DefaultController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/index.jsp");
        rd.forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productType1 = req.getParameter("productName");
        String productType = productType1.replace(" ", "+");
        req.setAttribute("product", productType);
        RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/searchResults.jsp");
        String link = "https://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords=" + productType;
       
        URL url = new URL(link);
        URLConnection conn = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        StringBuilder content = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
            
        }
        FileWriter writer = new FileWriter("d:/amazonSearch.html");
        writer.write(content.toString());
        writer.close();
        System.out.println(content.toString());
        String regEx = "<div class=\"(.*?)\"><a class=\"(.*?)\" title=\"(.*?)\" href=\"(.*?)\">";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(content.toString());
        StringBuilder content1 = new StringBuilder();
        while (matcher.find()) {
            String str1 = matcher.group(4);
            content1.append(str1).append("\n");
            System.out.println(content1);
        }
        FileWriter writer1 = new FileWriter("d:/amazonSearchLinks.txt");
        writer1.write(content1.toString());
        writer1.close();
        rd.forward(req, resp);
    }

}
