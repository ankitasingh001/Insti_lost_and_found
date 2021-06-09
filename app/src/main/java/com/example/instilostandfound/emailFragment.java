package com.example.instilostandfound;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * Fragment class to send message to user
 */
public class emailFragment extends Fragment implements View.OnClickListener {
    private EditText editTextMessage;
    private Button buttonSend;
    private String username;
    private String senderid;
    private String recieverid;
    private String itemname;
    CreateFoundObject objectfound;

    /**
     * On create method when fragement is generated
     * @param inflater default param
     * @param container default param
     * @param savedInstanceState default param
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_email, container, false);
        editTextMessage = (EditText) v.findViewById(R.id.editTextMessage);
        buttonSend = (Button) v.findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);
        Bundle bundle = getArguments();
        if(bundle != null){
            username = bundle.getString("username");
            senderid = bundle.getString("username");
            recieverid = bundle.getString("recievername");
            itemname = bundle.getString("itemname");

        }

        return v;
    }

    /**
     * Method to send email with corresponding message
     */
    private void sendEmail() {
        String email = recieverid ;
        String subject = "Regarding posted item in Lost and Found App " + itemname;
        String autoMsg = "\n\nThis is an auto generated email. Please do not reply to this email.";
        String message = editTextMessage.getText().toString().trim() + "\n\nsent by ldap user : " + username  + autoMsg;
        SendMail sm = new SendMail(getActivity(), email, subject, message);
        sm.execute();
    }

    /**
     * On click method for sending the message button
     * @param v Claim button
     */
    @Override
    public void onClick(View v) {
        if(editTextMessage.getText().toString().length() < 1){
            editTextMessage.setError("Message can't be empty.");
            editTextMessage.requestFocus();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Message");
        builder.setMessage("Your message will be send as an email to the owner of this post");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendEmail();
                editTextMessage.setText("");
                //dialog.dismiss();

            }
        });

        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editTextMessage.setText("");
                Toast.makeText(getActivity(),"Message Discarded", Toast.LENGTH_SHORT).show();
               // dialog.dismiss();
            }
        });
        /**
         * Alert dialog to confirm if user wants to send email or not
         */
        AlertDialog ad = builder.create();
        ad.show();


    }
}
