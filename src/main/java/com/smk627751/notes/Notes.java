package com.smk627751.notes;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.smk627751.model.Note;
import com.smk627751.repository.NotesRepository;


@WebServlet("/notes")
public class Notes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		setCorsHeaders(response);
		List<Note> notes = NotesRepository.getInstance().getNotes();
		JSONArray array = new JSONArray();	
		notes.forEach((note) -> {
			JSONObject json = new JSONObject();
			json.put("id", note.getId());
			json.put("title", note.getTitle());
			json.put("content", note.getContent());
			json.put("lastEdit", note.getLastEdit());
			array.add(json);
		});
		response.getWriter().write(array.toString());
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		NotesRepository.getInstance().addNote();
	}
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		setCorsHeaders(response);
		String id = request.getParameter("id");
		StringBuilder requestBody = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        reader.close();
        String requestBodyString = requestBody.toString();
        JSONParser obj = new JSONParser();
        JSONObject json = null;
        try {
			json = (JSONObject) obj.parse(requestBodyString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        NotesRepository.getInstance().updateNote(new Note(Integer.parseInt(id),(String)json.get("title"),(String)json.get("content"),null));
        response.setStatus(HttpServletResponse.SC_OK);
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		setCorsHeaders(response);
		int id = Integer.parseInt(request.getParameter("id"));
		NotesRepository.getInstance().deleteNote(id);
	}
	
	private void setCorsHeaders(HttpServletResponse response) {
	        response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5500");
	        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
	        response.setHeader("Access-Control-Allow-Headers",
	                "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
	        response.setHeader("Access-Control-Allow-Credentials", "true");
    }
	
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
