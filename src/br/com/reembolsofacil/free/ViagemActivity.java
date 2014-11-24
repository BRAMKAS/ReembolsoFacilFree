package br.com.reembolsofacil.free;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import br.com.reembolsofacil.free.repository.pojo.Despesa;
import br.com.reembolsofacil.free.repository.pojo.Viagem;
import br.com.reembolsofacil.free.util.NumberUtil;
import br.com.reembolsofacil.free.util.UtlDate;

import com.csvreader.CsvWriter;

public class ViagemActivity extends BaseActivity {
	
	private Button btnExcluirViagem;
	private Button btnCancelar;
	private Button btnDtInicio;
	private Button btnDtFim;
	private EditText editTextMotivo;
	private EditText editTextAdiantamento;
	private CheckBox chkBxAberta;
	private DatePickerDialog dtPickerInicio;
	private DatePickerDialog dtPickerFim;
	private EditText editTextTotalDespesa;
	private EditText editTextSaldo;
	private LinearLayout resumo;
	
	static final int DIALOG_DATE_INICIO  = 1;
	static final int DIALOG_DATA_FIM	 = 2;
	
	private Viagem viagem;
	
	protected ProgressDialog prgsDialog;

	private Handler handler;//atualiza a view por outras threads
			
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setContentView(R.layout.view_viagem);

		handler   = new Handler();

		btnExcluirViagem	= (Button) findViewById(R.id.btnExcluirViagem);
		btnCancelar			= (Button) findViewById(R.id.btnCancelar);
		btnDtInicio 		= (Button) findViewById(R.id.btnDtInicio);
		btnDtFim 			= (Button) findViewById(R.id.btnDtFim);
		editTextMotivo 		= (EditText) findViewById(R.id.editTextMotivo);
		editTextAdiantamento= (EditText) findViewById(R.id.editTextAdiantamento);
		chkBxAberta			= (CheckBox) findViewById(R.id.chkBxAberta);
		editTextTotalDespesa = (EditText) findViewById(R.id.editTextTotalDespesa);
		editTextSaldo        = (EditText) findViewById(R.id.editTextSaldo);
		resumo			     = (LinearLayout) findViewById(R.id.linResumo);
		
		setDtPickerInicioFim();
				
		Intent i = getIntent();
		if(i!=null){
			viagem = (Viagem)i.getSerializableExtra("viagem");
			if (viagem == null) {//nova viagem
				viagem = new Viagem();
				Calendar c = Calendar.getInstance();
				viagem.setDataInicioViagem(c.getTime());
				viagem.setDataFimViagem(c.getTime());
			} else {//editar viagem
				resumo.setVisibility(LinearLayout.VISIBLE);
				setBtnExcluirCancelarViagem();
				editTextMotivo.setText(viagem.getMotivoViagem());
				chkBxAberta.setChecked(viagem.isAberta());
				//btnDtInicio.setText(UtlDate.getStringFromDate(viagem.getDataInicioViagem(), getResourceString(R.string.reemb_dateformat)));
				//btnDtFim.setText(UtlDate.getStringFromDate(viagem.getDataFimViagem(), getResourceString(R.string.reemb_dateformat)));
				Calendar c = Calendar.getInstance();
				c.setTime(viagem.getDataInicioViagem());
				dtPickerInicio.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				c.setTime(viagem.getDataFimViagem());
				dtPickerFim.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				editTextAdiantamento.setText(NumberUtil.format(viagem.getAdiantamento()));
				editTextTotalDespesa.setText(NumberUtil.format(viagem.getTotalDespesas()));
				editTextSaldo.setText(NumberUtil.format(viagem.getSaldo()));
			}
			
		}
		setBtnDtInicioFim();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_DATE_INICIO:
				return dtPickerInicio;
				
