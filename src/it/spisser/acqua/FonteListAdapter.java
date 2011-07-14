package it.spisser.acqua;

import it.spisser.acqua.R;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FonteListAdapter extends BaseAdapter{

	private List<Fonte> listaFonte;
	private Context context;
	
	public FonteListAdapter(List<Fonte> fonteList, Context context){
		this.listaFonte=fonteList;
		this.context=context;
	}
	@Override
	public int getCount() {
		return listaFonte.size();	}
	@Override
	public Object getItem(int position) {
return listaFonte.get(position);
		
	}
	@Override
	public long getItemId(int position) {
		return listaFonte.get(position).getId();
		
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout itemLayout;
		Fonte fonte = listaFonte.get(position);
		
		itemLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.listitem, parent,false);
		TextView nomeFonte = (TextView) itemLayout.findViewById(R.id.nomefonte);
		nomeFonte.setText(fonte.getNomeCommerciale());
		Double distanza=fonte.getDistanza();
		TextView distanzaFonte= (TextView) itemLayout.findViewById(R.id.distanzafonte);
		distanzaFonte.setText(Math.round(fonte.getDistanza())+"km");
		if (distanza<50){
			nomeFonte.setTextColor(Color.GREEN);
			distanzaFonte.setTextColor(Color.GREEN);
		}

		else if ((distanza<80)){
			nomeFonte.setTextColor(Color.YELLOW);
			distanzaFonte.setTextColor(Color.YELLOW);
		}
		else if (distanza>=80){
			nomeFonte.setTextColor(Color.RED);
			distanzaFonte.setTextColor(Color.RED);
		}
		
		
		
		// TODO Auto-generated method stub
		return itemLayout;
	}
}
