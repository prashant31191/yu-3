package com.sen;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Adminsss on 11-02-2016.
 */
public class ActData extends Activity
{
    String strCoppyText="";
    Button button,button1;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_data);

        button = (Button)findViewById(R.id.button);
        button1 = (Button)findViewById(R.id.button1);
        editText = (EditText)findViewById(R.id.editText);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCoppyText = editText.getText().toString();

                editText.setText("");
            }
        });
        button1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                editText.setText(""+strCoppyText);
                return false;
            }
        });


       final int sdk = android.os.Build.VERSION.SDK_INT;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strCoppyText = editText.getText().toString();


                if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(strCoppyText);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("text label",strCoppyText);
                    clipboard.setPrimaryClip(clip);
                }

                Toast.makeText(ActData.this, "Done...!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