			case DIALOG_DATA_FIM:
				return dtPickerFim;
		}
		return super.onCreateDialog(id);
	}
	
	private void setDtPickerInicioFim(){
		final Calendar c1 = Calendar.getInstance();
		dtPickerInicio = new DatePickerDialog(this, new OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar c1 = Calendar.getInstance();
				c1.set(Calendar.YEAR, year);
				c1.set(Calendar.MONTH, monthOfYear);
				c1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				viagem.setDataInicioViagem(c1.getTime());
				btnDtInicio.setText(UtlDate.getStringFromDate(viagem.getDataInicioViagem(), getResourceString(R.string.reemb_dateformat)));
				
			}
		}, c1.get(Calendar.YEAR), c1.get(Calendar.MONTH),
				c1.get(Calendar.DAY_OF_MONTH));
		
		final Calendar c2 = Calendar.getInstance();
		dtPickerFim = new DatePickerDialog(this, new OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar c1 = Calendar.getInstance();
				c1.set(Calendar.YEAR, year);
				c1.set(Calendar.MONTH, monthOfYear);
				c1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				viagem.setDataFimViagem(c1.getTime());
				btnDtFim.setText(UtlDate.getStringFromDate(viagem.getDataFimViagem(), getResourceString(R.string.reemb_dateformat)));
				
			}
		}, c2.get(Calendar.YEAR), c2.get(Calendar.MONTH),
				c2.get(Calendar.DAY_OF_MONTH));
	}

	private void setBtnDtInicioFim(){
		
		btnDtInicio.setText(UtlDate.getStringFromDate(viagem.getDataInicioViagem(), getResourceString(R.string.reemb_dateformat)));
		btnDtFim.setText(UtlDate.getStringFromDate(viagem.getDataFimViagem(), getResourceString(R.string.reemb_dateformat)));
		
		btnDtInicio.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_DATE_INICIO);
			}
		});
		
		btnDtFim.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_DATA_FIM);
			}
		});
	}
	
	private void setBtnExcluirCancelarViagem(){
		btnExcluirViagem.setEnabled(true);
		btnCancelar.setEnabled(true);
	}
	
	protected class SendViagemRunnable implements Runnable{
		
		public void run() {
			
			ReembolsoFacilFree.repositorio.salvarViagem(viagem);
			
			handler.post(new AvisoLongRunnable(ViagemActivity.this, getResourceString(R.string.viagemEnviada)));
			setResult(RESULT_OK);
			finish();

			prgsDialog.dismiss();
		}
	}
	
	protected class DeleteViagemRunnable implements Runnable{
		
		public void run() {
			ReembolsoFacilFree.repositorio.deletarDespesasDeViagem(viagem.getId());
			ReembolsoFacilFree.repositorio.deletarViagem(viagem.getId());
			handler.post(new AvisoLongRunnable(ViagemActivity.this, getResourceString(R.string.viagemExcluida)));
			setResult(RESULT_OK);
			finish();
			prgsDialog.dismiss();
		}
	}
	
    public void onExportButtonClick(View v) {
		String msg2 = getResourceString(R.string.exportAlert);
		
		new AlertDialog.Builder(ViagemActivity.this).setTitle("Exportar").setMessage(msg2)
				.setNeutralButton("Confirmar", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int sumthin) {
						
						prgsDialog = ProgressDialog.show(ViagemActivity.this, "Exportando", "Exportando suas despesas, aguarde...",true,true);

						new Thread(new ExportCvsRunnable(viagem)).start();
					}
				}).setNegativeButton(
					"Cancelar", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dlg, int sumthin) {
						}
					}).show();
    }
	
    public void onDespesasButtonClick(View v) {
		Intent i2 = new Intent(ViagemActivity.this, DespesasActivity.class);
		
		i2.putExtra("viagem", viagem);
		startActivity(i2);
    }
	
    public void onCancelViagemClick(View v) {
		setResult(RESULT_CANCELED);
		finish();
    }
	
    public void onDeleteViagemClick(View v) {
    	AlertDialog.Builder ad = new AlertDialog.Builder(ViagemActivity.this);
    	ad.setIcon(android.R.drawable.ic_dialog_alert);
		ad.setTitle(getResourceString(R.string.confirmacao));
		ad.setMessage(getResourceString(R.string.confirmaExcluirViagem));
		ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int sumthin) {
				prgsDialog = ProgressDialog.show(ViagemActivity.this, getResourceString(R.string.excluindo), getResourceString(R.string.excluindoViagem), true, true);
				new Thread(new DeleteViagemRunnable()).start();
			}
		});
		ad.setNegativeButton(getResourceString(R.string.cancelar), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dlg, int sumthin) {
			}
		});
		ad.show();
    }
	
    public void onSaveViagemClick(View v) {
		if(editTextMotivo.getText().length()==0) {
			aviso(getResourceString(R.string.informeTodosCampos));
		} else {
				
			prgsDialog = ProgressDialog.show(ViagemActivity.this, getResourceString(R.string.enviando), getResourceString(R.string.enviandoViagem), true, true);
			
			viagem.setAberta(chkBxAberta.isChecked());
			viagem.setMotivoViagem(editTextMotivo.getText().toString());
			
			if ((editTextAdiantamento.getText().length()>0)) {
				//viagem.setAdiantamento(new BigDecimal(valor.trim().replace(",", ".")));
				Double valor = NumberUtil.parseNumber(editTextAdiantamento.getText().toString());
				viagem.setAdiantamento(new BigDecimal(valor));
			} else {
				viagem.setAdiantamento(new BigDecimal(0));
			}
			new Thread(new SendViagemRunnable()).start();
			
		}
    }
    
	private class ExportCvsRunnable implements Runnable {
		
		Viagem eViagem = null;
		
		public ExportCvsRunnable(Viagem v){
			this.eViagem = v;
		}
		
		public void run() {
			
			boolean mExternalStorageAvailable = false;
			boolean mExternalStorageWriteable = false;
			String state = Environment.getExternalStorageState();

			if (Environment.MEDIA_MOUNTED.equals(state)) {
			    // We can read and write the media
			    mExternalStorageAvailable = mExternalStorageWriteable = true;
			} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			    // We can only read the media
			    mExternalStorageAvailable = true;
			    mExternalStorageWriteable = false;
			} else {
			    // Something else is wrong. It may be one of many other states, but all we need
			    //  to know is we can neither read nor write
			    mExternalStorageAvailable = mExternalStorageWriteable = false;
			}
			
			if (mExternalStorageAvailable && mExternalStorageWriteable){
				File directory = Environment.getExternalStorageDirectory();
				File downloadDirectory = new File(directory.getAbsolutePath() + "/ReembolsoFacil");
				FileOutputStream fos = null;
				try {
					if (!downloadDirectory.exists()) {
						Boolean ret = downloadDirectory.mkdirs();
						if (!ret) {
							handler.post(new AvisoLongRunnable(ViagemActivity.this, "O diretório nao foi criado. " + ret));
						} else {
							handler.post(new AvisoLongRunnable(ViagemActivity.this, "O diretório foi criado. " + ret));
						}
					}
					File csv = new File(downloadDirectory.getAbsolutePath() + "/" + eViagem.getMotivoViagem() + ".csv");
					handler.post(new AvisoLongRunnable(ViagemActivity.this, csv.getAbsolutePath()));
					
					csv.createNewFile();
					fos = new FileOutputStream(csv);
				
					CsvWriter csvWriter = new CsvWriter(fos, ';', Charset.forName("UTF-8"));
					
					csvWriter.write("data despesa");
					csvWriter.write("tipo");
					csvWriter.write("valor");
					csvWriter.write("descrição");
					csvWriter.endRecord();
					for(Despesa d:eViagem.getDespesas()) {
						if(d.getDescricaoDespesa() != null && d.getDescricaoDespesa().length() > 0) {
							csvWriter.writeRecord(new String[]{UtlDate.getStringFromDate(d.getDataDespesa(), "dd/MM/yyyy"), d.getTipoDespesa(), d.getValorDespesa().toPlainString(), d.getDescricaoDespesa()});
						} else {
							csvWriter.writeRecord(new String[]{UtlDate.getStringFromDate(d.getDataDespesa(), "dd/MM/yyyy"), d.getTipoDespesa(), d.getValorDespesa().toPlainString()});
						}
					}
					
					csvWriter.flush();
					csvWriter.close();
					fos.close();
				} catch (Exception e) {
					handler.post(new AvisoLongRunnable(ViagemActivity.this, "Não é possível exportar as despesas " + e.getMessage()));
				}
			}else{
				handler.post(new AvisoLongRunnable(ViagemActivity.this, 
						"Não é possível salvar no SD card, se estiver conectado ao computador, desconecte e tente novamente"));
			}
			
			handler.post(new AvisoLongRunnable(ViagemActivity.this, "Exportado com sucesso"));
			prgsDialog.dismiss();
		}
		
	}
    
}
