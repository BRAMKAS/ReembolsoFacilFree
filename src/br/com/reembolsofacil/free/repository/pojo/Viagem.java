package br.com.reembolsofacil.free.repository.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import android.util.Log;


public class Viagem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String ID = "_id", ID_VIAGEM = "id_viagem",
	FK_USUARIO = "fk_usuario", DATA_INICIO_VIAGEM = "data_inicio_viagem",
	DATA_FIM_VIAGEM = "data_fim_viagem", MOTIVO_VIAGEM = "motivo_viagem",
	ABERTA = "aberta",ADIANTAMENTO="adiantamento", DATA_HORA = "data_hora";
	
	public static final String[] COLUMNS = new String[] { ID, ID_VIAGEM,
		FK_USUARIO, DATA_INICIO_VIAGEM, DATA_FIM_VIAGEM, MOTIVO_VIAGEM,
		ABERTA, ADIANTAMENTO, DATA_HORA };

	private long id;
	
	private Long idViagem;
	
	private Usuario usuario;
	
	private Date dataInicioViagem;
	
	private Date dataFimViagem;
	
	private String motivoViagem;
	
	private boolean aberta;

	private BigDecimal adiantamento;
	
	private Date dataHora;
	
	//transient
	private BigDecimal totalDespesas;
	
	//transient
	private BigDecimal saldo;
	
	//transient
	private List<Despesa> despesas;

    public Viagem() {
    }

	public Viagem(long id) {
		super();
		this.id = id;
	}

	public Viagem(long id, Long idViagem, Date dataInicioViagem,
			Date dataFimViagem, String motivoViagem, boolean aberta,
			BigDecimal adiantamento, Date dataHora) {
		super();
		this.id = id;
		this.idViagem = idViagem;
		this.dataInicioViagem = dataInicioViagem;
		this.dataFimViagem = dataFimViagem;
		this.motivoViagem = motivoViagem;
		this.aberta = aberta;
		this.adiantamento = adiantamento;
		this.dataHora = dataHora;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getIdViagem() {
		return idViagem;
	}

	public void setIdViagem(Long idViagem) {
		this.idViagem = idViagem;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getDataInicioViagem() {
		return dataInicioViagem;
	}

	public void setDataInicioViagem(Date dataInicioViagem) {
		this.dataInicioViagem = dataInicioViagem;
	}

	public Date getDataFimViagem() {
		return dataFimViagem;
	}

	public void setDataFimViagem(Date dataFimViagem) {
		this.dataFimViagem = dataFimViagem;
	}

	public String getMotivoViagem() {
		return motivoViagem;
	}

	public void setMotivoViagem(String motivoViagem) {
		this.motivoViagem = motivoViagem;
	}

	public boolean isAberta() {
		return aberta;
	}

	public void setAberta(boolean aberta) {
		this.aberta = aberta;
	}

	public BigDecimal getAdiantamento() {
		return adiantamento;
	}

	public void setAdiantamento(BigDecimal adiantamento) {
		this.adiantamento = adiantamento;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	public BigDecimal getTotalDespesas() {
		return totalDespesas;
	}

	public void setTotalDespesas(BigDecimal totalDespesas) {
		this.totalDespesas = totalDespesas;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public List<Despesa> getDespesas() {
		return despesas;
	}

	public void setDespesas(List<Despesa> despesas) {
		this.despesas = despesas;
	}

	@Override
	public String toString() {
		return "Viagem [id=" + id + ", idViagem=" + idViagem
				+ ", dataInicioViagem=" + dataInicioViagem + ", dataFimViagem="
				+ dataFimViagem + ", aberta=" + aberta + ", motivoViagem="
				+ motivoViagem + ", adiantamento=" + adiantamento
				+ ", dataHora=" + dataHora + ", totalDespesas=" + totalDespesas
				+ ", saldo=" + saldo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Viagem other = (Viagem) obj;
		if(this.id != other.id){
			Log.i("INFO", "OK");
			return false;
		}
		Log.i("INFO", "OK2");
		return true;
	}

}