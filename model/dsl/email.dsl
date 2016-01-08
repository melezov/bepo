module email
{
  event SendEmail {
    String  from;
    String? replyTo;

    List<String>  to;
    List<String>  cc;
    List<String>  bcc;

    String  subject;
    String? textBody;
    String? htmlBody;

    List<Attachment>  attachments;
  }

  value Attachment {
    String  fileName;
    String  mimeType;
    Binary  content;
  }
}
