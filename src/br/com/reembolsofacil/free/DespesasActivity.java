package br.com.reembolsofacil.free;

import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import br.com.reembolsofacil.free.adapter.DespesasArrayAdapter;
import br.com.reembolsofacil.free.repository.pojo.Despesa;
import br.com.reembolsofacil.free.repository.pojo.Viagem;
import br.com.reembolsofacil.free.util.UtlDate;

public class DespesasActivity extends BaseActivity {

	public static final int MENU_OPT_ADD = Menu.FIRST + 1;

	private ListView listViewDespesas;
	private Viagem viagem;
	private ProgressDialog prgsDialog;
	private Handler handler;//atualiza a view por outras threads
	private AlertDialog.Builder alertDialog=null;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.view_despesas);

		listViewDespesas = (ListView) findViewById(R.id.listViewDespesas);
		handler = new Handler();

		listViewDespesas.setOnItemClickListener(new OnItemClickListener() {
			Despesa d=null;
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				d = (Despesa)listViewDespesas.getAdapter().getItem(position);

				StringBuilder message = new StringBuilder();
				message.append(getResourceString(R.string.despact_data));
				message.append(": ");
				message.append(UtlDate.getStringFromDate(d.getDataDespesa(), getResourceString(R.string.reemb_dateformat)));
				message.append("\n");
				message.append(getResourceString(R.string.despact_tipo));
				message.append(": ");
				message.append(d.getTipoDespesa());
				message.append("\n");
				message.append(getResourceString(R.string.despact_valor));
				message.append(": ");
				message.append(d.getValorDespesa().toPlainString());
				message.append("\n");
				message.append(getResourceString(R.string.despact_descricao));
				message.append(": ");
				message.append(d.getDescricaoDespesa());

				alertDialog = new AlertDialog.Builder(DespesasActivity.this).setTitle(R.string.despact_despesa)
				.setMessage(message.toString())
				.setPositiveButton(R.string.despact_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {
					}
				}).setNeutralButton(R.string.despact_editar, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {
						if(!viagem.isAberta()){
							avisoLong(R.string.despact_para_editar);
							alertDialog.show();
						}
						else{
							Intent i1 = new Intent(DespesasActivity.this,ReembolsoFacilFree.class);
							i1.putExtra("despesa", d);
							i1.putExtra("viagem", viagem);
							i1.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
							startActivity(i1);
						}
					}
				});
				alertDialog.show();
			}
		});

		Intent i = getIntent();
		if(i != null){
			viagem = (Viagem)i.getSerializableExtra("viagem");

			prgsDialog = ProgressDialog.show(
					this, getResourceString(R.string.despact_carregando), 
					getResourceString(R.string.despact_buscando),true,true);

			new Thread(new GetDespesasRunnable()).start();

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(viagem.isAberta())
			menu.add(0, MENU_OPT_ADD, 0, R.string.reemb_adicionar).setIcon(android.R.drawable.ic_menu_add);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_OPT_ADD:
			Intent i1 = new Intent(DespesasActivity.this,ReembolsoFacilFree.class);
			i1.putExtra("viagem", viagem);
			i1.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
			startActivity(i1);
			return true;
		}
		return false;
	}

	private class GetDespesasRunnable implements Runnable{

		List<Despesa> despesasList = null;

		public void run() {

			despesasList = ReembolsoFacilFree.repositorio.listarDespesasDeViagem(viagem);

			handler.post(new Runnable() {
				public void run() {
					if(despesasList.size() == 0) {
						aviso(R.string.despact_nao_ha_despesas);
					}
					listViewDespesas.setAdapter(new DespesasArrayAdapter(DespesasActivity.this, despesasList));
				}
			});
			prgsDialog.dismiss();
		}

	}
}
