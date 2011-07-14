package it.spisser.android.acqua;




public class Fonte implements Comparable<Fonte> {
	private long Id;
	private String nomeCommerciale;
	private String nomeFonte;
	private String comuneFonte;
	
	private String provinciaFonte;
	
	private Double lat_gradi_dec;
	private Double long_gradi_dec;
	private Double distanza;
	private Double distanzaMaps;

	public int compareTo(Fonte fonte) {
		int result = 0;
		if (this.distanza > fonte.distanza) {
			result = 1;
		}
		if (this.distanza < fonte.distanza) {
			result = -1;
		}
		if (this.distanza == fonte.distanza) {
			result = 0;
		}

		return result;

	}

	public Double getDistanzaMaps() {
		return distanzaMaps;
	}

	public void setDistanzaMaps(Double distanzaMaps) {
		this.distanzaMaps = distanzaMaps;
	}

	public Double getDistanza() {
		return distanza;
	}

	public void setDistanza(Double distanza) {
		this.distanza = distanza;
	}

	public String getNomeCommerciale() {
		return nomeCommerciale;
	}

	public void setNomeCommerciale(String nomeCommerciale) {
		this.nomeCommerciale = nomeCommerciale;
	}

	public String getNomeFonte() {
		return nomeFonte;
	}

	public void setNomeFonte(String nomeFonte) {
		this.nomeFonte = nomeFonte;
	}

	public String getComuneFonte() {
		return comuneFonte;
	}

	public void setComuneFonte(String comuneFonte) {
		this.comuneFonte = comuneFonte;
	}

	public String getProvinciaFonte() {
		return provinciaFonte;
	}

	public void setProvinciaFonte(String provinciaFonte) {
		this.provinciaFonte = provinciaFonte;
	}

	public Double getLat_gradi_dec() {
		return lat_gradi_dec;
	}

	public void setLat_gradi_dec(Double lat_gradi_dec) {
		this.lat_gradi_dec = lat_gradi_dec;
	}

	public Double getLong_gradi_dec() {
		return long_gradi_dec;
	}

	public void setLong_gradi_dec(Double long_gradi_dec) {
		this.long_gradi_dec = long_gradi_dec;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public String toString(){
		return this.nomeCommerciale;
	}
}
