package com.example.instilostandfound;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Class to build the email framework parameters
 */
public class SendMail extends AsyncTask<Void, Void, Void> {

    private Context context;
    private Session session;
    private String email;
    private String subject;
    private String message;
    private ProgressDialog progressDialog;

    /**
     * Constructor to initialise members
     * @param context calling class
     * @param email email of the reciever
     * @param subject subject of the email
     * @param message Content of the email
     */
    public SendMail(Context context, String email, String subject, String message) {

        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    /**
     * Shows progress bar before sending the mail
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context, "Sending message", "Please wait...", false, false);
    }

    /**
     * Notifies user after email is sent
     * @param aVoid
     */
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        Toast.makeText(context, "Message Sent", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this.context, NewsFeedFound.class);
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("username", email);
        context.startActivity(intent);
    }

    /**
     * Background process for setting up email parameters (gmail used here)
     * @param params list of parameters
     * @return  null parameter
     */
    @Override
    protected Void doInBackground(Void... params) {
        Properties props = new Properties();
        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //Creating a new session
        session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            //Authenticating the password
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
            }
        });
        try {
            MimeMessage mm = new MimeMessage(session);
            //Setting sender address
            mm.setFrom(new InternetAddress(Config.EMAIL));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mm.setSubject(subject);
            mm.setText(message);
            Transport.send(mm);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
