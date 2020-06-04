package servicesequest.hctx.net;

import android.app.Activity;
import android.content.Context;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputFilter;
import android.widget.EditText;

import androidx.annotation.NonNull;

public class HCPhoneFormattingTextWatcher extends PhoneNumberFormattingTextWatcher
{
    private Activity _context;
    EditText tv_Phone;

    public HCPhoneFormattingTextWatcher(Activity context, EditText txtPhone) {
        _context = context;
         tv_Phone =  txtPhone;
    }

    @Override
    public synchronized void afterTextChanged(Editable s) {
        super.afterTextChanged(s);

        if(tv_Phone != null && tv_Phone.getText().toString().startsWith("1"))
        {
            setEditTextMaxLength(tv_Phone, 16);
        }
        else
        {
            setEditTextMaxLength(tv_Phone, 14);
        }
    }

    public void setEditTextMaxLength(EditText editText, int length) {
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(length);
        editText.setFilters(FilterArray);
    }

}
