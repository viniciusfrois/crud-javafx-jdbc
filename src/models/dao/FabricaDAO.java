package models.dao;

import db.DBConnect;
import models.dao.implemento.DepartamentoDaoJDBC;
import models.dao.implemento.VendedorDaoJDBC;

public class FabricaDAO {
	
	public static VendedorDAO criarVendedorDao () {
		return new VendedorDaoJDBC(DBConnect.getConnection());
	}
	
	public static DepartamentoDAO criarDepartamentoDao() {
		return new DepartamentoDaoJDBC(DBConnect.getConnection());
	}
}
