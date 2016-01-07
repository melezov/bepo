module bepo
{
  value Email {
    String  from;
    String? replyTo;

    List<String>  to;
    List<String>  cc;
    List<String>  bcc;

    String  subject;
    String? textBody;
    String? htmlBody;

    List<Bepo.Attachment>  attachments;
  }

  value Attachment {
    String  fileName;
    String  mimeType;
    Binary  bytes;
  }
}
