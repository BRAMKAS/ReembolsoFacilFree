package br.com.reembolsofacil.free.repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.reembolsofacil.free.repository.pojo.Despesa;
import br.com.reembolsofacil.free.repository.pojo.Usuario;
import br.com.reembolsofacil.free.repository.pojo.Viagem;

public class RepositorioScript {
	
	public static final String ASC = " ASC";
	public static final String DESC = " DESC";

	// Script para fazer drop na tabela
	private static final String SCRIPT__DELETE[] = new String[]{"DROP TABLE IF EXISTS usuario",
		"DROP TABLE IF EXISTS viagem","DROP TABLE IF EXISTS despesa"};

	// Cria a tabelas
	private static final String[] SCRIPT_CREATE = new String[] {
			"create table usuario (_id integer primary key autoincrement, id_usuario integer unique, " +
			"email_usuario text, tipo_usuario text, login text, senha text, data_hora integer);",
			"create table viagem(_id integer primary key autoincrement, id_viagem integer unique, fk_usuario integer, " +
			"data_inicio_viagem integer, data_fim_viagem integer, motivo_viagem text, aberta integer, " +
			"adiantamento real, data_hora integer,foreign key(fk_usuario) references usuario(_id));",
			"create table despesa (_id integer primary key autoincrement, id_despesa integer unique, fk_viagem integer, " +
			"data_despesa integer, descricao_despesa text, tipo_despesa text, valor_despesa real,data_hora integer, " +
			"foreign key(fk_viagem) references viagem(_id));"};

	// Nome do banco
	private static final String DB_NAME = "reembolsoFacilDB";

	// Controle de versão
	private static final int DB_VERSION = 1;

	// Nome da tabela
	public static final String TABLE_USUARIO = "usuario";
	public static final String TABLE_VIAGEM  = "viagem";
	public static final String TABLE_DESPESA = "despesa";

	// Classe utilitária para abrir, criar, e atualizar o banco de dados
	private SQLiteHelper dbHelper;
	
	protected SQLiteDatabase db;

	// Cria o banco de dados com um script SQL
	public RepositorioScript(Context ctx) {
		// Criar utilizando um script SQL
		dbHelper = new SQLiteHelper(ctx, RepositorioScript.DB_NAME, RepositorioScript.DB_VERSION,
				RepositorioScript.SCRIPT_CREATE, RepositorioScript.SCRIPT__DELETE);

		// abre o banco no modo escrita para poder alterar também
		db = dbHelper.getWritableDatabase();
	}
	
	// Retorna um cursor com todas as viagens
	public Cursor getViagensCursor() {
		try {
			// select * from viagem
			return db.query(TABLE_VIAGEM, Viagem.COLUMNS, null, null, null, null, Viagem.DATA_INICIO_VIAGEM+ASC, null);
		} catch (SQLException e) {
			Log.e("TODO", "Erro ao buscar os carros: " + e.toString());
			return null;
		}
	}

	// Retorna uma lista com todas viagens
	public List<Viagem> listarViagens() {
		Cursor c = getViagensCursor();
		return loadViagemFromCursor(c);
	}
	
	public List<Viagem> listarViagensAbertas() {
		
		Cursor c = db.query(TABLE_VIAGEM, Viagem.COLUMNS, Viagem.ABERTA + ">0", null, null, null, Viagem.DATA_INICIO_VIAGEM+ASC);
		return loadViagemFromCursor(c);
	}
	
	private List<Viagem> loadViagemFromCursor(Cursor c){
		List<Viagem> viagens = new ArrayList<Viagem>();

		if (c.moveToFirst()) {

			// Recupera os índices das colunas
			int idxId = c.getColumnIndex(Viagem.ID);
			int idxIdViagem = c.getColumnIndex(Viagem.ID_VIAGEM);
			int idxFkUsuario = c.getColumnIndex(Viagem.FK_USUARIO);
			int idxDtInicio = c.getColumnIndex(Viagem.DATA_INICIO_VIAGEM);
			int idxDtFim = c.getColumnIndex(Viagem.DATA_FIM_VIAGEM);
			int idxMotivo = c.getColumnIndex(Viagem.MOTIVO_VIAGEM);
			int idxAberta = c.getColumnIndex(Viagem.ABERTA);
			int idxAdiantamento = c.getColumnIndex(Viagem.ADIANTAMENTO);
			int idxDtHora = c.getColumnIndex(Viagem.DATA_HORA);
			
			do {
				Viagem viagem = new Viagem();
				viagens.add(viagem);

				viagem.setId(c.getLong(idxId));
				viagem.setIdViagem(c.getString(idxIdViagem)==null?null:c.getLong(idxIdViagem));
				viagem.setUsuario(new Usuario(c.getLong(idxFkUsuario)));
				viagem.setDataInicioViagem(new Date(c.getLong(idxDtInicio)));
				viagem.setDataFimViagem(new Date(c.getLong(idxDtFim)));
				viagem.setMotivoViagem(c.getString(idxMotivo));
				viagem.setAberta(c.getInt(idxAberta)==0?false:true);
				viagem.setAdiantamento(new BigDecimal(c.getDouble(idxAdiantamento)));
				viagem.setAdiantamento(viagem.getAdiantamento().setScale(2, RoundingMode.HALF_UP));
				viagem.setDataHora(new Date(c.getLong(idxDtHora)));
			} while (c.moveToNext());
		}

		return viagens;
	}
	
