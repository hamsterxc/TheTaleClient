package com.lonebytesoft.thetaleclient.fragment.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;

import java.util.Arrays;
import java.util.List;

/**
 * @author Hamster
 * @since 14.10.2014
 */
public class ChoiceDialog extends BaseDialog {

    private static final String PARAM_CAPTION = "PARAM_CAPTION";
    private static final String PARAM_CHOICES = "PARAM_CHOICES";

    private int layoutResId;
    private int listViewResId;

    private ItemChooseListener listener;

    public static ChoiceDialog newInstance(final String caption, final String[] choices) {
        final ChoiceDialog dialog = new ChoiceDialog();

        Bundle args = new Bundle();
        args.putString(PARAM_CAPTION, caption);
        args.putStringArray(PARAM_CHOICES, choices);

        dialog.setArguments(args);
        return dialog;
    }

    public ChoiceDialog() {
        layoutResId = R.layout.dialog_content_choice;
        listViewResId = R.id.dialog_choice_list;
    }

    public void setLayout(final int layoutResId, final int listViewResId) {
        this.layoutResId = layoutResId;
        this.listViewResId = listViewResId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(layoutResId, container, false);

        ((ListView) view.findViewById(listViewResId)).setAdapter(new ChoiceAdapter(
                inflater,
                Arrays.asList(getArguments().getStringArray(PARAM_CHOICES)),
                new ItemChooseListener() {
                    @Override
                    public void onItemSelected(int position) {
                        dismiss();
                        if (listener != null) {
                            listener.onItemSelected(position);
                        }
                    }
                }));

        return wrapView(inflater, view, getArguments().getString(PARAM_CAPTION));
    }

    public void setItemChooseListener(final ItemChooseListener listener) {
        this.listener = listener;
    }

    public static class ChoiceAdapter extends BaseAdapter {

        private final LayoutInflater layoutInflater;
        private final List<String> choices;
        private final ItemChooseListener listener;

        public ChoiceAdapter(final LayoutInflater layoutInflater, final List<String> choices, final ItemChooseListener listener) {
            this.layoutInflater = layoutInflater;
            this.choices = choices;
            this.listener = listener;
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

            viewHolder.text.setText(choices.get(position));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemSelected(position);
                }
            });

            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return choices.get(position);
        }

        @Override
        public int getCount() {
            return choices.size();
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
