package uz.fintech.uzbankcard.common.codeadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;

import com.nirigo.mobile.view.passcode.adapters.PasscodeBaseAdapter;
import com.nirigo.mobile.view.passcode.models.PasscodeItem;

import java.util.Arrays;

import uz.fintech.uzbankcard.R;

public class PasscodeAdapter extends PasscodeBaseAdapter {
    private LayoutInflater inflater;

    public PasscodeAdapter(Context context) {
        super(Arrays.asList(
                new PasscodeItem("1", PasscodeItem.TYPE_NUMBER),
                new PasscodeItem("2", PasscodeItem.TYPE_NUMBER),
                new PasscodeItem("3", PasscodeItem.TYPE_NUMBER),
                new PasscodeItem("4", PasscodeItem.TYPE_NUMBER),
                new PasscodeItem("5", PasscodeItem.TYPE_NUMBER),
                new PasscodeItem("6", PasscodeItem.TYPE_NUMBER),
                new PasscodeItem("7", PasscodeItem.TYPE_NUMBER),
                new PasscodeItem("8", PasscodeItem.TYPE_NUMBER),
                new PasscodeItem("9", PasscodeItem.TYPE_NUMBER),
                new PasscodeItem("x", PasscodeItem.TYPE_CLEAR),
                new PasscodeItem("0", PasscodeItem.TYPE_NUMBER)
        ));
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PasscodeItem item = getItem(position);
        if(convertView == null || convertView.getTag() != PasscodeItemNum.class){
            convertView = inflater.inflate(R.layout.button_passcode_layout, parent, false);
           convertView.setTag(PasscodeItemNum.class);
        }

       AppCompatTextView numberTextView = convertView.findViewById(R.id.number);
        numberTextView.setText(item.getValue());
        convertView.setVisibility(item.getType() == PasscodeItem.TYPE_EMPTY ? View.INVISIBLE : View.VISIBLE);
        return convertView;

    }

}
