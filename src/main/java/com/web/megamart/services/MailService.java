package com.web.megamart.services;


import com.web.megamart.payload.MailData;

public interface MailService {

    public boolean sendMail(MailData mailData);

}
