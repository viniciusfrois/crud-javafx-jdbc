package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Departamento;

public class DepartamentoServico {
	
	public List<Departamento> buscarTodos (){
		
		List<Departamento> lista = new ArrayList<>();
		lista.add(new Departamento (1, "Livros"));
		lista.add(new Departamento (2, "Autopeças"));
		lista.add(new Departamento (3, "Eletronicos"));
		return lista;
		
	}

}
