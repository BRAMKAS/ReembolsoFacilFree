package br.com.reembolsofacil.free;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import br.com.reembolsofacil.free.adapter.ViagensArrayAdapter;
import br.com.reembolsofacil.free.repository.RepositorioScript;
import br.com.reembolsofacil.free.repository.pojo.Despesa;
import br.com.reembolsofacil.free.repository.pojo.Viagem;
import br.com.reembolsofacil.free.util.NumberUtil;
import br.com.reembolsofacil.free.util.UtlDate;

public class ReembolsoFacilFree extends BaseActivity {

	private List<String> tipos = new ArrayList<String>();
	static final int DIALOG_DATE = 0;

	private static final int MENU_OPT_INFO = Menu.FIRST;
	private static final int MENU_OPT_SAIR = Menu.FIRST + 1;
	private static final int MENU_OPT_VIAGENS = Menu.FIRST + 2;

	private boolean restoreInsertView;//informa qdo deve restaurar a view do estado upadate para insert

	private Button   btnDtDespesa;
	private Button   btnExcluirDespesa;
	private Button   btnCancelar;
	private EditText editTextDescricao;
	private EditText editTextValor;
	private Spinner  spinViagens;
	private Spinner  spinTipos;
	private ProgressDialog prgsDialog;
	ImageView img;

	private DatePickerDialog.OnDateSetListener mDateSetListener;
	private Handler handler;//atualiza a view por outras threads

	private Despesa despesa;
	private Viagem viagem;
	ArrayList<Viagem> viagensList = new ArrayList<Viagem>();

