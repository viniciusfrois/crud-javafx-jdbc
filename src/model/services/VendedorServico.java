package model.services;

import java.util.List;
import model.entities.Vendedor;
import models.dao.VendedorDAO;
import models.dao.FabricaDAO;

public class VendedorServico {

	private VendedorDAO dao = FabricaDAO.criarVendedorDao();

	public List<Vendedor> buscarTodos() {
		return dao.buscarTodos();
	}

	public void SalvarOuAtualizar(Vendedor obj) {
		if (obj.getId() == null) {
			dao.inserir(obj);
		} else {
			dao.atualizar(obj);
		}
	}

	public void remove(Vendedor obj) {
		dao.deletarById(obj.getId());
	}
}