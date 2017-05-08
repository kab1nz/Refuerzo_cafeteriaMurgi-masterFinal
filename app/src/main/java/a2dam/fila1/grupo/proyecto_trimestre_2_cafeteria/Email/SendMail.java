package a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Email;

/**
 * Created by Manuel on 30/01/2017.
 */


import android.app.ProgressDialog;
import android.content.Context;
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

import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.AdapterPedidos;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.AdapterPedidosDetalles;


//Necesitamos un hilo para usar conexion a internet
public class SendMail extends AsyncTask<Void,Void,Void> {

    //Variables
    private Context context;
    private Session session;

    //Informacion del email
    private String email;
    private String subject;
    private String message;

    //Dialogo de envio (una animacion de espera mientras se envia
    private ProgressDialog progressDialog;

    //Constructor
    public SendMail(Context context, String email, String subject, String message){
        
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Informacion del dialogo
        progressDialog = ProgressDialog.show(context,"Enviado Email","Por favor, espere...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Cierra el dialogo cuando el email a acabado (Cuando el doInBackGround a terminado)
        progressDialog.dismiss();
        //Mensaje de confirmacion
        Toast.makeText(context,"Mensaje enviado a " + email,Toast.LENGTH_LONG).show();

    }

    @Override
    protected Void doInBackground(Void... params) {
        //Objeto que contiene las configuraciones del servidor de salida de nuestro proveedor de correo electronico
        Properties propiedades = new Properties();

        //Propiedades para Gmail, en caso de tener otro proveedor, cambiar los datos por los propios
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.socketFactory.port", "465");
        propiedades.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.port", "465");

        //Abrimos sesion con nuestro correo
        session = Session.getDefaultInstance(propiedades,
                new javax.mail.Authenticator() {
                    //Autentificacion
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
                    }
                });

        try {
            //Contenido del mensaje
            MimeMessage mensaje = new MimeMessage(session);

            //Remitente
            mensaje.setFrom(new InternetAddress(Config.EMAIL));
            //Destino
            mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Asunto
            mensaje.setSubject(subject);
            //Mensaje
            mensaje.setText(message);

            //Objeto (estatico) + metodo enviar
            Transport.send(mensaje);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}