	public static RepositorioScript repositorio;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_main);
		
		carregaTipoDespesa();
		
		repositorio = new RepositorioScript(this);

		btnDtDespesa 	 	 = (Button)   findViewById(R.id.btnDtDespesa);
		btnExcluirDespesa 	 = (Button)   findViewById(R.id.btnExcluirDespesa);
		btnCancelar 	 	 = (Button)   findViewById(R.id.btnCancelar);
		editTextDescricao 	 = (EditText) findViewById(R.id.editTextDescricao);
		editTextValor 	 	 = (EditText) findViewById(R.id.editTextValor);
		spinViagens 	 	 = (Spinner)  findViewById(R.id.spinViagens);
		spinTipos 	 		 = (Spinner)  findViewById(R.id.spinTipos);
		img                  = (ImageView) findViewById(R.id.novoTipo);

		despesa = new Despesa();
		handler = new Handler();
		
		img.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	Toast.makeText(getApplicationContext(), "ADICIONAR NOVO TIPO DE DESPESA", Toast.LENGTH_SHORT).show();
		    }
		});

		setSpinnerTiposGastos();
		setDataDespesa();
		loadViagens();
	}

	
	
	@Override
	protected void onResume() {
		carregaTipoDespesa();
		super.onResume();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		repositorio.fechar();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DATE:
			final Calendar c = Calendar.getInstance();
			return new DatePickerDialog(this, mDateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_OPT_VIAGENS, 0, R.string.reemb_viagens).setIcon(android.R.drawable.ic_menu_agenda);
		menu.add(0, MENU_OPT_INFO, 1, R.string.reemb_sobre).setIcon(android.R.drawable.ic_menu_info_details);
		menu.add(0, MENU_OPT_SAIR, 2, R.string.reemb_sair).setIcon(android.R.drawable.ic_lock_power_off);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case MENU_OPT_VIAGENS:
			Intent i = new Intent(this,ViagensActivity.class);
			startActivityForResult(i, VIEW_VIAGENS);
			return true;
		case MENU_OPT_INFO:
			final TextView message = new TextView(this);
			message.setText(R.string.about);
			message.setBackgroundColor(Color.WHITE);
			message.setTextColor(Color.BLACK);
			message.setPadding(5, 5, 5, 5);
			Linkify.addLinks(message, Linkify.EMAIL_ADDRESSES|Linkify.WEB_URLS);
			message.setMovementMethod(LinkMovementMethod.getInstance());
			new AlertDialog.Builder(this).setTitle(R.string.app_name).setView(message).show();
			return true;

		case MENU_OPT_SAIR:

			aviso(R.string.reemb_saindo);
			finish();
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {

		case VIEW_VIAGENS:
			if(resultCode == RESULT_OK)
				loadViagens();
			break;

		default:
			break;
		}
	}

	//qdo uma activity chama novamente esta, que é apenas colocada
	//no topo da pilha, sem ser recriada
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		loadViagens();
		if(intent.getSerializableExtra("despesa") != null) {
			despesa = (Despesa) intent.getSerializableExtra("despesa");
			btnCancelar.setEnabled(true);
			btnExcluirDespesa.setEnabled(true);
		}else{
			despesa.setDescricaoDespesa("");
			despesa.setId(0);
			despesa.setValorDespesa(null);
			//pode ocorrer de a tela ter ficado ja no estado de edicao, entao aqui deve ser resetado
			btnCancelar.setEnabled(false);
			btnExcluirDespesa.setEnabled(false);
		}
		viagem = (Viagem) intent.getSerializableExtra("viagem");
		setScreenWithDespesa();
	}

	private void loadViagens() {
		prgsDialog = ProgressDialog.show(ReembolsoFacilFree.this, getResourceString(R.string.reemb_sincronizando), getResourceString(R.string.reemb_carregando), true, true);
		new Thread(new GetViagensRunnable()).start();
	}

	/**
	 * Configura o spinner de tipos de gasto (cria e adiciona listener)
	 */
	private void setSpinnerTiposGastos() {

		ArrayAdapter<String> adapterTipoDespesas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos);

		adapterTipoDespesas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinTipos.setAdapter(adapterTipoDespesas);

		spinTipos.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
				despesa.setTipoDespesa(tipos.get(position));
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
	}

	private void setDataDespesa() {
		Calendar c = Calendar.getInstance();
		despesa.setDataDespesa(c.getTime());
		
		btnDtDespesa.setText(UtlDate.getStringFromDate(despesa.getDataDespesa(), getResourceString(R.string.reemb_dateformat)));

		btnDtDespesa.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_DATE);
			}
		});

		mDateSetListener = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar c1 = Calendar.getInstance();
				c1.set(Calendar.YEAR, year);
				c1.set(Calendar.MONTH, monthOfYear);
				c1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				despesa.setDataDespesa(c1.getTime());
				btnDtDespesa.setText(UtlDate.getStringFromDate(despesa.getDataDespesa(), getResourceString(R.string.reemb_dateformat)));
			}
		};
	}

	private void restoreInsertView() {
		if(restoreInsertView) {
			restoreInsertView = false;
			btnCancelar.setEnabled(false);
			btnExcluirDespesa.setEnabled(false);
			despesa.setId(0);
			editTextDescricao.getText().clear();
			editTextValor.getText().clear();
			loadViagens();
		}
	}

	private void setScreenWithDespesa() {
		editTextDescricao.setText(despesa.getDescricaoDespesa() == null ? "" : despesa.getDescricaoDespesa());
		editTextValor.setText(despesa.getValorDespesa() == null ? "" : NumberUtil.format(despesa.getValorDespesa()));
		btnDtDespesa.setText(UtlDate.getStringFromDate(despesa.getDataDespesa(), getResourceString(R.string.reemb_dateformat)));
		if (despesa.getTipoDespesa() != null) {
			int i = 0;
			for(String tipo : tipos) {
				if(despesa.getTipoDespesa().equalsIgnoreCase(tipo)) {
					spinTipos.setSelection(i);
					break;
				}
				i++;
			}
		}
		
		//ViagensArrayAdapter arrAdpt=(ViagensArrayAdapter) spinViagens.getAdapter();

		/*for(int i=0;i<viagensList.size();i++) {
			if(viagensList.get(i).getId()==viagem.getId()) {
				spinViagens.setSelection(i);
			}
		}*/
		//bugado
		/*for(Viagem v:viagensList) {
			if(v.getId()==viagem.getId()) {
				spinViagens.setSelection(arrAdpt.getPosition(v));
				break;
			}
		}*/
	}

	private class GetViagensRunnable implements Runnable{

		public void run() {
			viagensList = (ArrayList<Viagem>) repositorio.listarViagensAbertas();

			//atualiza spinner de viagens, tendo ou não sido recuperado
			//pois qdo é alterado o usuário e o novo não tem viagens, deve-se limpar o spinner
			handler.post(new Runnable() {	
				public void run() {
					ViagensArrayAdapter adapter = new ViagensArrayAdapter(ReembolsoFacilFree.this, viagensList);
					spinViagens.setAdapter(adapter);
					//atualiza aqui para ficar na mesma thread
					//pode causar problemas dificeis de detectar caso outra thread tente alterar
					//TODO melhorar organizacao do codigo
					if(viagem != null)
						for(int i=0;i<viagensList.size();i++) {
							if(viagensList.get(i).getId()==viagem.getId()) {
								spinViagens.setSelection(i);
							}
						}
				}
			});
			prgsDialog.dismiss();
		}

	}

	private class SendDespesaRunnable implements Runnable {

		public void run() {

			despesa.setDescricaoDespesa(editTextDescricao.getText().toString().trim());
			//remove virgulas, deixando apenas a última
			String valor = editTextValor.getText().toString();
			despesa.setValorDespesa(new BigDecimal(NumberUtil.parseNumber(valor)));
			despesa.setViagem(viagem);
			repositorio.salvarDespesa(despesa);
			if(despesa.getId()>0)
				restoreInsertView=true;

			handler.post(new Runnable() {
				public void run() {
					editTextDescricao.getText().clear();
					editTextValor.getText().clear();
					restoreInsertView();
					aviso(getResourceString(R.string.despesaSalva));
				}
			});

			prgsDialog.dismiss();
		}
	}

	private class DeleteDespesaRunnable implements Runnable {

		public void run() {

			handler.post(new Runnable() {
				public void run() {
					repositorio.deletarDespesa(despesa.getId());
					avisoLong(getResourceString(R.string.despesaExcluida));
					editTextDescricao.getText().clear();
					editTextValor.getText().clear();
					restoreInsertView();
				}
			});
			prgsDialog.dismiss();
		}
	}
	
	private void carregaTipoDespesa() {
		Collections.addAll(tipos, getResources().getStringArray(R.array.reemb_tipoDespesa));
	}
	
    public void onSendDespesaClick(View v) {
		if (spinViagens.getSelectedItem() == null) {
			new AlertDialog.Builder(ReembolsoFacilFree.this).setTitle(R.string.reemb_aviso).setMessage(
					R.string.reemb_nao_possui_viagens).setNeutralButton(R.string.reemb_ok2, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dlg, int sumthin) {
								}
							}).show();
			return;
		}
		if (editTextValor.getText().toString().equals("")) {
			aviso(R.string.reemb_preencha_valor);
		} else {
			viagem = ((Viagem)spinViagens.getSelectedItem());
			prgsDialog = ProgressDialog.show(
					ReembolsoFacilFree.this, getResourceString(R.string.reemb_enviando), 
					getResourceString(R.string.reemb_enviando_despesa),true,true);
			new Thread(new SendDespesaRunnable()).start();


		}
    }
    
    public void onCancelDespesaClick(View v) {
		restoreInsertView = true;
		restoreInsertView();
    }
    
    public void onDeleteDespesaClick(View v) {
		new AlertDialog.Builder(ReembolsoFacilFree.this).setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle(R.string.reemb_confirmacao)
		.setMessage(R.string.reemb_confirma).setNeutralButton(
				R.string.reemb_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {

						prgsDialog = ProgressDialog.show(
								ReembolsoFacilFree.this, getResourceString(R.string.reemb_excluindo), 
								getResourceString(R.string.reemb_excluindo_aguarde),true,true);
						restoreInsertView=true;
						new Thread(new DeleteDespesaRunnable()).start();
					}
				}).setNegativeButton(R.string.reemb_cancelar, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {}
				}).show();
    }
}