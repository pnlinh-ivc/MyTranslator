package com.ndanh.mytranslator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ndanh.mytranslator.R;
import com.ndanh.mytranslator.model.History;
import com.ndanh.mytranslator.screen.history.DeleteMode;
import com.ndanh.mytranslator.screen.history.HistoryActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;


/**
 * Created by ndanh on 5/10/2017.
 */

public final class HistoryRecyclerViewApdater  extends RecyclerView.Adapter<HistoryRecyclerViewApdater.ViewHolder> implements HistoryActivity.DeleteProcessListener , Observer {

    private List<HistoryView> historyList = new ArrayList<HistoryView> (  );
    private Context context;
    private static final int NORMAL_MODE = 1;
    private static final int DELETE_MODE_CHECKED = 2;
    private static final int DELETE_MODE_NON_CHECK = 3;
    private int count = 0;
    private LayoutInflater inflater;

    public HistoryRecyclerViewApdater(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        DeleteMode.getInstance ().addObserver ( HistoryRecyclerViewApdater.this );
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView ;
        switch (viewType){
            case NORMAL_MODE:
                itemView = inflater.inflate(R.layout.item_history_normal, parent, false) ;
                break;
            case DELETE_MODE_CHECKED:
                itemView = inflater.inflate(R.layout.item_history_checked, parent, false) ;
                break;
            case DELETE_MODE_NON_CHECK:
                itemView = inflater.inflate(R.layout.item_history_non_check, parent, false) ;
                break;
            default:
                itemView = null;
        }
        return new ViewHolder ( itemView );
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final HistoryView hs = historyList.get(position);
        holder.Lang_Scr.setText(hs.history.getLangsrc() );
        holder.textSrc.setText(hs.history.getTextsrc());
        holder.Lang_Des.setText( hs.history.getLangdes() );
        holder.Text_Des.setText(hs.history.getTextdes());
        holder.TimeStamp.setText(getDateCurrentTimeZone(hs.history.getTimestamp()));
        switch (holder.getItemViewType ()){
            case NORMAL_MODE:
                holder.panelTouch.setOnLongClickListener ( new View.OnLongClickListener () {
                    @Override
                    public boolean onLongClick(View v) {
                        historyList.get ( holder.getAdapterPosition () ).isDeleted = true;
                        DeleteMode.getInstance ().on ();
                        return true;
                    }
                } );
                break;
            case DELETE_MODE_CHECKED:
            holder.panelTouch.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition ();
                        historyList.get(pos).isDeleted = false;
                        count--;
                        if (count == 0){
                            DeleteMode.getInstance ().off ();
                            return;
                        }
                        notifyItemChanged ( pos );
                    }
                } );
            break;
            case DELETE_MODE_NON_CHECK:
            holder.panelTouch.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getAdapterPosition ();
                        historyList.get(pos).isDeleted = true;
                        count++;
                        notifyItemChanged ( pos );
                    }
                } );
                break;
            default:
                    break;
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size ();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(DeleteMode.getInstance ().isDeleteMode ()){
            count = 1;
            notifyDataSetChanged();
        } else {
            count = 0;
            Iterator<HistoryView> iter = historyList.iterator ();
            while (iter.hasNext ()){
                iter.next ().isDeleted = false;
            }
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView Lang_Scr;
        protected TextView textSrc;
        protected TextView Lang_Des;
        protected TextView Text_Des;
        protected TextView TimeStamp;
        protected RelativeLayout panelTouch;

        protected ViewHolder(View itemView) {
            super(itemView);
            Lang_Scr = (TextView) itemView.findViewById( R.id.lang_scr);
            textSrc = (TextView) itemView.findViewById(R.id.text_src);
            Lang_Des = (TextView) itemView.findViewById(R.id.lang_des);
            Text_Des = (TextView) itemView.findViewById(R.id.text_des);
            TimeStamp = (TextView) itemView.findViewById(R.id.time_stamp);
            panelTouch = (RelativeLayout) itemView.findViewById ( R.id.touch_panel );
        }

    }

    public static class HistoryView{
        public boolean isDeleted;
        public History history;

        public HistoryView(History history) {
            this.history = history;
            this.isDeleted = false;
        }
    }

    public  String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add( Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date currenTimeZone =  calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
    }

    @Override
    public int getItemViewType(int position) {
        if (!DeleteMode.getInstance ().isDeleteMode ())
            return NORMAL_MODE;
        return historyList.get ( position ).isDeleted ? DELETE_MODE_CHECKED : DELETE_MODE_NON_CHECK;
    }

    @Override
    public void onSelectAll() {
        if(count == historyList.size ()){
            DeleteMode.getInstance ().off ();
            return;
        }
        Iterator<HistoryView> iter = historyList.iterator ();
        while (iter.hasNext ()){
            iter.next ().isDeleted = true;
        }
        count = historyList.size ();
        notifyDataSetChanged ();
    }

    @Override
    public long[] beforeDelete() {
        List<Long> lst = new ArrayList<Long> (  );
        for(HistoryView itemDelete : historyList){
            if(itemDelete.isDeleted){
                lst.add ( itemDelete.history.getTimestamp () );
            }
        }
        long[] arr = new long[lst.size()];
        for(int i = 0; i < lst.size(); i++) {
            arr[i] = lst.get(i);
        }
        return arr ;
    }

    public void addItems(List<HistoryView> list){
        historyList.clear ();
        historyList = list;
        notifyDataSetChanged ();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


}
