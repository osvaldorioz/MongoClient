package com.ucuau.datawarehousing.mongodb.vo;

public class MaterialDidacticoVO {
	private _Id _id;
	private String idmaterial;
	private String nombre;
	private String url;
	private String tipo;
	
	
	/**
	 * @return the idmaterial
	 */
	public String getIdmaterial() {
		return idmaterial;
	}
	/**
	 * @param idmaterial the idmaterial to set
	 */
	public void setIdmaterial(String idmaterial) {
		this.idmaterial = idmaterial;
	}
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * @return the _id
	 */
	public _Id get_id() {
		return _id;
	}
	/**
	 * @param _id the _id to set
	 */
	public void set_id(_Id _id) {
		this._id = _id;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MaterialDidacticoVO [idmaterial=" + idmaterial + ", nombre="
				+ nombre + ", url=" + url + ", tipo=" + tipo + "]";
	}
	
	
}
