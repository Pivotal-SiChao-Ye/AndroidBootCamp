package com.example.test;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<RowItem>{
	
	Context context;
	
	public CustomList(Context context, int index, List<RowItem> items){
		super(context, index, items);
		this.context = context;
	}
	
	private class ViewHolder{
		ImageView imageView;
		TextView txtTitle;
		TextView txtYear;
		TextView txtCreation;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder = null;
		RowItem rowItem = getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null){
			holder = new ViewHolder();
            if (rowItem.getsearch() == 0){
            	convertView = mInflater.inflate(R.layout.listview, null);
            	holder.txtYear = (TextView) convertView.findViewById(R.id.txt3);
            	holder.txtTitle = (TextView) convertView.findViewById(R.id.txt);
            	holder.txtCreation = (TextView) convertView.findViewById(R.id.txt2);
            	holder.imageView = (ImageView) convertView.findViewById(R.id.img);
            	 convertView.setTag(holder);
            }
            else {
            	convertView = mInflater.inflate(R.layout.listviewsearch, null);
            	holder.txtYear = (TextView) convertView.findViewById(R.id.txt5);
            	holder.txtTitle = (TextView) convertView.findViewById(R.id.txt3);
            	holder.txtCreation = (TextView) convertView.findViewById(R.id.txt4);
            	holder.imageView = (ImageView) convertView.findViewById(R.id.img2);
            	convertView.setTag(holder);
            }
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Calendar c = Calendar.getInstance();
		int minutes = c.get(Calendar.MINUTE);
		int hours = c.get(Calendar.HOUR_OF_DAY);
		int days = c.get(Calendar.DAY_OF_MONTH);
		int months = c.get(Calendar.MONTH);
		int years = c.get(Calendar.YEAR);
		System.out.println("flagg" + rowItem.getTitle());
		holder.txtTitle.setText(rowItem.getTitle());
		System.out.println("flagg" + rowItem.getYear());
		holder.txtYear.setText(rowItem.getYear());
		System.out.println("flag1");
		holder.txtCreation.setText("Creation time: " + years + "-" + (months + 1) + "-" + days + " " + hours + ":" + minutes);
		System.out.println("flag2");
		holder.imageView.setImageBitmap(rowItem.getImageURL());
		//holder.imageView.setImageResource(rowItem.getImageURL());
		System.out.println("flag3");
		return convertView;
	}

}

