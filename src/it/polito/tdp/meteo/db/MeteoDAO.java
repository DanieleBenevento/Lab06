package it.polito.tdp.meteo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {

		String sql = "SELECT Localita, Data, Umidita FROM situazione WHERE MONTH(DATA)= ? AND Localita= ? ";
		List<Rilevamento> rilevamentiLocMese = new ArrayList<Rilevamento>();
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1,mese);
			st.setString(2, localita);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamentiLocMese.add(r);
			}

			conn.close();
			return rilevamentiLocMese;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Double getAvgRilevamentiLocalitaMese(int mese, String localita) {

		String sql = "SELECT Localita, Data, Umidita FROM situazione WHERE MONTH(DATA)= ? AND Localita= ? ";
		List<Rilevamento> rilevamentiLocMese = new ArrayList<Rilevamento>();
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, mese);
			st.setString(2, localita);
			ResultSet rs = st.executeQuery();

			
			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamentiLocMese.add(r);
			}
			double somma=0.0;
			for(Rilevamento r:rilevamentiLocMese) {
				somma+=r.getUmidita();
			}

			conn.close();
			return somma/rilevamentiLocMese.size();

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<String> getAllLocalita() {
	
		String sql = "SELECT Localita, Data, Umidita FROM situazione ";
		List<String> lLocalita = new ArrayList<String>();
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				String l=rs.getString("Localita");
				if(!lLocalita.contains(l)) {
				lLocalita.add(l);
				}
			}

			conn.close();
			return lLocalita;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	
	}

	public List<Citta> getAllCitta() {
		
		String sql = "SELECT Localita, Data, Umidita FROM situazione ";
		List<Citta> lCitta = new ArrayList<Citta>();
		List<Rilevamento> lRilevamenti = new ArrayList<Rilevamento>();
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				boolean presente=false;
				Rilevamento r =new Rilevamento(rs.getString("Localita"),rs.getDate("Data"),rs.getInt("Umidita"));
				lRilevamenti.add(r);
				Citta c=new Citta(rs.getString("Localita"));
				for(int i=0;i< lCitta.size();i++) {
				if(lCitta.get(i).getNome().equals(c.getNome())) {
				presente=true;
				}
				}
				if(presente==false) {
				lCitta.add(c);
				}
			}
			
			for(Citta c:lCitta) {
				for(Rilevamento ri:lRilevamenti) {
				if(c.getNome().equals(ri.getLocalita())) {
					c.setRilevamenti(ri);
					}
				}
			}

			conn.close();
			return lCitta;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	
	}
	
public List<Citta> getAllCittaByMonth(int mese) {
		
		String sql = "SELECT Localita, Data, Umidita FROM situazione WHERE MONTH(Data) =  ? ";
		List<Citta> lCittaByMonth = new ArrayList<Citta>();
		List<Rilevamento> lRilevamentiByMonth = new ArrayList<Rilevamento>();
		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				boolean presente=false;
				Rilevamento r =new Rilevamento(rs.getString("Localita"),rs.getDate("Data"),rs.getInt("Umidita"));
				lRilevamentiByMonth.add(r);
				Citta c=new Citta(rs.getString("Localita"));
				for(int i=0;i< lCittaByMonth.size();i++) {
				if(lCittaByMonth.get(i).getNome().equals(c.getNome())) {
				presente=true;
				}
				}
				if(presente==false) {
				lCittaByMonth.add(c);
				}
			}
			
			for(Citta c:lCittaByMonth) {
				for(Rilevamento ri:lRilevamentiByMonth) {
				if(c.getNome().equals(ri.getLocalita())) {
					c.setRilevamenti(ri);
					}
				}
			}

			conn.close();
			return lCittaByMonth;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
}
}

