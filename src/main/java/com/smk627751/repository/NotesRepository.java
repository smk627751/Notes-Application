package com.smk627751.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.smk627751.model.Note;

public class NotesRepository {
	private static NotesRepository Repo;
	Connection con;
	private NotesRepository()
	{
		try {
			Class.forName("org.postgresql.Driver");
			String dbURL = "jdbc:postgresql://localhost:5432/smk627751";
			String user = "postgres";
			String pass = "627751";
			this.con = DriverManager.getConnection(dbURL, user, pass);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	public static NotesRepository getInstance()
	{
		if(Repo == null)
		{
			Repo = new NotesRepository();
		}
		return Repo;
	}
	public List<Note> getNotes()
	{
		try {
			Statement s = con.createStatement();
			String query = "SELECT * FROM notes_table";
			ResultSet rs = s.executeQuery(query);
			List<Note> notes = new ArrayList<>();
			while(rs.next())
			{
				notes.add(new Note(rs.getInt("id"),rs.getString("title"),rs.getString("content"),rs.getString("last_edit")));
			}
			return notes;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean updateNote(Note note)
	{
		try {
			String query = "UPDATE notes_table SET title = ?,content = ?,last_edit = CURRENT_TIMESTAMP WHERE id = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, note.getTitle());
			ps.setString(2, note.getContent());
			ps.setInt(3,note.getId());
			int rowsAffected = ps.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public void addNote() {
		String query = "INSERT INTO notes_table (title,content) VALUES('Title','Content')";
		try {
			Statement s = con.createStatement();
			s.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void deleteNote(int id)
	{
		String query = "DELETE FROM notes_table WHERE id = ?";
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
