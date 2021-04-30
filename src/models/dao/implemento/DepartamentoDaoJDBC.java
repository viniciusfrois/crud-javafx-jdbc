package models.dao.implemento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DBConnect;
import db.DbException;
import model.entities.Departamento;
import models.dao.DepartamentoDAO;

public class DepartamentoDaoJDBC implements DepartamentoDAO {
	
	private Connection conn;

	public DepartamentoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void inserir(Departamento obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
			"INSERT INTO departamento " 
			+ "(Nome) "
			+ "VALUES " 
			+ "(?)",
			Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			
			int linhasAfetadas = st.executeUpdate();
			if (linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
					}
				DBConnect.closeResultSet(rs);
			} else {
				throw new DbException("Erro, nenhuma linha foi afetada!");
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DBConnect.closeStatement(st);
		}

	}

	@Override
	public void atualizar(Departamento obj) {
		try {
			PreparedStatement st = null;
			st = conn.prepareStatement(
			"UPDATE departamento " 
			+ "SET Nome = ? "
			+ "WHERE Id = ?",
			Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setInt(1, obj.getId());
			
			st.executeUpdate();
			
			DBConnect.closeStatement(st);
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		
	}

	@Override
	public void deletarById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
				"DELETE FROM departamento WHERE Id = ?");

			st.setInt(1, id);

			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} 
		finally {
			DBConnect.closeStatement(st);
		}
		
	}

	@Override
	public Departamento buscarId(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM departamento WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Departamento obj = new Departamento();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Nome"));
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DBConnect.closeStatement(st);
			DBConnect.closeResultSet(rs);
		}
	}

	@Override
	public List<Departamento> buscarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
				"SELECT * FROM departamento ORDER BY Nome");
			rs = st.executeQuery();

			List<Departamento> list = new ArrayList<>();

			while (rs.next()) {
				Departamento obj = new Departamento();
				obj.setId(rs.getInt("Id"));
				obj.setName(rs.getString("Nome"));
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DBConnect.closeStatement(st);
			DBConnect.closeResultSet(rs);
		}
	}

}
