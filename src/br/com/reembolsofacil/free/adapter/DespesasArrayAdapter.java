package br.com.reembolsofacil.free.adapter;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import br.com.reembolsofacil.free.repository.pojo.Despesa;
import br.com.reembolsofacil.free.util.UtlDate;
import br.com.reembolsofacil.free.util.UtlString;

public class DespesasArrayAdapter extends ArrayAdapter<Despesa> {
	Activity context;
	List<Despesa> items;
	private LayoutInflater mInflater;

	public DespesasArrayAdapter(final Activity context,List<Despesa> result){
		
        super(context, android.R.layout.simple_list_item_1, result);
        this.context = context;
        this.items = result;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getView(final int position, final View convertView,
	        final ViewGroup parent) {
		
	        final View view = this.context.getLayoutInflater().inflate(
	        		android.R.layout.simple_list_item_1, null);
	        final Despesa item = this.items.get(position);
	        
	        ((TextView) view.findViewById(android.R.id.text1)).setText(
	        		UtlDate.getStringFromDate(item.getDataDespesa(), "dd/MM/yyyy")+" "+item.getTipoDespesa()
	        		+" "+UtlString.getCurrency(item.getValorDespesa(), new Locale("pt", "BR")));
	        
	        return view;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		
		return createViewFromResource(position, convertView, parent, android.R.layout.simple_list_item_1);

	}
	
    private View createViewFromResource(int position, View convertView, ViewGroup parent,
            int resource) {
        View view;
        CheckedTextView cText;

        if (convertView == null) {
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        Despesa item = getItem(position);
        cText = (CheckedTextView) view.findViewById(android.R.id.text1);
        cText.setText(item.getDataDespesa()+ " tipo: " + item.getTipoDespesa() + " valor: " + item.getValorDespesa());

        return view;
    }

	
}
