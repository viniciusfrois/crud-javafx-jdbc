package models.dao.implemento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import db.DBConnect;
import db.DbException;
import model.entities.Departamento;
import model.entities.Vendedor;
import models.dao.VendedorDAO;

public class VendedorDaoJDBC implements VendedorDAO {

	private Connection conn;

	public VendedorDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void inserir(Vendedor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
			"INSERT INTO vendedor " 
			+ "(Nome, Email, DataNascimento, Salario, DepartamentoId) "
			+ "VALUES " 
			+ "(?, ?, ?, ?, ?)",
			Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, (java.sql.Date) new Date(obj.getDataNascimento().getTime()));
			st.setDouble(4, obj.getSalario());
			st.setInt(5, obj.getDepartamento().getId());	
			
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
	public void atualizar(Vendedor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
			"UPDATE vendedor " 
			+ "SET Nome = ?, Email = ?, DataNascimento = ?, Salario = ?, DepartamentoId = ? "
			+ "WHERE Id = ?",
			Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, (java.sql.Date) new Date(obj.getDataNascimento().getTime()));
			st.setDouble(4, obj.getSalario());
			st.setInt(5, obj.getDepartamento().getId());
			st.setInt(6, obj.getId());
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DBConnect.closeStatement(st);
		}

	}

	@Override
	public void deletarById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
			"DELETE FROM vendedor " 
		  + "WHERE Id = ?");			
			st.setInt(1, id);
			
			int linhasAfetadas = st.executeUpdate();
			
			if (linhasAfetadas == 0) {
				throw new DbException("Esse ID não Existe!");
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DBConnect.closeStatement(st);
		}

	}

	@Override
	public Vendedor buscarId(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
			"SELECT vendedor.*,departamento.Nome as DepNome " 
			+ "FROM vendedor INNER JOIN departamento "
			+ "ON vendedor.DepartamentoId = departamento.Id " 
			+ "WHERE vendedor.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {

				Departamento dep = instanciaDepartamento(rs);
				Vendedor vend = instanciaVendedor(rs, dep);
				return vend;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DBConnect.closeStatement(st);
			DBConnect.closeResultSet(rs);
		}

	}
	
	@Override
	public List<Vendedor> buscarTodos() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
			"SELECT vendedor.*,departamento.Nome as DepNome " 
			+ "FROM vendedor INNER JOIN departamento "
			+ "ON vendedor.DepartamentoId = departamento.Id " 
			+ "ORDER BY Nome");
						
			rs = st.executeQuery();
			
			List<Vendedor> lista = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();
			
			while (rs.next()) {				
				Departamento dep = map.get(rs.getInt("DepartamentoId"));
				if (dep==null) {
					dep = instanciaDepartamento(rs);
					map.put(rs.getInt("DepartamentoId"), dep);
				}				
				Vendedor vend = instanciaVendedor(rs, dep);
				lista.add(vend);
			}
			return lista;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DBConnect.closeStatement(st);
			DBConnect.closeResultSet(rs);
		}
	}

	@Override
	public List<Vendedor> buscarDepartamento(Departamento departamento) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
			"SELECT vendedor.*,departamento.Nome as DepNome " 
			+ "FROM vendedor INNER JOIN departamento "
			+ "ON vendedor.DepartamentoId = departamento.Id " 
			+ "WHERE departamento.Id = ? ORDER BY Nome");
			
			st.setInt(1, departamento.getId());			
			rs = st.executeQuery();
			
			List<Vendedor> lista = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();
			
			while (rs.next()) {				
				Departamento dep = map.get(rs.getInt("DepartamentoId"));
				if (dep==null) {
					dep = instanciaDepartamento(rs);
					map.put(rs.getInt("DepartamentoId"), dep);
				}				
				Vendedor vend = instanciaVendedor(rs, dep);
				lista.add(vend);
			}
			return lista;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DBConnect.closeStatement(st);
			DBConnect.closeResultSet(rs);
		}
	}

	private Departamento instanciaDepartamento(ResultSet rs) throws SQLException {
		Departamento dep = new Departamento();
		dep.setId(rs.getInt("DepartamentoId"));
		dep.setName(rs.getString("DepNome"));
		return dep;
	}
	
	private Vendedor instanciaVendedor(ResultSet rs, Departamento dep) throws SQLException {
		Vendedor vend = new Vendedor();
		vend.setId(rs.getInt("Id"));
		vend.setNome(rs.getString("Nome"));
		vend.setEmail(rs.getString("Email"));
		vend.setSalario(rs.getDouble("Salario"));
		vend.setDataNascimento(new java.util.Date(rs.getTimestamp("DataNascimento").getTime()));
		vend.setDepartamento(dep);
		return vend;
	}
}
