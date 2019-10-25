package com.fenix.wakonga.envioemail;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;

public class Mail extends AsyncTask<Void, Void, Void> {

    //Declaring Variables
    private Context context;
    private Session session;

    //Information to send email
    private String email[];
    private String subject;
    private String message;
    String user="wakonga78@gmail.com";//usuario e email do remetente
     String pass="wakonga123fenix123";//senha

    //Progressdialog to show while seding email

    private ProgressDialog progressDialog;
    private Multipart file;

    public Mail() {
    }

    public Mail(Context context, String email[], String subject, String message) {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Show progress dialog while seding email
        progressDialog=ProgressDialog.show(context, "A enviar convite", "Aguarde...", false, false);
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();

        //Showing a sucess message
        Toast.makeText(context, "convite enviado", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props=new Properties();
        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, pass);


                    }
                });

        try {
            MimeMessage mm=new MimeMessage(session);
            //Setting sender adress
            mm.setFrom(new InternetAddress(user));
            //Adding receiver



            InternetAddress[] addressesTo=new InternetAddress[email.length];
            for (int i=0; i<email.length; i++){
                addressesTo[i]=new InternetAddress(email[i]);

            }
           // mm.setRecipients(MimeMessage.RecipientType.TO, addressesTo);
            mm.addRecipients(Message.RecipientType.TO, addressesTo);


            //Adding subject
            mm.setSubject(subject);
            mm.setText(message);
            //mm.setContent(file);
            Transport.send(mm);
        }catch (MessagingException e){
            e.printStackTrace();
        }

        return null;
    }


    public void addAttachment(String filename)  {
        filename = filename.replace("file:","").replace("//","/");
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        FileDataSource source = new FileDataSource(filename);
        try {
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            file.addBodyPart(messageBodyPart);
        } catch (MessagingException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Ficheiro não enviado! "+e.getMessage(), Toast.LENGTH_LONG);
        }

    }

    public String[] getEmail() {
        return email;
    }

    public void setEmail(String[] email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
