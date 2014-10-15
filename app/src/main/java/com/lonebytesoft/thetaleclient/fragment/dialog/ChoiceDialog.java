package com.lonebytesoft.thetaleclient.fragment.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;

/**
 * @author Hamster
 * @since 14.10.2014
 */
public class ChoiceDialog extends BaseDialog {

    private static final String PARAM_CAPTION = "PARAM_CAPTION";
    private static final String PARAM_CHOICES = "PARAM_CHOICES";

    private LayoutInflater layoutInflater;
    private ItemChooseListener listener;

    public static ChoiceDialog newInstance(final String caption, final String[] choices) {
        final ChoiceDialog dialog = new ChoiceDialog();

        Bundle args = new Bundle();
        args.putString(PARAM_CAPTION, caption);
        args.putStringArray(PARAM_CHOICES, choices);

        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        final View view = inflater.inflate(R.layout.dialog_content_choice, container, false);

        ((ListView) view.findViewById(R.id.dialog_choice_list)).setAdapter(new ChoiceAdapter(getArguments().getStringArray(PARAM_CHOICES)));

        return wrapView(inflater, view, getArguments().getString(PARAM_CAPTION));
    }

    public void setItemChooseListener(final ItemChooseListener listener) {
        this.listener = listener;
    }

    private class ChoiceAdapter extends BaseAdapter {

        private final String[] choices;

        public ChoiceAdapter(final String[] choices) {
            this.choices = choices;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ChoiceViewHolder viewHolder;
            if(convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_choice, parent, false);
                viewHolder = new ChoiceViewHolder();
                viewHolder.text = (TextView) convertView.findViewById(R.id.choice_item_text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ChoiceViewHolder) convertView.getTag();
            }

            viewHolder.text.setText(choices[position]);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if(listener != null) {
                        listener.onItemSelected(position);
                    }
                }
            });

            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return choices[position];
        }

        @Override
        public int getCount() {
            return choices.length;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ChoiceViewHolder {
            public TextView text;
        }

    }

    public interface ItemChooseListener {
        void onItemSelected(int position);
    }

}
