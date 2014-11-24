package br.com.reembolsofacil.free;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import br.com.reembolsofacil.free.adapter.ListViagensArrayAdapter;
import br.com.reembolsofacil.free.repository.pojo.Despesa;
import br.com.reembolsofacil.free.repository.pojo.Viagem;

public class ViagensActivity extends BaseActivity {
	
	public static final int MENU_OPT_ADD = Menu.FIRST + 1;
	
	private ListView listViewViagens;
	private Handler handler;//atualiza a view por outras threads
	private ProgressDialog prgsDialog;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setContentView(R.layout.view_viagens);
		
		listViewViagens = (ListView) findViewById(R.id.listViewViagens);
		handler = new Handler();
		
		listViewViagens.setOnItemClickListener(new OnItemClickListener() {
			Viagem v;
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				v = (Viagem)listViewViagens.getAdapter().getItem(position);
				Intent i1 = new Intent(ViagensActivity.this,ViagemActivity.class);
				i1.putExtra("viagem", v);
				startActivityForResult(i1, VIEW_VIAGEM);
			}
		});
		
		Intent i = getIntent();
		if(i != null){
			prgsDialog = ProgressDialog.show(this, getResourceString(R.string.carregando), getResourceString(R.string.carregandoViagens), true, true);

			new Thread(new GetViagensRunnable()).start();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_OPT_ADD, 0, R.string.reemb_adicionar).setIcon(android.R.drawable.ic_menu_add);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case MENU_OPT_ADD:
				Intent i1 = new Intent(this,ViagemActivity.class);
				startActivityForResult(i1, VIEW_VIAGEM);
				return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case VIEW_VIAGEM:
			if(resultCode == RESULT_OK){
				prgsDialog = ProgressDialog.show(this, getResourceString(R.string.carregando), getResourceString(R.string.carregandoViagens), true, true);

				new Thread(new GetViagensRunnable()).start();
				
			}else if(resultCode == RESULT_CANCELED){
				
			}
			break;
			
		default:
			break;
		}
	}

	private class GetViagensRunnable implements Runnable{
		
		List<Viagem> viagensList = null;
		public void run() {
			
			 viagensList = ReembolsoFacilFree.repositorio.listarViagens();
			 //calcula o total de despesas e saldo
			 for(Viagem v:viagensList){
				 BigDecimal totalDespesas = new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
				 List<Despesa> ds= ReembolsoFacilFree.repositorio.listarDespesasDeViagem(v);
				 v.setDespesas(ds);
				 for(Despesa d:ds)
					 totalDespesas=totalDespesas.add(d.getValorDespesa());
				 v.setTotalDespesas(totalDespesas);
				 v.setSaldo(v.getAdiantamento().subtract(totalDespesas));
			 }
			
			handler.post(new Runnable() {
				public void run() {
					
					if(viagensList.size()==0)
						aviso(getResourceString(R.string.naoHaViagem));
					listViewViagens.setAdapter(new ListViagensArrayAdapter(ViagensActivity.this,GetViagensRunnable.this.viagensList));
				}
			});
			//caso botao voltar for pressionado, indica a activity principal recarregar as viagens abertas
			setResult(RESULT_OK);
			prgsDialog.dismiss();
		}
		
	}
	
}
