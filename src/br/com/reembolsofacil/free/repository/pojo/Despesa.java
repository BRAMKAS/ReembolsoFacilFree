package br.com.reembolsofacil.free.repository.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Despesa implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String ID = "_id", ID_DESPESA = "id_despesa",
			FK_VIAGEM = "fk_viagem", DATA_DESPESA = "data_despesa",
			TIPO_DESPESA = "tipo_despesa", VALOR_DESPESA = "valor_despesa",
			DESCRICAO_DESPESA = "descricao_despesa", DATA_HORA = "data_hora";

	public static final String[] COLUMNS = new String[] { ID, ID_DESPESA,
			FK_VIAGEM, DATA_DESPESA, DESCRICAO_DESPESA, TIPO_DESPESA,
			VALOR_DESPESA, DATA_HORA };

	private long id;// id local (android)

	private Long idDespesa;// id no banco remoto

	private Viagem viagem;

	private Date dataDespesa;

	private String tipoDespesa;

	private BigDecimal valorDespesa;

	private String descricaoDespesa;

	private Date dataHora;

	public Despesa() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getIdDespesa() {
		return idDespesa;
	}

	public void setIdDespesa(Long idDespesa) {
		this.idDespesa = idDespesa;
	}

	public Viagem getViagem() {
		return viagem;
	}

	public void setViagem(Viagem viagem) {
		this.viagem = viagem;
	}

	public Date getDataDespesa() {
		return dataDespesa;
	}

	public void setDataDespesa(Date dataDespesa) {
		this.dataDespesa = dataDespesa;
	}

	public String getTipoDespesa() {
		return tipoDespesa;
	}

	public void setTipoDespesa(String tipoDespesa) {
		this.tipoDespesa = tipoDespesa;
	}

	public BigDecimal getValorDespesa() {
		return valorDespesa;
	}

	public void setValorDespesa(BigDecimal valorDespesa) {
		this.valorDespesa = valorDespesa;
	}

	public String getDescricaoDespesa() {
		return descricaoDespesa;
	}

	public void setDescricaoDespesa(String descricaoDespesa) {
		this.descricaoDespesa = descricaoDespesa;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}

	@Override
	public String toString() {
		return "Despesa [id=" + id + ", idDespesa=" + idDespesa
				+ ", dataDespesa=" + dataDespesa + ", tipoDespesa="
				+ tipoDespesa + ", valorDespesa=" + valorDespesa
				+ ", descricaoDespesa=" + descricaoDespesa + ", dataHora="
				+ dataHora + "]";
	}

}