# Come utilizzare PennyMate
BackEnd porta 8080
FrontEnd porta 80

## Colegarsi al sito
Per utilizzare il sistema come utente è sufficiente collegarsi alla pagina: http://pennymate.3utilities.com/
IMPORTANTE: ricordate HTTP, non possediamo alcun certificato per l'utilizzo di HTTPS, ogni informazione registrata non avra alcuna assicurazione

## Scaricare una Versione (Apparte quella online)
Se si vuole utilizzare il sistema il Locale è sufficiente scaricare una delle versioni di sviluppo ed avviarle in locale nel seguente modo:
- Importare il DataBase su MySql, ed avviare il servizio.
- Avviare il server TomCat, tramite un IDE come eclipse compilare la classe PennyMateApplication.java.
- Avviare attraverso XAMPP o con altre metodologie il Server APACHE e MySql, importando il contenuto della cartella FE nella Directory definita su httpd.conf.
