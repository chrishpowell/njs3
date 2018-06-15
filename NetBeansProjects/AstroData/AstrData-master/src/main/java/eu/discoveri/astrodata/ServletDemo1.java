/*
 * Copyright (C) 2018 chris
 */
package eu.discoveri.astrodata;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author chris
 */
public class ServletDemo1 extends HttpServlet
{
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        resp.setContentType("text/html");
        try( PrintWriter out = resp.getWriter() )
        {
            out.print("<html><body><h1>Yo!</h1><body></html>");
        }
    }
}
