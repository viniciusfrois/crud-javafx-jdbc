package models.dao;

import java.util.List;
import model.entities.Vendedor;
import model.entities.Departamento;

public interface VendedorDAO {
	
	void inserir (Vendedor obj);
	void atualizar (Vendedor obj);
	void deletarById (Integer id);
	Vendedor buscarId (Integer id);
	List <Vendedor> buscarTodos ();
	List <Vendedor> buscarDepartamento (Departamento departamento);
	

}
