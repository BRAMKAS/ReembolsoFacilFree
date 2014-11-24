package br.com.reembolsofacil.free.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import br.com.reembolsofacil.free.R;
import br.com.reembolsofacil.free.repository.pojo.Viagem;
import br.com.reembolsofacil.free.util.UtlDate;

public class ListViagensArrayAdapter extends ArrayAdapter<Viagem> {
	Activity context;
	List<Viagem> items;
	private LayoutInflater mInflater;

	public ListViagensArrayAdapter(final Activity context,List<Viagem> result){
		
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
	        final Viagem item = this.items.get(position);
	        
	        ((TextView) view.findViewById(android.R.id.text1)).setText(item.getMotivoViagem());
	        
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

        Viagem item = getItem(position);
        cText = (CheckedTextView) view.findViewById(android.R.id.text1);
        
        StringBuilder sb = new StringBuilder("");
        sb.append("\n");
        sb.append(UtlDate.getStringFromDate(item.getDataInicioViagem(), this.context.getResources().getString(R.string.reemb_dateformat)));
        sb.append(" ");
        sb.append(this.context.getResources().getString(R.string.reemb_to));
        sb.append(" ");
        sb.append(UtlDate.getStringFromDate(item.getDataFimViagem(), this.context.getResources().getString(R.string.reemb_dateformat)));
        sb.append("\n");
        sb.append(item.getMotivoViagem());
        sb.append("\n");
        
        cText.setText(sb.toString());

        return view;
    }

	
}
