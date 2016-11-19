#!/bin/env  python
# coding=utf-8

__author__ = 'feel'

import os
import sys
import getopt
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.application import MIMEApplication
from email.mime.text import MIMEText
from  smtplib import SMTPException

sender = 'email'

receivers = ['email']

user = 'user'

smtpssl = True

passwd = 'passwd'

stmpServer = 'smtp.exmail.qq.com'

subject = ''

files = None

signature = '''
<p>
你的个性签名
</p>
'''

'''

发送文本

'''

message = signature


def sendText(content):
    flag = True
    # content = []
    try:
        if (flag):
            msg = MIMEText("<h1 style='color:red'>   " + ",".join(content) + "   service is fail</h1>",
                           _subtype='html', _charset='UFT-8')
            msg['Subject'] = "test"
            msg['From'] = sender
            msg['To'] = ";".join(receivers)
            smtpObj = smtplib.SMTP(stmpServer)
            smtpObj.sendmail(sender, receivers, msg.as_string())
            print "Successfully sent email"

    except SMTPException:
        print "Error: unable to send email"


'''

发送附件带文本内容

'''


def sendAttach(subject, files, message):
    try:
        msg = MIMEMultipart()
        msg['Subject'] = subject
        msg['From'] = sender
        msg['To'] = ";".join(receivers)

        if len(files) > 0:
            fileList = files.split(',')
            for file in fileList:
                if os.path.isfile(file):
                    filename = os.path.split(file)[1]
                    part = MIMEApplication(open(file, 'rb').read())
                    part.add_header('Content-Disposition', 'attachment', filename=filename)
                    msg.attach(part)

        msg.attach(MIMEText(message, _subtype='html', _charset='UFT-8'))

        if smtpssl:
            s = smtplib.SMTP_SSL(stmpServer, port=465, timeout=30)  # 连接smtp邮件服务器,端口默认是465
        else:
            s = smtplib.SMTP(stmpServer, port=25, timeout=30)  # 连接smtp邮件服务器,端口默认是25
        s.login(user, passwd)
        s.sendmail(sender, receivers, msg.as_string())  # 发送邮件
        s.close()

    except Exception:
        print "Error: unable to send email"


def usage():
    """

    The output  configuration file contents.



    Usage: python  sendMail.py [-s|--subject,["发送邮件主题"]] [-f|--files,[附件]] [-h|--help] [-v|--version] [message, =[邮件内容]]



    Description

            -s,--subject  邮件主题

            -f,--files    发送邮件附件, 逗号分隔

            -m ,--message 邮件文档内容,默认可以不填,为公司标签.

            -h,--help     查看 帮助文档

            -t,--receivers   邮件接收者

            -v,--version  显示版本号

    for example:

    python  sendMail.py  -s 发送邮件  -f /tmp/files,/tmp/files2


  """


def version():
    print("当前版本号:1.0")


def main():
    try:
        opts, args = getopt.getopt(sys.argv[1:], "ho:s:f:m:t:vo",
                                   ["help", "version" "subject=", "files=", "message=", "receivers=", "version"])

    except getopt.GetoptError as err:
        # print help information and exit:
        # will print something like "option -a not recognized"
        print str(err)
        print usage.__doc__
        sys.exit()

    if (len(opts) == 0):
        print usage.__doc__
        sys.exit()
    flag = False
    for o, a in opts:
        if o in ("-v", "--version"):
            version()
        elif o in ("-h", "--help"):
            print usage.__doc__
            sys.exit()
        elif o in ("-s", "--subject"):
            global subject
            subject = a
            flag = True

        elif o in ("-f", "--files"):
            global files
            files = a
        elif o in ("-m", "--message"):
            global message
            message = a

        elif o in ("-t", "--receivers"):
            global receivers
            receivers = a.split(",")

        else:
            print usage.__doc__
            sys.exit()

    if flag:
        sendAttach(subject, files, message)


if __name__ == "__main__":
    main()