	private List<Despesa> loadDespesaFromCursor(Cursor c){
		List<Despesa> despesas = new ArrayList<Despesa>();

		if (c.moveToFirst()) {

			// Recupera os índices das colunas
			int idxId = c.getColumnIndex(Despesa.ID);
			int idxIdDespesa = c.getColumnIndex(Despesa.ID_DESPESA);
			int idxFkViagem = c.getColumnIndex(Despesa.FK_VIAGEM);
			int idxDtDespesa = c.getColumnIndex(Despesa.DATA_DESPESA);
			int idxTipo = c.getColumnIndex(Despesa.TIPO_DESPESA);
			int idxValor = c.getColumnIndex(Despesa.VALOR_DESPESA);
			int idxDescricao = c.getColumnIndex(Despesa.DESCRICAO_DESPESA);
			int idxDtHora = c.getColumnIndex(Despesa.DATA_HORA);
			
			do {
				Despesa despesa = new Despesa();
				despesas.add(despesa);

				despesa.setId(c.getLong(idxId));
				despesa.setIdDespesa(c.getString(idxIdDespesa)==null?null:c.getLong(idxIdDespesa));
				despesa.setViagem(new Viagem(c.getLong(idxFkViagem)));
				despesa.setDataDespesa(new Date(c.getLong(idxDtDespesa)));
				despesa.setTipoDespesa(c.getString(idxTipo));
				despesa.setValorDespesa(new BigDecimal(c.getDouble(idxValor)));
				despesa.setValorDespesa(despesa.getValorDespesa().setScale(2, RoundingMode.HALF_UP));
				despesa.setDescricaoDespesa(c.getString(idxDescricao));
				despesa.setDataHora(new Date(c.getLong(idxDtHora)));
			} while (c.moveToNext());
		}

		return despesas;
	}
	
	public List<Despesa> listarDespesasDeViagem(Viagem viagem) {
		
		Cursor c = db.query(TABLE_DESPESA, Despesa.COLUMNS, Despesa.FK_VIAGEM + "=?", 
				new String[]{Long.toString(viagem.getId())}, null, null, Despesa.DATA_DESPESA+ASC);
		return loadDespesaFromCursor(c);
	}
	
	public long salvarViagem(Viagem viagem) {
		long id = viagem.getId();

		if (id != 0) {
			atualizarViagem(viagem);
		} else {
			id = inserirViagem(viagem);
		}

		return id;
	}
	
	public long salvarDespesa(Despesa despesa) {
		long id = despesa.getId();

		if (id != 0) {
			atualizarDespesa(despesa);
		} else {
			id = inserirDespesa(despesa);
		}

		return id;
	}
	
	public long inserirDespesa(Despesa despesa) {
		ContentValues values = new ContentValues();
		//values.put(Viagem.ID, viagem.getId());
		values.put(Despesa.ID_DESPESA, despesa.getIdDespesa());
		if(despesa.getViagem()!=null)
			values.put(Despesa.FK_VIAGEM, despesa.getViagem().getId());
		values.put(Despesa.DATA_DESPESA, despesa.getDataDespesa().getTime());
		values.put(Despesa.TIPO_DESPESA, despesa.getTipoDespesa());
		values.put(Despesa.VALOR_DESPESA, despesa.getValorDespesa().doubleValue());
		values.put(Despesa.DESCRICAO_DESPESA, despesa.getDescricaoDespesa());
		if(despesa.getDataHora()==null)
			despesa.setDataHora(new Date());
		values.put(Viagem.DATA_HORA, despesa.getDataHora().getTime());

		long id = inserir(TABLE_DESPESA, values);
		return id;
	}
	
