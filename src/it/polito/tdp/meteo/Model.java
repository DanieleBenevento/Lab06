package it.polito.tdp.meteo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.bean.Citta;
import it.polito.tdp.meteo.bean.Rilevamento;
import it.polito.tdp.meteo.bean.SimpleCity;
import it.polito.tdp.meteo.db.MeteoDAO;

public class Model {


    private MeteoDAO m; 
    private List<Citta> lSimpleCity;
    private List<Citta> lCity;
    private List<Citta> lSoluzione;
    private double best;
 
	
    public Model() {

    	lSoluzione=new ArrayList<Citta>();
    	lSimpleCity=new ArrayList<Citta>(); 
		m=new MeteoDAO();
		lCity=new ArrayList<Citta>();
		
	}
    
    

	public List<Citta> getlSoluzione() {
		return lSoluzione;
	}

	


	public List<Citta> getlSimpleCity() {
		return lSimpleCity;
	}



	public String getUmiditaMedia(int mese) {

		String risultato="";
		for(String s: m.getAllLocalita()) {
			risultato+= s+" "+m.getAvgRilevamentiLocalitaMese(mese, s)+"\n";
		}
		
		return risultato.trim();
	}
	
public void genera(int mese,List<Citta> parziale) {
		lCity.clear();
		lSoluzione.clear();
		best=Double.MAX_VALUE;
		lCity.addAll(m.getAllCittaByMonth(mese));
		lSimpleCity.clear();
		this.trovaSequenza(mese,parziale, 0);
		
	}

	public void trovaSequenza(int mese,List<Citta> soluzioneParziale,int level) {

		/*
		 * nei primi quindici giorni tutte e 3 le citta devono essere visitate almeno una volta
		 * in nessuna citta si possono trascorrere piu di 6 giorni
		 * scelta una citta il tecnico non si può spostare prima di esserci stato per almeno 3 giorni consecutivi
		 * il costo(da minimizzare) è uguale a 100 per ogni spostamento ed un fattore variabile pari all umidita della citta
		 * 
		 */
		if(level==15) {
			if(soluzioneParziale.containsAll(lCity)==true ) {
		    if( this.punteggioSoluzione(soluzioneParziale, mese,level)<best) {
			    lSoluzione.clear();
		    	lSoluzione.addAll(soluzioneParziale);
		    	best=this.punteggioSoluzione(soluzioneParziale, mese,level);
		    	}
		    	return;
			}
		}
		else {
			for(int i=0;i<lCity.size();i++) {
				
				soluzioneParziale.add(lCity.get(i));	
				
				if(this.controllaParziale(soluzioneParziale, level)==true 
				   && this.punteggioSoluzione(soluzioneParziale, mese,level)<best ) {
					 					
					this.trovaSequenza(mese,soluzioneParziale,level+1);
			    	
				}
				
				soluzioneParziale.remove(level);
			}
		}
		return ;
	}

	private Double punteggioSoluzione(List<Citta> soluzioneCandidata, int mese,int level) {

		double score = 0.0;
		
		for(int i=0;i<soluzioneCandidata.size();i++) {
			score+= soluzioneCandidata.get(i).getRilevamenti().get(i).getUmidita();		
		if(i>0) {
			if(!soluzioneCandidata.get(i).getNome().equals(soluzioneCandidata.get(i-1).getNome())) {
				score+=100.0;
			}
		}
		}
		return score;
	}

	private boolean controllaParziale(List<Citta> parziale, int level) {

		int counter=1;
		
			for(int i=0;i<parziale.size();i++){
				
				if(i>0) {
					
					if(parziale.get(i).getNome().equals(parziale.get(i-1).getNome())==false) {
						
						if(counter>6 || counter<3) {
						
							return false;
						}
						
						counter=1;
					}
					
					else {
					
						counter++;
						
						if(counter>6) {
							
						return false;
						}
				}
				}
			if(i==14 && counter<3) {
				return false;
			}
					
			}
			
			return true;
	}
}