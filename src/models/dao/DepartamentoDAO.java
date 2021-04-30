package models.dao;

import model.entities.Departamento;
import java.util.List;

public interface DepartamentoDAO {

	void inserir (Departamento obj);
	void atualizar (Departamento obj);
	void deletarById (Integer id);
	Departamento buscarId (Integer id);
	List <Departamento> buscarTodos ();
	
}
