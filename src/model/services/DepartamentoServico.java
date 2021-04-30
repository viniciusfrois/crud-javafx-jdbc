package model.services;

import java.util.List;
import model.entities.Departamento;
import models.dao.DepartamentoDAO;
import models.dao.FabricaDAO;

public class DepartamentoServico {
	
	private DepartamentoDAO dao = FabricaDAO.criarDepartamentoDao();	
	
	public List<Departamento> buscarTodos (){		
		return dao.buscarTodos();
	}
	
	public void SalvarOuAtualizar (Departamento obj) {
		if (obj.getId() == null) {
			dao.inserir(obj);
		} else {
			dao.atualizar(obj);
		}
	}
}