	public long inserirViagem(Viagem viagem) {
		ContentValues values = new ContentValues();
		//values.put(Viagem.ID, viagem.getId());
		values.put(Viagem.ID_VIAGEM, viagem.getIdViagem());
		if(viagem.getUsuario()!=null)
			values.put(Viagem.FK_USUARIO, viagem.getUsuario().getId());
		values.put(Viagem.DATA_INICIO_VIAGEM, viagem.getDataInicioViagem().getTime());
		values.put(Viagem.DATA_FIM_VIAGEM, viagem.getDataFimViagem().getTime());
		values.put(Viagem.MOTIVO_VIAGEM, viagem.getMotivoViagem());
		values.put(Viagem.ABERTA, viagem.isAberta());
		values.put(Viagem.ADIANTAMENTO, viagem.getAdiantamento().doubleValue());
		if(viagem.getDataHora()==null)
			viagem.setDataHora(new Date());
		values.put(Viagem.DATA_HORA, viagem.getDataHora().getTime());

		long id = inserir(TABLE_VIAGEM, values);
		return id;
	}

	public long inserir(String table,ContentValues valores) {
		long id = db.insert(table, "", valores);
		return id;
	}

	public int atualizarViagem(Viagem viagem) {
		ContentValues values = new ContentValues();
		values.put(Viagem.ID, viagem.getId());
		values.put(Viagem.ID_VIAGEM, viagem.getIdViagem());
		values.put(Viagem.FK_USUARIO, viagem.getUsuario().getId());
		values.put(Viagem.DATA_INICIO_VIAGEM, viagem.getDataInicioViagem().getTime());
		values.put(Viagem.DATA_FIM_VIAGEM, viagem.getDataFimViagem().getTime());
		values.put(Viagem.MOTIVO_VIAGEM, viagem.getMotivoViagem());
		values.put(Viagem.ABERTA, viagem.isAberta());
		values.put(Viagem.ADIANTAMENTO, viagem.getAdiantamento().doubleValue());
		values.put(Viagem.DATA_HORA, viagem.getDataHora().getTime());

		String _id = String.valueOf(viagem.getId());
		String where = Viagem.ID + "=?";
		String[] whereArgs = new String[] { _id };

		int count = atualizar(TABLE_VIAGEM, values, where, whereArgs);

		return count;
	}
	
	public int atualizarDespesa(Despesa despesa) {
		ContentValues values = new ContentValues();
		values.put(Despesa.ID, despesa.getId());
		values.put(Despesa.ID_DESPESA, despesa.getIdDespesa());
		if(despesa.getViagem()!=null)
			values.put(Despesa.FK_VIAGEM, despesa.getViagem().getId());
		values.put(Despesa.DATA_DESPESA, despesa.getDataDespesa().getTime());
		values.put(Despesa.TIPO_DESPESA, despesa.getTipoDespesa());
		values.put(Despesa.VALOR_DESPESA, despesa.getValorDespesa().doubleValue());
		values.put(Despesa.DESCRICAO_DESPESA, despesa.getDescricaoDespesa());
		if(despesa.getDataHora()==null)
			despesa.setDataHora(new Date());
		values.put(Viagem.DATA_HORA, despesa.getDataHora().getTime());

		String _id = String.valueOf(despesa.getId());
		String where = Despesa.ID + "=?";
		String[] whereArgs = new String[] { _id };

		int count = atualizar(TABLE_DESPESA, values, where, whereArgs);

		return count;
	}

	public int atualizar(String table, ContentValues valores, String where, String[] whereArgs) {
		int count = db.update(table, valores, where, whereArgs);
		return count;
	}
	
	public int deletarViagem(long id) {
		String where = Viagem.ID + "=?";

		String _id = String.valueOf(id);
		String[] whereArgs = new String[] { _id };

		int count = deletar(TABLE_VIAGEM, where, whereArgs);

		return count;
	}
	
	public int deletarDespesa(long id) {
		String where = Viagem.ID + "=?";

		String _id = String.valueOf(id);
		String[] whereArgs = new String[] { _id };

		int count = deletar(TABLE_DESPESA, where, whereArgs);

		return count;
	}
	
	public int deletarDespesasDeViagem(long id) {
		String where = Despesa.FK_VIAGEM + "=?";

		String _id = String.valueOf(id);
		String[] whereArgs = new String[] { _id };

		int count = deletar(TABLE_DESPESA, where, whereArgs);

		return count;
	}

	// Deleta o carro com os argumentos fornecidos
	public int deletar(String table, String where, String[] whereArgs) {
		int count = db.delete(table, where, whereArgs);
		return count;
	}

	// Fecha o banco
	public void fechar() {
		try{
			if (db != null) 
				db.close();
			
			if (dbHelper != null) 
				dbHelper.close();
		}catch (RuntimeException e) {
			Log.e("A", e.getMessage());
		}
	}
}
