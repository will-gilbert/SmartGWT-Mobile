/**
 * 
 */
package com.smartgwt.mobile.showcase.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class CountriesDataProviderServlet extends HttpServlet{

	private static final long serialVersionUID = -7449311593970677641L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
        int start = 0;
        String startRowParam = req.getParameter("startRow");
        if (startRowParam != null) {
            try {
                start = Integer.parseInt(startRowParam);
            } catch (NumberFormatException ex) {}
        }
        if (start < 0) start = 0;

        int end = 5;
        String endRowParam = req.getParameter("endRow");
        if (endRowParam != null) {
            try {
                end = Integer.parseInt(endRowParam);
            } catch (NumberFormatException ex) {}
        }
        if (end > 100) end = 100;

        resp.setContentType("application/json");

		PrintWriter wr = resp.getWriter();
        wr.write("{\"response\": {\"status\": 0,\"startRow\": " + start + ",\"endRow\": " + end + ",\"totalRows\": " + 100 + ",\"data\": [");
		for (int i = start; i < end; i++) {
            wr.write("{\"continent\": \"Asia\", \"_id\": \"Country_" + i + "\", \"countryName\": \"Country_" + i + "\", \"countryCode\": \"C" + i + "\"}");
			if (i < end - 1) {
				wr.write(",");
			}
		}
		wr.write("]}}");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
}
