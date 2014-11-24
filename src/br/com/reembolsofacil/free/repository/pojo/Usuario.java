package br.com.reembolsofacil.free.repository.pojo;

import java.io.Serializable;
import java.util.Date;

public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String ID = "_id", ID_USUARIO = "id_usuario", EMAIL_USUARIO="email_usuario",
		TIPO_USUARIO="tipo_usuario",LOGIN="login",SENHA="senha",DATA_HORA = "data_hora";
	
	public static final String[] COLUMNS = new String[] { ID, ID_USUARIO,
		EMAIL_USUARIO, TIPO_USUARIO, LOGIN, SENHA, DATA_HORA };

	private long id;
	
	private Long idUsuario;

	private String emailUsuario;

	private String tipoUsuario;

	private String login;

	private String senha;
	
	private Date dataHora;

	public Usuario() {
	}

	public Usuario(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getEmailUsuario() {
		return emailUsuario;
	}

	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", idUsuario=" + idUsuario
				+ ", tipoUsuario=" + tipoUsuario + ", emailUsuario="
				+ emailUsuario + ", login=" + login + ", senha=" + senha
				+ ", dataHora=" + dataHora + "]";
	}

}